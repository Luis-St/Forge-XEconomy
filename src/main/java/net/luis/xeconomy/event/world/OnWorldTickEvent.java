package net.luis.xeconomy.event.world;

import net.luis.xeconomy.XEconomy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = XEconomy.MOD_ID)
public class OnWorldTickEvent {

	@SubscribeEvent
	public static void worldTick(TickEvent.WorldTickEvent event) {
		
	}

}
