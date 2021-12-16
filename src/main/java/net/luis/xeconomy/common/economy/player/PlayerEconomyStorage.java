package net.luis.xeconomy.common.economy.player;

import java.util.Random;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.luis.xeconomy.common.economy.player.bank.BankPlayerStorage;
import net.luis.xeconomy.common.economy.update.BankStorageUpdate;
import net.luis.xeconomy.common.economy.update.PlayerStorageUpdate;
import net.minecraft.server.level.ServerPlayer;

public class PlayerEconomyStorage {
	
	public static final Codec<PlayerEconomyStorage> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(BankPlayerStorage.CODEC.fieldOf("bankStorage").forGetter((playerStorage) -> {
			return playerStorage.bankStorage;
		}), Codec.INT.fieldOf("money").forGetter((playerStorage) -> {
			return playerStorage.money;
		}), Codec.DOUBLE.fieldOf("inflation").forGetter((playerStorage) -> {
			return playerStorage.inflation;
		})).apply(instance, PlayerEconomyStorage::new);
	});
	
	protected final BankPlayerStorage bankStorage;
	protected int money = 0;
	protected double inflation = 0.0;
	
	public PlayerEconomyStorage() {
		this(new BankPlayerStorage());
	}
	
	public PlayerEconomyStorage(BankPlayerStorage bankStorage) {
		this(bankStorage, 0, 0.0);
	}
	
	protected PlayerEconomyStorage(BankPlayerStorage bankStorage, int money, double inflation) {
		this.bankStorage = bankStorage;
		this.money = money;
		this.inflation = inflation;
	}
	
	public BankPlayerStorage getBankStorage() {
		return this.bankStorage;
	}
	
	public int getMoney() {
		return this.money;
	}
	
	public double getInflation() {
		return this.inflation;
	}
	
	// TODO: add Player Info
	public void update(ServerPlayer player, PlayerStorageUpdate storageUpdate, BankStorageUpdate bankUpdate) {
		Random rng = new Random(System.currentTimeMillis());
		double inflation = storageUpdate.inflation();
		this.bankStorage.update(player, bankUpdate);
		this.money += storageUpdate.money();
		if (this.money > 0) {
			this.money *= 1.0 + this.bankStorage.getInterest();
		}
		if (inflation != 0.0) {
			if (inflation > this.inflation * 1.03) {
				double d = (rng.nextInt(3) + 1) / 100;
				this.inflation *= 1.0 + d;
			} else if (this.inflation * 0.97 > inflation) {
				double d = (rng.nextInt(3) + 7) / 100;
				this.inflation *= 0.9 + d;
			} else {
				this.inflation = inflation;
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("PlayerEconomyStorage:{");
		builder.append("money=").append(this.money).append(", ");
		builder.append("inflation=").append(this.inflation).append(", ");
		builder.append("bankStorage=").append(this.bankStorage).append("}");
		return builder.toString();
	}
	
}
