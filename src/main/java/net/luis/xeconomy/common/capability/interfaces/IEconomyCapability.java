package net.luis.xeconomy.common.capability.interfaces;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.luis.xeconomy.common.economy.ItemEconomy;
import net.luis.xeconomy.common.economy.ItemStorage;
import net.luis.xeconomy.common.economy.player.PlayerEconomyStorage;
import net.luis.xeconomy.common.economy.player.bank.BankCredit;
import net.luis.xeconomy.common.economy.player.bank.BankPlayerStorage;
import net.luis.xeconomy.common.economy.update.BankStorageUpdate;
import net.luis.xeconomy.common.economy.update.PlayerStorageUpdate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

public interface IEconomyCapability {
	
	void init();
	
	void registerPlayer(ServerPlayer player);
	
	List<ServerPlayer> getPlayers();
	
	@Nullable
	ServerPlayer getPlayer(UUID uuid);
	
	List<ItemEconomy> getEconomies();
	
	ItemEconomy getEconomy(Item item);
	
	boolean canBuy(ServerPlayer player, ItemStorage itemStorage, boolean includeBank);
	
	void buy(ServerPlayer player, ItemStorage itemStorage);
	
	boolean canSell(ServerPlayer player, ItemStorage itemStorage);
	
	void sell(ServerPlayer player, ItemStorage itemStorage);
	
	int getBuyPrice(ServerPlayer player, ItemStorage itemStorage);
	
	int getSellPrice(ServerPlayer player, ItemStorage itemStorage);
	
	List<PlayerEconomyStorage> getEconomyStorages();
	
	PlayerEconomyStorage getEconomyStorage(ServerPlayer player);
	
	List<BankPlayerStorage> getBankStorages();
	
	BankPlayerStorage getBankStorage(ServerPlayer player);
	
	boolean hasMoney(ServerPlayer player, int money, boolean includeBank);
	
	List<BankCredit> getBankCredits(ServerPlayer player);
	
	void updatePlayer(ServerPlayer player, PlayerStorageUpdate storageUpdate, BankStorageUpdate bankUpdate);
	
	void updateItem(Item item, double inflation);
	
	void saveAsJson(Path path);
	
	void loadAsJson(Path path);
	
}
