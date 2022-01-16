package net.luis.xeconomy.common.economy.bank;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.commons.compress.utils.Lists;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.luis.xeconomy.common.economy.player.EconomyPlayer;

public class PlayerBankStorage {
	
	public static final Codec<PlayerBankStorage> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(EconomyPlayer.CODEC.fieldOf("economyPlayer").forGetter((bankStorage) -> {
			return bankStorage.economyPlayer;
		}), Codec.INT.fieldOf("money").forGetter((bankStorage) -> {
			return bankStorage.money;
		}), Codec.DOUBLE.fieldOf("interest").forGetter((bankStorage) -> {
			return bankStorage.interest;
		}), Codec.list(BankCredit.CODEC).fieldOf("bankCredits").forGetter((bankStorage) -> {
			return bankStorage.bankCredits;
		})).apply(instance, PlayerBankStorage::new);
	});
	
	protected final EconomyPlayer economyPlayer;
	protected int money = 0;
	protected double interest = 0.0;
	protected final List<BankCredit> bankCredits;
	
	public PlayerBankStorage(EconomyPlayer economyPlayer, int money) {
		this(economyPlayer, money, 0.0);
	}
	
	public PlayerBankStorage(EconomyPlayer economyPlayer, int money, double interest) {
		this(economyPlayer, money, interest, Lists.newArrayList());
	}
	
	protected PlayerBankStorage(EconomyPlayer economyPlayer, int money, double interest, List<BankCredit> bankCredits) {
		this.economyPlayer = economyPlayer;
		this.bankCredits = bankCredits;
		this.money = money;
		this.interest = interest;
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
	
	public boolean hasMoney() {
		return this.money > 0;
	}
	
	public double getInterest() {
		return this.interest;
	}
	
	public void setInterest(double interest) {
		this.interest = interest;
	}
	
	public List<BankCredit> getBankCredits() {
		return this.bankCredits;
	}
	
	public void setBankCredits(List<BankCredit> bankCredits) {
		this.bankCredits.clear();
		this.bankCredits.addAll(bankCredits);
	}
	
	public void addBankCredit(BankCredit bankCredit) {
		if (this.getBankCreditIDs().contains(bankCredit.getId())) {
			throw new IllegalArgumentException("A BankCredit with ID " + bankCredit.getId() + ", already exists for player " + this.economyPlayer.getName());
		}
		this.bankCredits.add(bankCredit);
	}
	
	public List<Integer> getBankCreditIDs() {
		return this.bankCredits.stream().map(BankCredit::getId).collect(Collectors.toList());
	}
	
	@Nullable
	public BankCredit getBankCredit(int id) {
		for (BankCredit bankCredit : this.bankCredits) {
			if (bankCredit.getId() == id) {
				return bankCredit;
			}
		}
		return null;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		} else if (object instanceof PlayerBankStorage playerBankStorage) {
			if (this.economyPlayer.equals(playerBankStorage.economyPlayer) && this.money == playerBankStorage.money && this.interest == playerBankStorage.interest) {
				return this.bankCredits.equals(playerBankStorage.bankCredits);
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("BankPlayerStorage:{");
		builder.append("economyPlayer=").append(this.economyPlayer).append(", ");
		builder.append("money=").append(this.money).append(", ");
		builder.append("interest=").append(this.interest).append(", ");
		builder.append("bankCredits=").append(this.bankCredits).append("}");
		return builder.toString();
	}
	
}
