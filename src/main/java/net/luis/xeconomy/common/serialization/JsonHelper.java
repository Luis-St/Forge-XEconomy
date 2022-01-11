package net.luis.xeconomy.common.serialization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.luis.xeconomy.XEconomy;

public class JsonHelper {
	
	public static void saveEconomy(Gson gson, JsonElement jsonElement, Path path) throws IOException {
		String element = gson.toJson(jsonElement);
		if (!Files.exists(path)) {
			Files.createDirectories(path.getParent());
			Files.createFile(path);
			XEconomy.LOGGER.info("Create new Economy File in {}", path);
		}
		BufferedWriter writer = Files.newBufferedWriter(path);
		writer.write(element);
		writer.flush();
		writer.close();
	}
	
	public static JsonElement loadEconomy(Path path) throws IOException {
		if (!Files.exists(path)) {
			XEconomy.LOGGER.info("The Economy File does not exists in Path {}", path);
			return null;
		}
		BufferedReader reader = new BufferedReader(new FileReader(path.toFile()));
		JsonElement element = JsonParser.parseReader(reader);
		reader.close();
		return element;
	}
	
}
