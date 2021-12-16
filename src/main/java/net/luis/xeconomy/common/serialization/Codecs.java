package net.luis.xeconomy.common.serialization;

import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.luis.xeconomy.common.economy.ItemEconomy;
import net.luis.xeconomy.common.economy.player.PlayerEconomyStorage;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;

@SuppressWarnings("deprecation")
public class Codecs {
	
	public static final Codec<Item> ITEM = Registry.ITEM.byNameCodec();
	public static final Codec<Map<Item, ItemEconomy>> ECONOMIES = Codec.unboundedMap(Codecs.ITEM, ItemEconomy.CODEC);
	public static final Codec<java.util.UUID> UUID = Codec.STRING.comapFlatMap(Codecs::readUUID, java.util.UUID::toString);
	public static final Codec<Map<java.util.UUID, PlayerEconomyStorage>> ECONOMY_STORAGES = Codec.unboundedMap(Codecs.UUID, PlayerEconomyStorage.CODEC);
	
	protected static DataResult<java.util.UUID> readUUID(String uuid) {
		try {
			return DataResult.success(java.util.UUID.fromString(uuid));
		} catch (IllegalArgumentException e) {
			return DataResult.error("Fail to create UUID from String " + uuid + ": " + e.getMessage());
		}
	}
	
}
