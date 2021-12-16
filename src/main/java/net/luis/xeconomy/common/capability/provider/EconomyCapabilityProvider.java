package net.luis.xeconomy.common.capability.provider;

import java.io.IOException;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.luis.xeconomy.XEconomy;
import net.luis.xeconomy.common.capability.handler.EconomyCapabilityHandler;
import net.luis.xeconomy.common.capability.interfaces.IEconomyCapability;
import net.luis.xeconomy.init.capability.ModCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class EconomyCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
	
	protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
	
	protected EconomyCapabilityHandler handler = new EconomyCapabilityHandler();
	protected final LazyOptional<IEconomyCapability> optional = LazyOptional.of(() -> this.handler);
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return cap == ModCapabilities.ECONOMY ? (LazyOptional<T>) this.optional : LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		this.handler.saveAsJson(XEconomy.getInstance().getEconomyPath());
		return new CompoundTag();
	}
	
	@Override
	public void deserializeNBT(CompoundTag tag) {
		try {
			this.handler.loadAsJson(XEconomy.getInstance().getEconomyPath());
		} catch (RuntimeException e) {
			XEconomy.LOGGER.warn("Fail to load Economy from {}", XEconomy.getInstance().getEconomyPath());
			XEconomy.LOGGER.warn("Due to errors in the economy_{}.json File, the File will be replaced by a new one", XEconomy.getInstance().getWorldName());
			try {
				Files.delete(XEconomy.getInstance().getEconomyPath());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
}
