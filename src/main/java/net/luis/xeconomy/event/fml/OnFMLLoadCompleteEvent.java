package net.luis.xeconomy.event.fml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

//@EventBusSubscriber(modid = XEconomy.MOD_ID, bus = Bus.MOD)
public class OnFMLLoadCompleteEvent {
	
//	@SubscribeEvent
	@SuppressWarnings("deprecation")
	public static void loadComplete(FMLLoadCompleteEvent event) throws IOException {
		Path path = new File(System.getProperty("user.home")).toPath().resolve("Desktop").resolve("economies.txt");
		Files.createFile(path);
		BufferedWriter writer = Files.newBufferedWriter(path);
		for (Item item : Registry.ITEM.stream().filter(item -> true).collect(Collectors.toList())) {
			String name = item.getRegistryName().getPath();
			writer.write("\tpublic static final ItemEconomy " + name.toUpperCase() + " = register(Items." + name.toUpperCase() + ", new ItemEconomy(0, 0, 0, 0));\n");
		}
		writer.flush();
		writer.close();
	}
	
}
