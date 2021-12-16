package net.luis.xeconomy.common.economy;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class EconomyHelper {
	
	protected static final ItemEconomy DEFAULT = new ItemEconomy(50);
	protected static final Map<Item, ItemEconomy> ECONOMIES = Maps.newHashMap();
	
	public static void setup() {
		// TODO: add Registry
	}
	
	public static void init(Map<Item, ItemEconomy> economies) {
		economies.put(Items.ACACIA_FENCE, new ItemEconomy(10));
	}
	
	public static ItemEconomy getOrDefault(Item item) {
		return ECONOMIES.get(item) != null ? ECONOMIES.get(item) : DEFAULT;
	}
	
}
