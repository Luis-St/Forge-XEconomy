package net.luis.xeconomy.init.util.tags;

import net.luis.xeconomy.XEconomy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

public class ModItemTags {
	
	public static final Tag.Named<Item> GLASS_SHARDS = ItemTags.createOptional(new ResourceLocation(XEconomy.MOD_ID, "glass_shards"));
	public static final Tag.Named<Item> SHARDS = ItemTags.createOptional(new ResourceLocation(XEconomy.MOD_ID, "shards"));
	public static final Tag.Named<Item> CHRUSHED_ORE = ItemTags.createOptional(new ResourceLocation(XEconomy.MOD_ID, "chrushed_ore"));
	public static final Tag.Named<Item> ORBS = ItemTags.createOptional(new ResourceLocation(XEconomy.MOD_ID, "orbs"));
	public static final Tag.Named<Item> SLATES = ItemTags.createOptional(new ResourceLocation(XEconomy.MOD_ID, "slates"));
	public static final Tag.Named<Item> RUNES = ItemTags.createOptional(new ResourceLocation(XEconomy.MOD_ID, "runes"));
	
}
