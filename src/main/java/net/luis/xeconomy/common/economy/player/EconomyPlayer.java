package net.luis.xeconomy.common.economy;

import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.luis.xeconomy.common.serialization.Codecs;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;

public class EconomyPlayer {
	
	public static final Codec<EconomyPlayer> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Codec.STRING.fieldOf("name").forGetter((economyPlayer) -> {
			return economyPlayer.name;
		}), Codecs.UUID.fieldOf("uuid").forGetter((economyPlayer) -> {
			return economyPlayer.uuid;
		})).apply(instance, EconomyPlayer::new);
	});
	
	protected final String name;
	protected final UUID uuid;
	
	public EconomyPlayer(String name, UUID uuid) {
		this.name = name;
		this.uuid = uuid;
	}
	
	public String getName() {
		return this.name;
	}
	
	public UUID getUuid() {
		return this.uuid;
	}
	
	@Nullable
	public ServerPlayer asPlayer() {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server != null) {
			return server.getPlayerList().getPlayer(this.uuid);
		}
		return null;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		} else if (object instanceof EconomyPlayer economyPlayer) {
			if (this.name.equals(economyPlayer.name)) {
				return this.uuid.equals(economyPlayer.uuid);
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("EconomyPlayer:{");
		builder.append("name=").append(this.name).append(", ");
		builder.append("uuid=").append(this.uuid).append("}");
		return builder.toString();
	}
	
}
