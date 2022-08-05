package net.luis.xeconomy.common.economy.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.luis.xeconomy.common.economy.bank.PlayerBankStorage;

public record PlayerStorage(PlayerEconomyStorage economyStorage, PlayerBankStorage bankStorage) {
	
	public static final Codec<PlayerStorage> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(PlayerEconomyStorage.CODEC.fieldOf("economyStorage").forGetter((playerStorage) -> {
			return playerStorage.economyStorage;
		}), PlayerBankStorage.CODEC.fieldOf("bankStorage").forGetter((playerStorage) -> {
			return playerStorage.bankStorage;
		})).apply(instance, PlayerStorage::new);
	}); 
	
}
