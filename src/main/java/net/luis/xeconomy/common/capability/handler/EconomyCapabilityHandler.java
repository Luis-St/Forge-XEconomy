package net.luis.xeconomy.common.capability.handler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.commons.compress.utils.Lists;

import com.google.common.collect.ImmutableMap;
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
import net.luis.xeconomy.common.economy.ItemEconomy;
import net.luis.xeconomy.common.economy.ItemStorage;
import net.luis.xeconomy.common.economy.player.PlayerEconomyStorage;
import net.luis.xeconomy.common.economy.player.bank.BankCredit;
import net.luis.xeconomy.common.economy.player.bank.BankPlayerStorage;
import net.luis.xeconomy.common.economy.update.BankStorageUpdate;
import net.luis.xeconomy.common.economy.update.PlayerStorageUpdate;
import net.luis.xeconomy.common.serialization.Codecs;
import net.luis.xeconomy.common.serialization.JsonHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraftforge.server.ServerLifecycleHooks;

public class EconomyCapabilityHandler implements IEconomyCapability {
	
	protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	public static final Codec<EconomyCapabilityHandler> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Codecs.ECONOMY_STORAGES.fieldOf("storages").forGetter((handler) -> {
			return handler.economyStorages;
		}), Codecs.ECONOMIES.fieldOf("economies").forGetter((handler) -> {
			return handler.economies;
		})).apply(instance, EconomyCapabilityHandler::new);
	});
	
	protected final Map<Item, ItemEconomy> economies;
	protected final Map<UUID, PlayerEconomyStorage> economyStorages;
	
	public EconomyCapabilityHandler() {
		this(Maps.newHashMap(), Maps.newHashMap());
	}
	
	protected EconomyCapabilityHandler(Map<UUID, PlayerEconomyStorage> economyStorages, Map<Item, ItemEconomy> economies) {
		this.economies = economies instanceof ImmutableMap ? Maps.newHashMap(economies) : economies;
		this.economyStorages = economyStorages instanceof ImmutableMap ? Maps.newHashMap(economyStorages) : economyStorages;
		this.init();
	}

	@Override
	public void init() {
		
	}
	
	@Override
	public void registerPlayer(ServerPlayer player) {
		this.economyStorages.putIfAbsent(player.getUUID(), new PlayerEconomyStorage());
	}
	
	@Override
	public List<ServerPlayer> getPlayers() {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		List<ServerPlayer> players = Lists.newArrayList();
		if (server != null) {
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				if (this.economyStorages.containsKey(player.getUUID())) {
					players.add(player);
				} else {
					XEconomy.LOGGER.warn("The player {} joined the server, without being registered", player.getName().getString());
				}
			}
		}
		return players;
	}

	@Override
	@Nullable
	public ServerPlayer getPlayer(UUID uuid) {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server != null) {
			return server.getPlayerList().getPlayer(uuid);
		}
		return null;
	}

	@Override
	public List<ItemEconomy> getEconomies() {
		return this.economies.values().stream().collect(Collectors.toList());
	}

	@Override
	public ItemEconomy getEconomy(Item item) {
		return this.economies.get(item);
	}
	
	protected ItemEconomy getEconomy(ItemStorage itemStorage) {
		return this.getEconomy(itemStorage.item());
	}

	@Override
	public boolean canBuy(ServerPlayer player, ItemStorage itemStorage, boolean includeBank) {
		if (this.hasMoney(player, this.getBuyPrice(player, itemStorage), includeBank)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void buy(ServerPlayer player, ItemStorage itemStorage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canSell(ServerPlayer player, ItemStorage itemStorage) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void sell(ServerPlayer player, ItemStorage itemStorage) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getBuyPrice(ServerPlayer player, ItemStorage itemStorage) {
		return 0;
	}

	@Override
	public int getSellPrice(ServerPlayer player, ItemStorage itemStorage) {
		return this.economies.get(itemStorage.item()).getPrice(itemStorage, this.getEconomyStorage(player).getInflation());
	}

	@Override
	public List<PlayerEconomyStorage> getEconomyStorages() {
		return this.economyStorages.values().stream().collect(Collectors.toList());
	}
	
	@Override
	public PlayerEconomyStorage getEconomyStorage(ServerPlayer player) {
		return this.economyStorages.get(player.getUUID());
	}
	
	@Override
	public List<BankPlayerStorage> getBankStorages() {
		return this.getEconomyStorages().stream().map(PlayerEconomyStorage::getBankStorage).collect(Collectors.toList());
	}

	@Override
	public BankPlayerStorage getBankStorage(ServerPlayer player) {
		return this.getEconomyStorage(player).getBankStorage();
	}

	@Override
	public boolean hasMoney(ServerPlayer player, int money, boolean includeBank) {
		if (includeBank) {
			return this.getEconomyStorage(player).getMoney() + this.getBankStorage(player).getMoney() >= money;
		}
		return this.getEconomyStorage(player).getMoney() >= money;
	}
	
	@Override
	public List<BankCredit> getBankCredits(ServerPlayer player) {
		return this.getBankStorage(player).getBankCredits();
	}
	
	@Override
	public void updatePlayer(ServerPlayer player, PlayerStorageUpdate storageUpdate, BankStorageUpdate bankUpdate) {
		this.getEconomyStorage(player).update(player, storageUpdate, bankUpdate);
	}
	
	@Override
	public void updateItem(Item item, double inflation) {
		this.getEconomy(item).update(item, inflation);
	}
	
	@Override
	public void saveAsJson(Path path) {
		try {
			if (this.economies != null && !this.economies.isEmpty() && this.economyStorages != null && !this.economyStorages.isEmpty()) {
				Function<EconomyCapabilityHandler, DataResult<JsonElement>> function = JsonOps.INSTANCE.withEncoder(EconomyCapabilityHandler.CODEC);
				Optional<JsonElement> optional = function.apply(this).result();
				if (optional.isPresent()) {
					JsonHelper.saveEconomy(GSON, optional.get(), path);
				} else {
					XEconomy.LOGGER.warn("Fail to encode the Economy Codec");
				}
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
				this.economies.clear();
				this.economyStorages.clear();
				this.economies.putAll(handler.economies);
				this.economyStorages.putAll(handler.economyStorages);
			} else {
				XEconomy.LOGGER.warn("Fail to decode the Economy Codec");
			}
		} catch (IOException e) {
			XEconomy.LOGGER.error("Fail to load the Economy from {}", XEconomy.getInstance().getEconomyPath());
		}
	}

}
