package net.luis.nero;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.luis.nero.init.villager.ModPoiTypes;
import net.luis.nero.init.villager.ModVillagerProfessions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Nero.MOD_ID)
public class Nero {
	
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "nero_economy";
	public static final String MINECRAFT_ID = "minecraft";
	public static final boolean RUN_IN_IDE = FMLEnvironment.production;
	
	private static Nero nero_economy;
	
	public Nero() {
		nero_economy = this;
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		ModPoiTypes.POI_TYPES.register(eventBus);
		ModVillagerProfessions.PROFESSIONS.register(eventBus);
	}
	
	public static Nero getInstance() {
		return nero_economy;
	}
	
}