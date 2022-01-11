package net.luis.xeconomy.common.economy.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class PlayerEconomyStorage {
	
	public static final Codec<PlayerEconomyStorage> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(EconomyPlayer.CODEC.fieldOf("economyPlayer").forGetter((playerStorage) -> {
			return playerStorage.economyPlayer;
		}), Codec.INT.fieldOf("money").forGetter((playerStorage) -> {
			return playerStorage.money;
		}), Codec.DOUBLE.fieldOf("inflation").forGetter((playerStorage) -> {
			return playerStorage.inflation;
		})).apply(instance, PlayerEconomyStorage::new);
	});
	
	protected final EconomyPlayer economyPlayer;
	protected int money = 0;
	protected double inflation = 0.0;
	
	protected PlayerEconomyStorage(EconomyPlayer economyPlayer, int money, double inflation) {
		this.economyPlayer = economyPlayer;
		this.money = money;
		this.inflation = inflation;
	}
	
	public EconomyPlayer getEconomyPlayer() {
		return this.economyPlayer;
	}
	
	public int getMoney() {
		return this.money;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}
	
	public double getInflation() {
		return this.inflation;
	}
	
	public void setInflation(double inflation) {
		this.inflation = inflation;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		} else if (object instanceof PlayerEconomyStorage playerEconomyStorage) {
			if (this.economyPlayer.equals(playerEconomyStorage.economyPlayer) && this.money == playerEconomyStorage.money) {
				return this.inflation == playerEconomyStorage.inflation;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("PlayerEconomyStorage:{");
		builder.append("economyPlayer=").append(this.economyPlayer).append(", ");
		builder.append("money=").append(this.money).append(", ");
		builder.append("inflation=").append(this.inflation).append("}");
		return builder.toString();
	}
	
}
