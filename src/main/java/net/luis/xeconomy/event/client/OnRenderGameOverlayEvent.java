package net.luis.xeconomy.event.client;

import net.luis.xeconomy.XEconomy;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = XEconomy.MOD_ID, value = Dist.CLIENT)
public class OnRenderGameOverlayEvent {
	
	@SubscribeEvent
	public static void renderGameOverlay(RenderGameOverlayEvent event) {
		
	}
	
}
