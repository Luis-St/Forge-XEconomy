package net.luis.xeconomy.event.capability;

import net.luis.xeconomy.XEconomy;
import net.luis.xeconomy.common.capability.interfaces.IEconomyCapability;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = XEconomy.MOD_ID, bus = Bus.MOD)
public class OnRegisterCapabilitiesEvent {
	
	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(IEconomyCapability.class);
	}

}
