package net.luis.xeconomy.event.entity;

import net.luis.xeconomy.XEconomy;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = XEconomy.MOD_ID)
public class OnEntityJoinWorldEvent {

	@SubscribeEvent
	public static void enityJoinWorld(PlayerLoggedInEvent event) {
//		if (event.getPlayer() instanceof ServerPlayer player) {
//			if (player.getCommandSenderWorld() instanceof ServerLevel level) {
//				level.getCapability(ModCapabilities.ECONOMY, null).ifPresent(handler -> {
//					handler.registerPlayer(player);
//				});
//			}
//		}
	}

}

