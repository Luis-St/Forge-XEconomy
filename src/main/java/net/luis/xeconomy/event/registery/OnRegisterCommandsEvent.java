package net.luis.xeconomy.event.registery;

import net.luis.xeconomy.XEconomy;
import net.luis.xeconomy.common.command.EconomyCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = XEconomy.MOD_ID)
public class OnRegisterCommandsEvent {
	
	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event) {
		EconomyCommand.register(event.getDispatcher());
	}
}
