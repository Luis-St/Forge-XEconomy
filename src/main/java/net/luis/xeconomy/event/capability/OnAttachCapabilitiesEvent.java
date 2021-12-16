package net.luis.xeconomy.event.capability;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.luis.xeconomy.XEconomy;
import net.luis.xeconomy.common.capability.provider.EconomyCapabilityProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = XEconomy.MOD_ID)
public class OnAttachCapabilitiesEvent {
	
	@SubscribeEvent
	public static void attachLevelChunkCapabilities(AttachCapabilitiesEvent<LevelChunk> event) {
		
	}
	
	@SubscribeEvent
	public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof Player) {
			
		}
	}
	
	@SubscribeEvent
	public static void attachItemStackCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
		
	}
	
	@SubscribeEvent
	public static void attachBlockEntityCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
		
	}
	
	@SubscribeEvent
	public static void attachLevelCapabilities(AttachCapabilitiesEvent<Level> event) {
		if (event.getObject() instanceof ServerLevel) {
			event.addCapability(new ResourceLocation(XEconomy.MOD_ID, "economy"), new EconomyCapabilityProvider());
		}
	}

}