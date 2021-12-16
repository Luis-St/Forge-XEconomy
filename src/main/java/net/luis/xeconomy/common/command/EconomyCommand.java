package net.luis.xeconomy.common.command;

import java.io.File;
import java.nio.file.Path;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.brigadier.CommandDispatcher;

import net.luis.xeconomy.XEconomy;
import net.luis.xeconomy.common.economy.player.PlayerEconomyStorage;
import net.luis.xeconomy.common.economy.player.bank.BankPlayerStorage;
import net.luis.xeconomy.init.capability.ModCapabilities;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class EconomyCommand {
	
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("economy").executes(source -> {
			return getEconomyCommandList(source.getSource());
		}).then(Commands.literal("player").executes(source -> {
			return getPlayerList(source.getSource());
		}).then(Commands.literal("info").executes(source -> {
			return getPlayerInfoList(source.getSource());
		}).then(Commands.argument("player", EntityArgument.player()).executes(source -> {
			return getPlayerInfo(source.getSource(), EntityArgument.getPlayer(source, "player").getLevel(), EntityArgument.getPlayer(source, "player"));
		}) // after Player
			
				
		) // after Player Argument
				
				
		) // after Info Argument
				
				
		).then(Commands.literal("item").executes(source -> {
			return getItemEconomyList(source.getSource());
		}) // after Item
				
				
		).then(Commands.literal("save").executes(source -> {
			return saveEconomy(source.getSource());
		}) // after Save
			
				
		) // another literal
				
				
		);
	}
	
	protected static int getEconomyCommandList(CommandSourceStack sourceStack) {
		sourceStack.sendFailure(new TextComponent("This Command part is not implemented yet"));
		return 0;
	}
	
	protected static int getPlayerList(CommandSourceStack sourceStack) {
		sourceStack.getLevel().getCapability(ModCapabilities.ECONOMY, null).ifPresent(handler -> {
			sourceStack.sendSuccess(new TextComponent("Preset Players: "), false);
			for (ServerPlayer player : handler.getPlayers()) {
				sourceStack.sendSuccess(new TextComponent(" - " + player.getName().getString()), false);
			}
		});
		return 0;
	}
	
	protected static int getPlayerInfoList(CommandSourceStack sourceStack) {
		sourceStack.getLevel().getCapability(ModCapabilities.ECONOMY, null).ifPresent(handler -> {
			sourceStack.sendSuccess(new TextComponent("Preset Player Infos: "), false);
			for (ServerPlayer player : handler.getPlayers()) {
				PlayerEconomyStorage economyStorage = handler.getEconomyStorage(player);
				if (economyStorage != null) {
					sourceStack.sendSuccess(new TextComponent(" - " + player.getName().getString() + ": " + economyStorageToText(economyStorage).getString()), false);
				} else {
					sourceStack.sendFailure(new TextComponent("Fail to get Economy Storage for " + player.getName().getString()));
				}
			}
		});
		return 0;
	}
	
	protected static int getPlayerInfo(CommandSourceStack sourceStack, ServerLevel level, ServerPlayer player) {
		level.getCapability(ModCapabilities.ECONOMY, null).ifPresent(handler -> {
			PlayerEconomyStorage economyStorage = handler.getEconomyStorage(player);
			if (economyStorage != null) {
				sourceStack.sendSuccess(new TextComponent("Economy Storage for " + player.getName().getString() + ": " + economyStorageToText(economyStorage).getString()), false);
			} else {
				sourceStack.sendFailure(new TextComponent("Fail to get Economy Storage for " + player.getName().getString()));
			}
		});
		return 0;
	}
	
	protected static int getItemEconomyList(CommandSourceStack sourceStack) {
		sourceStack.sendFailure(new TextComponent("This Command part is not implemented yet"));
		return 0;
	}
	
	@VisibleForTesting
	protected static int saveEconomy(CommandSourceStack sourceStack) {
		Path path = new File(System.getProperty("user.home")).toPath().resolve("Desktop").resolve("economy_" + XEconomy.getInstance().getWorldName() + ".json");
		try {
			sourceStack.getLevel().getCapability(ModCapabilities.ECONOMY, null).ifPresent(handler -> {
				handler.saveAsJson(path);
			});
			sourceStack.sendSuccess(new TextComponent("Save Economy to " + path), false);
		} catch (Exception e) {
			sourceStack.sendFailure(new TextComponent("Fail to save Economy"));
		}
		return 0;
	}
	
	
	
	
	
	
	// TODO: better way to formate
	protected static Component economyStorageToText(PlayerEconomyStorage economyStorage) {
		BankPlayerStorage bankStorage = economyStorage.getBankStorage();
		return new TextComponent("\n   > Money: " + economyStorage.getMoney())
				.append("\n   > Inflation: " + economyStorage.getInflation())
				.append("\n   > BankStorage: Money: " + bankStorage.getMoney() + " Interest: " + bankStorage.getInterest()); // TODO: better way, add BankPlayerStorage
	}
	
}
