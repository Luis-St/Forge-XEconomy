package net.luis.xeconomy.common.capability.interfaces;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.luis.xeconomy.common.economy.bank.BankCredit;
import net.luis.xeconomy.common.economy.bank.PlayerBankStorage;
import net.luis.xeconomy.common.economy.item.EconomyItemStack;
import net.luis.xeconomy.common.economy.item.ItemEconomy;
import net.luis.xeconomy.common.economy.player.EconomyPlayer;
import net.luis.xeconomy.common.economy.player.PlayerEconomyStorage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

public interface IEconomyCapability {
	
	boolean isPlayerRegistered(ServerPlayer player);
	
	void registerPlayer(ServerPlayer player);
	
	List<ServerPlayer> getPlayers();
	
	List<EconomyPlayer> getEconomyPlayers();
	
	@Nonnull
	EconomyPlayer getPlayer(ServerPlayer player);
	
	@Nonnull
	EconomyPlayer getPlayer(UUID uuid);
	
	List<ItemEconomy> getEconomies();
	
	@Nonnull
	ItemEconomy getEconomy(Item item);
	
	int getBuyPrice(EconomyPlayer player, EconomyItemStack economyStack);
	
	int getSellPrice(EconomyPlayer player, EconomyItemStack economyStack);
	
	boolean canBuy(EconomyPlayer player, EconomyItemStack economyStack, boolean includeBank);
	
	void buy(EconomyPlayer player, EconomyItemStack economyStack, boolean includeBank);
	
	boolean canSell(EconomyPlayer player, EconomyItemStack economyStack);
	
	void sell(EconomyPlayer player, EconomyItemStack economyStack, boolean sellPossible);
	
	List<PlayerEconomyStorage> getEconomyStorages();
	
	@Nonnull
	PlayerEconomyStorage getEconomyStorage(EconomyPlayer player);
	
	List<PlayerBankStorage> getBankStorages();
	
	@Nonnull
	PlayerBankStorage getBankStorage(EconomyPlayer player);
	
	boolean hasMoney(EconomyPlayer player, int money, boolean includeBank);
	
	@Nonnull
	List<BankCredit> getBankCredits(EconomyPlayer player);
	
	void saveAsJson(Path path);
	
	void loadAsJson(Path path);
	
}
