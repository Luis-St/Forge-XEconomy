package net.luis.xeconomy.common.economy.player.bank;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.commons.compress.utils.Lists;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.luis.xeconomy.common.economy.update.BankStorageUpdate;
import net.minecraft.server.level.ServerPlayer;

public class BankPlayerStorage {
	
	public static final Codec<BankPlayerStorage> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Codec.INT.fieldOf("money").forGetter((bankStorage) -> {
			return bankStorage.money;
		}), Codec.DOUBLE.fieldOf("interest").forGetter((bankStorage) -> {
			return bankStorage.interest;
		}), Codec.list(BankCredit.CODEC).fieldOf("bankCredits").forGetter((bankStorage) -> {
			return bankStorage.bankCredits;
		})).apply(instance, BankPlayerStorage::new);
	});
	
	protected final List<BankCredit> bankCredits;
	protected int money = 0;
	protected double interest = 0.0;
	
	public BankPlayerStorage() {
		this(0, 0.05);
	}
	
	public BankPlayerStorage(int money, double interest) {
		this(money, interest, Lists.newArrayList());
	}
	
	protected BankPlayerStorage(int money, double interest, List<BankCredit> bankCredits) {
		this.bankCredits = bankCredits;
		this.money = money;
		this.interest = interest;
	}
	
	public void addBankCredit(ServerPlayer player, BankCredit bankCredit) {
		if (this.getBankCreditIDs().contains(bankCredit.getId())) {
			throw new IllegalArgumentException("A BankCredit with the ID " + bankCredit.getId() + ", already exists for player " + player.getName().getString());
		}
		this.bankCredits.add(bankCredit);
	}
	
	public List<BankCredit> getBankCredits() {
		return this.bankCredits;
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
	
	public List<Integer> getBankCreditIDs() {
		return this.bankCredits.stream().map(BankCredit::getId).collect(Collectors.toList());
	}
	
	public int getMoney() {
		return this.money;
	}
	
	public boolean hasMoney() {
		return this.money > 0;
	}
	
	public double getInterest() {
		return this.interest;
	}
	
	// TODO: add Player Info
	public void update(ServerPlayer player, BankStorageUpdate bankUpdate) {
		this.money += bankUpdate.money();
		if (0 > this.money) {
			this.interest -= this.interest * 0.01;
		} else {
			this.money *= 1.0 + this.interest;
		}
		this.bankCredits.forEach(BankCredit::defaultUpdate);
		BankCredit bankCredit = this.getBankCredit(bankUpdate.id());
		if (bankCredit != null) {
			bankCredit.update(player, bankUpdate.repaidMoney());
		}
		this.bankCredits.removeIf(credit -> {
			return credit.canRemove(player);
		});
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("BankPlayerStorage:{");
		builder.append("money=").append(this.money).append(", ");
		builder.append("interest=").append(this.interest).append(", ");
		builder.append("bankCredits=").append(this.bankCredits).append("}");
		return builder.toString();
	}
	
}
