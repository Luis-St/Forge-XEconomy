package net.luis.xeconomy.common.economy.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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
	
	public void setMinPrice(int minPrice) {
		this.minPrice = minPrice;
	}
	
	public int getMaxPrice() {
		return this.maxPrice;
	}
	
	public void setMaxPrice(int maxPrice) {
		this.maxPrice = maxPrice;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}

	
	public double getInflation() {
		return this.inflation;
	}
	
	public void setInflation(double inflation) {
		this.inflation = inflation;
	}
	
	// TODO: add getPrice method for buy and sell
	
	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		} else if (object instanceof ItemEconomy itemEconomy) {
			if (this.price == itemEconomy.price && this.minPrice == itemEconomy.minPrice && this.maxPrice == itemEconomy.maxPrice) {
				return this.inflation == itemEconomy.inflation;
			}
		}
		return false;
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
