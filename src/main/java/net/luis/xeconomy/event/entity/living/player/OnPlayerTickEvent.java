package net.luis.xeconomy.event.entity.living.player;

import net.luis.xeconomy.XEconomy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = XEconomy.MOD_ID)
public class OnPlayerTickEvent {

	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent event) {
		
	}

}

