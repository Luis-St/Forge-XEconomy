package net.luis.xeconomy;

import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.luis.xeconomy.init.villager.ModPoiTypes;
import net.luis.xeconomy.init.villager.ModVillagerProfessions;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod(XEconomy.MOD_ID)
public class XEconomy {
	
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "xeconomy";
	public static final String MINECRAFT_ID = "minecraft";
	public static final boolean RUN_IN_IDE = FMLEnvironment.production;
	
	public static final Path ECONOMY_PATH = FMLPaths.GAMEDIR.get().resolve("economy");
	
	private static XEconomy nero;
	
	public XEconomy() {
		nero = this;
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		ModPoiTypes.POI_TYPES.register(eventBus);
		ModVillagerProfessions.PROFESSIONS.register(eventBus);
	}
	
	public static XEconomy getInstance() {
		return nero;
	}
	
	public String getWorldName() {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		return server != null ? server.getWorldData().getLevelName().toLowerCase().replace(" ", "_") : "default";
	}
	
	public Path getEconomyPath() {
		return XEconomy.ECONOMY_PATH.resolve("economy_" + this.getWorldName() + ".json");
	}
	
}