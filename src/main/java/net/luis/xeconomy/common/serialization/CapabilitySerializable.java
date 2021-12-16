package net.luis.xeconomy.common.serialization;

import net.minecraft.nbt.CompoundTag;

public interface CapabilitySerializable {
	
	CompoundTag serialize();

	void deserialize(CompoundTag tag);
	
}
