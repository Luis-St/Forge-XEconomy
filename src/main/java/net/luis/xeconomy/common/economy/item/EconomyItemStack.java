package net.luis.xeconomy.common.economy.item;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EconomyItemStack {
	
	protected final Item item;
	protected final int count;
	
	public EconomyItemStack(Item item, int count) {
		this.item = item;
		this.count = count;
	}
	
	public Item getItem() {
		return this.item;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public List<ItemStack> asItemStacks() {
		if (64 >= this.count) {
			return Lists.newArrayList(new ItemStack(this.item, this.count));
		}
		List<ItemStack> itemStacks = Lists.newArrayList();
		int count = this.count;
		while (count > 0) {
			if (count > 64) {
				itemStacks.add(new ItemStack(this.item, 64));
				count -=64;
			} else {
				itemStacks.add(new ItemStack(this.item, count));
				count = 0;
			}
		}
		return itemStacks;
	}
	
}
