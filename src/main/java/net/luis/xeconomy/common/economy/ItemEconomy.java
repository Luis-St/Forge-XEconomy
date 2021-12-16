package net.luis.xeconomy.common.economy;

import java.util.Random;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.item.Item;

public class ItemEconomy {
	
	public static final Codec<ItemEconomy> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Codec.INT.fieldOf("price").forGetter((itemEconomy) -> {
			return itemEconomy.price;
		}), Codec.INT.fieldOf("minPrice").forGetter((itemEconomy) -> {
			return itemEconomy.minPrice;
		}), Codec.INT.fieldOf("maxPrice").forGetter((itemEconomy) -> {
			return itemEconomy.maxPrice;
		}), Codec.INT.fieldOf("count").forGetter((itemEconomy) -> {
			return itemEconomy.count;
		}),  Codec.DOUBLE.fieldOf("inflation").forGetter((itemEconomy) -> {
			return itemEconomy.inflation;
		})).apply(instance, ItemEconomy::new);
	});
	
	protected final int price;
	protected int minPrice = 0;
	protected int maxPrice = Integer.MAX_VALUE;
	protected int count = 0;
	protected double inflation = 0.0;
	
	public ItemEconomy(int price) {
		this(price, 0, Integer.MAX_VALUE);
	}
	
	public ItemEconomy(int price, int minPrice, int maxPrice) {
		this(price, minPrice, maxPrice, 0.0);
	}
	
	public ItemEconomy(int price, int minPrice, int maxPrice, double inflation) {
		this(price, minPrice, maxPrice, 0, inflation);
	}
	
	protected ItemEconomy(int price, int minPrice, int maxPrice, int count, double inflation) {
		this.price = price;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.inflation = inflation;
	}
	
	public int getPrice() {
		return this.price;
	}
	
	public int getMinPrice() {
		return this.minPrice;
	}
	
	public int getMaxPrice() {
		return this.maxPrice;
	}
	
	public int getCount() {
		return this.count;
	}

	/* -- Edit: use extra sell and buy -- */
	
	// TODO: test and modify
	protected int calculatePrice(Item item, double playerInflation) {
		int priceAdditional = (int) Math.round(-(Math.pow(this.count, 3) / Math.pow(2, 15) * 0.003));
		int price = (int) Math.round((this.price * (1.0 + this.inflation)) * (1.0 + playerInflation));
		return Math.max(Math.min(price + priceAdditional, this.maxPrice), this.minPrice);
	}
	
	public int getPrice(ItemStorage itemStorage, double playerInflation) {
		return this.calculatePrice(itemStorage.item(), playerInflation) * itemStorage.count();
	}
	
	/* -- Edit: use extra sell and buy -- */
	
	public double getInflation() {
		return this.inflation;
	}
	
	// TODO: log info
	public void update(Item item, double inflation) {
		Random rng = new Random(System.currentTimeMillis());
		if (inflation > this.inflation * 1.05) {
			double d = (rng.nextInt(5) + 1) / 100;
			this.inflation *= 1.0 + d;
		} else if (this.inflation * 0.95 > inflation) {
			double d = (rng.nextInt(5) + 5) / 100;
			this.inflation *= 0.9 + d;
		} else {
			this.inflation = inflation;
		}
		
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("ItemEconomy:{");
		builder.append("price=").append(this.price).append(", ");
		builder.append("minPrice=").append(this.minPrice).append(", ");
		builder.append("maxPrice=").append(this.maxPrice).append(", ");
		builder.append("inflation=").append(this.inflation).append("}");
		return builder.toString(); 
	}
	
}
