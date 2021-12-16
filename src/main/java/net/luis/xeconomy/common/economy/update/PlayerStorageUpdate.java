package net.luis.xeconomy.common.economy.update;

public record PlayerStorageUpdate(int money, double inflation) {
	
	public static final PlayerStorageUpdate NO_UPDATE = new PlayerStorageUpdate(0, 0);
	
}
