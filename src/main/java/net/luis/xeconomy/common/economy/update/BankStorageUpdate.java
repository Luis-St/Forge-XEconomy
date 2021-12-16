package net.luis.xeconomy.common.economy.update;

public record BankStorageUpdate(int money, int id, int repaidMoney) {
	
	public static final BankStorageUpdate NO_UPDATE = new BankStorageUpdate(0, -1, 0);
	
}
