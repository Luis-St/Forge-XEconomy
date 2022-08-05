package net.luis.xeconomy.common.capability.handler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.luis.xeconomy.XEconomy;
import net.luis.xeconomy.common.capability.interfaces.IEconomyCapability;
import net.luis.xeconomy.common.economy.bank.BankCredit;
import net.luis.xeconomy.common.economy.bank.PlayerBankStorage;
import net.luis.xeconomy.common.economy.item.EconomyItemStack;
import net.luis.xeconomy.common.economy.item.ItemEconomy;
import net.luis.xeconomy.common.economy.player.EconomyPlayer;
import net.luis.xeconomy.common.economy.player.PlayerEconomyStorage;
import net.luis.xeconomy.common.economy.player.PlayerStorage;
import net.luis.xeconomy.common.serialization.JsonHelper;
import net.minecraft.core.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.server.ServerLifecycleHooks;

public class EconomyCapabilityHandler implements IEconomyCapability {

	protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	@SuppressWarnings("deprecation")
	public static final Codec<EconomyCapabilityHandler> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Codec.list(EconomyPlayer.CODEC).fieldOf("economy_players").forGetter((handler) -> {
			return handler.economyPlayers;
		}), Codec.unboundedMap(EconomyPlayer.CODEC, PlayerStorage.CODEC).fieldOf("player_storages").forGetter((handler) -> {
			return handler.playerStorages;
		}), Codec.unboundedMap(Registry.ITEM.byNameCodec(), ItemEconomy.CODEC).fieldOf("item_economies").forGetter((handler) -> {
			return handler.itemEconomies;
		})).apply(instance, EconomyCapabilityHandler::new);
	});
	
	protected final List<EconomyPlayer> economyPlayers;
	protected final Map<EconomyPlayer, PlayerStorage> playerStorages;
	protected final Map<Item, ItemEconomy> itemEconomies;
	
	public EconomyCapabilityHandler() {
		this(Lists.newArrayList(), Maps.newHashMap(), Maps.newHashMap());
	}
	
	public EconomyCapabilityHandler(List<EconomyPlayer> economyPlayers, Map<EconomyPlayer, PlayerStorage> playerStorages, Map<Item, ItemEconomy> itemEconomies) {
		this.economyPlayers = Lists.newArrayList(economyPlayers);
		this.playerStorages = Maps.newHashMap(playerStorages);
		this.itemEconomies = Maps.newHashMap(itemEconomies);
	}
	
	@Override
	public boolean isPlayerRegistered(ServerPlayer player) {
		EconomyPlayer economyPlayer = new EconomyPlayer(player);
		if (this.economyPlayers.contains(economyPlayer)) {
			return this.playerStorages.containsKey(economyPlayer);
		}
		return false;
	}
	
	@Override
	public void registerPlayer(ServerPlayer player) {
		if (!this.isPlayerRegistered(player)) {
			EconomyPlayer economyPlayer = new EconomyPlayer(player);
			this.economyPlayers.add(economyPlayer);
			this.playerStorages.put(economyPlayer, new PlayerStorage(new PlayerEconomyStorage(economyPlayer, 0), new PlayerBankStorage(economyPlayer, 0)));
		}
	}

	@Override
	public List<ServerPlayer> getPlayers() {
		return this.economyPlayers.stream().map(EconomyPlayer::asPlayer).filter(player -> player != null).collect(Collectors.toList());
	}

	@Override
	public List<EconomyPlayer> getEconomyPlayers() {
		return this.economyPlayers;
	}
	
	@Nonnull
	@Override
	public EconomyPlayer getPlayer(ServerPlayer serverPlayer) {
		if (serverPlayer != null) {
			Optional<EconomyPlayer> optional = this.economyPlayers.stream().filter(player -> player.equals(new EconomyPlayer(serverPlayer))).findFirst();
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}

	@Nonnull
	@Override
	public EconomyPlayer getPlayer(UUID uuid) {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server != null) {
			return this.getPlayer(server.getPlayerList().getPlayer(uuid));
		}
		return null;
	}

	@Override
	public List<ItemEconomy> getEconomies() {
		return this.itemEconomies.values().stream().filter(itemEconomy -> itemEconomy != null).collect(Collectors.toList());
	}

	@Nonnull
	@Override
	public ItemEconomy getEconomy(Item item) {
		return this.itemEconomies.get(item);
	}

	@Override
	public int getBuyPrice(EconomyPlayer player, EconomyItemStack economyStack) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSellPrice(EconomyPlayer player, EconomyItemStack economyStack) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean canBuy(EconomyPlayer player, EconomyItemStack economyStack, boolean includeBank) {
		return this.hasMoney(player, this.getBuyPrice(player, economyStack), includeBank);
	}

	@Override
	public void buy(EconomyPlayer player, EconomyItemStack economyStack, boolean includeBank) {
		int price = this.getBuyPrice(player, economyStack);
		if (this.canBuy(player, economyStack, includeBank)) {
			List<ItemStack> buyedStacks = economyStack.asItemStacks();
			int priceLeft = this.payMoney(player, price, includeBank);
			if (priceLeft > 0) {
				if (!this.cancelBuying(player, price, priceLeft, includeBank)) {
					this.addBuyedItems(player.asPlayer(), buyedStacks);
				}
			} else {
				this.addBuyedItems(player.asPlayer(), buyedStacks);
			}
		}
	}
	
	protected int payMoney(EconomyPlayer player, int money, boolean includeBank) {
		int leftMoney = 0;
		if (this.hasMoney(player, money, includeBank)) {
			PlayerEconomyStorage economyStorage = this.getEconomyStorage(player);
			if (includeBank) {
				PlayerBankStorage bankStorage = this.getBankStorage(player);
				leftMoney = bankStorage.money(money);
				if (leftMoney > 0) {
					leftMoney = economyStorage.money(leftMoney);
				}
			} else {
				leftMoney = economyStorage.money(money);
			}
		}
		return leftMoney;
	}
	
	protected boolean cancelBuying(EconomyPlayer player, int price, int priceLeft, boolean includeBank) {
		if (price != 0 && priceLeft != 0) {
			int payedMoney = price - priceLeft;
			this.getEconomyStorage(player).money(payedMoney);
		}
		return false;
	}
	
	protected void addBuyedItems(ServerPlayer player, List<ItemStack> buyedStacks) {
		if (player != null) {
			Optional<IItemHandler> optional = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).resolve();
			if (optional.isPresent()) {
				Iterator<ItemStack> iterator = buyedStacks.iterator();
				while (iterator.hasNext()) {
					this.addOrDropItem(player, optional.get(), iterator.next());
					iterator.remove();
				}
			}
		}
	}
	
	protected void addOrDropItem(ServerPlayer player, IItemHandler handler, ItemStack buyedStack) {
		ItemStack stack = ItemStack.EMPTY;
		for (int slot = 0; slot < handler.getSlots(); slot++) {
			stack = handler.insertItem(slot, stack, false);
			if (stack.isEmpty()) {
				break;
			}
		}
		if (!stack.isEmpty()) {
			Containers.dropItemStack(player.getLevel(), player.getX(), player.getY(), player.getZ(), stack);
		}
	}
	
	@Override
	public boolean canSell(EconomyPlayer player, EconomyItemStack economyStack) {
		List<ItemStack> sellStacks = economyStack.asItemStacks();
		ServerPlayer serverPlayer = player.asPlayer();
		for (ItemStack itemStack : sellStacks) {
			if (!this.hasItemStackInInventory(serverPlayer, itemStack)) {
				return false;
			}
		}
		return true;
	}

	protected boolean hasItemStackInInventory(ServerPlayer player, ItemStack itemStack) {
		if (player != null) {
			Optional<IItemHandler> optional = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).resolve();
			if (optional.isPresent()) {
				IItemHandler handler = optional.get();
				for (int i = 0; i < handler.getSlots(); i++) {
					ItemStack stack = handler.getStackInSlot(i);
					if (stack.getItem() == itemStack.getItem() && stack.getCount() >= itemStack.getCount()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public void sell(EconomyPlayer player, EconomyItemStack economyStack, boolean sellPossible) {
		if (this.canSell(player, economyStack)) {
			this.removeSelledItems(player.asPlayer(), economyStack.asItemStacks());
			this.getEconomyStorage(player).money(this.getSellPrice(player, economyStack));
		}
	}
	
	protected void removeSelledItems(ServerPlayer player, List<ItemStack> selledStacks) {
		if (player != null) {
			Optional<IItemHandler> optional = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).resolve();
			if (optional.isPresent()) {
				Iterator<ItemStack> iterator = selledStacks.iterator();
				while (iterator.hasNext()) {
					this.removeItem(player, optional.get(), iterator.next());
					iterator.remove();
				}
			}
		}
	}
	
	protected void removeItem(ServerPlayer player, IItemHandler handler, ItemStack selledStack) {
		List<Integer> slots = Lists.newArrayList();
		for (int slot = 0; slot < handler.getSlots(); slot++) {
			if (handler.getStackInSlot(slot).getItem() == selledStack.getItem()) {
				slots.add(slot);
			}
		}
		if (!slots.isEmpty()) {
			int count = selledStack.getCount();
			for (int slot : slots) {
				count -= handler.extractItem(slot, count, false).getCount();
				if (0 >= count) {
					break;
				}
			}
		}
	}

	@Override
	public List<PlayerEconomyStorage> getEconomyStorages() {
		return this.playerStorages.values().stream().map(PlayerStorage::economyStorage).filter(economyStorage -> economyStorage != null).collect(Collectors.toList());
	}

	@Nonnull
	@Override
	public PlayerEconomyStorage getEconomyStorage(EconomyPlayer player) {
		if (player != null) {
			PlayerStorage playerStorage = this.playerStorages.get(player);
			return playerStorage != null ? playerStorage.economyStorage() : null;
		}
		return null;
	}

	@Override
	public List<PlayerBankStorage> getBankStorages() {
		return this.playerStorages.values().stream().map(PlayerStorage::bankStorage).filter(bankStorage -> bankStorage != null).collect(Collectors.toList());
	}

	@Nonnull
	@Override
	public PlayerBankStorage getBankStorage(EconomyPlayer player) {
		if (player != null) {
			PlayerStorage playerStorage = this.playerStorages.get(player);
			return playerStorage != null ? playerStorage.bankStorage() : null;
		}
		return null;
	}

	@Override
	public boolean hasMoney(EconomyPlayer player, int money, boolean includeBank) {
		PlayerEconomyStorage economyStorage = this.getEconomyStorage(player);
		if (includeBank) {
			PlayerBankStorage bankStorage = this.getBankStorage(player);
			return economyStorage.getMoney() + bankStorage.getMoney() >= money;
		}
		return economyStorage.getMoney() >= money;
	}
	
	@Nonnull
	@Override
	public List<BankCredit> getBankCredits(EconomyPlayer player) {
		return this.getBankStorage(player).getBankCredits();
	}
	
	@Override
	public void saveAsJson(Path path) {
		try {
			Function<EconomyCapabilityHandler, DataResult<JsonElement>> function = JsonOps.INSTANCE.withEncoder(EconomyCapabilityHandler.CODEC);
			Optional<JsonElement> optional = function.apply(this).result();
			if (optional.isPresent()) {
				JsonHelper.saveEconomy(GSON, optional.get(), path);
			} else {
				XEconomy.LOGGER.warn("Fail to encode the Economy Codec");
			}
		} catch (IOException e) {
			XEconomy.LOGGER.error("Fail to save the Economy to {}", path);
		}
	}

	@Override
	public void loadAsJson(Path path) {
		try {
			Function<JsonElement, DataResult<Pair<EconomyCapabilityHandler, JsonElement>>> function = JsonOps.INSTANCE.withDecoder(EconomyCapabilityHandler.CODEC);
			Optional<Pair<EconomyCapabilityHandler, JsonElement>> optional = function.apply(JsonHelper.loadEconomy(XEconomy.getInstance().getEconomyPath())).result();
			if (optional.isPresent()) {
				EconomyCapabilityHandler handler = optional.get().getFirst();
				this.economyPlayers.clear();
				this.playerStorages.clear();
				this.itemEconomies.clear();
				this.economyPlayers.addAll(handler.economyPlayers);
				this.playerStorages.putAll(handler.playerStorages);
				this.itemEconomies.putAll(handler.itemEconomies);
			} else {
				XEconomy.LOGGER.warn("Fail to decode the Economy Codec");
			}
		} catch (IOException e) {
			XEconomy.LOGGER.error("Fail to load the Economy from {}", XEconomy.getInstance().getEconomyPath());
		}
	}

}
