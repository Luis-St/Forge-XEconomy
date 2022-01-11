package net.luis.xeconomy.common.economy.bank;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class BankCredit {
	
	public static final Codec<BankCredit> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Codec.INT.fieldOf("id").forGetter((bankCredit) -> {
			return bankCredit.id;
		}), Codec.INT.fieldOf("money").forGetter((bankCredit) -> {
			return bankCredit.money;
		}), Codec.DOUBLE.fieldOf("interest").forGetter((bankCredit) -> {
			return bankCredit.interest;
		}), Codec.INT.fieldOf("repaidMoney").forGetter((bankCredit) -> {
			return bankCredit.repaidMoney;
		})).apply(instance, BankCredit::new);
	});
	
	protected final int id;
	protected final int money;
	protected final double interest;
	protected int repaidMoney;
	
	public BankCredit(int id, int money, double interest) {
		this(id, money, interest, 0);
	}
	
	protected BankCredit(int id, int money, double interest, int repaidMoney) {
		this.id = id;
		this.money = money;
		this.interest = interest;
		this.repaidMoney = repaidMoney;
		if (0 > this.id) {
			throw new IllegalArgumentException("The ID can't be negativ, but it is " + this.id);
		}
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getMoney() {
		return this.money;
	}
	
	public double getInterest() {
		return this.interest;
	}
	
	public int getMoneyForRepaid() {
		return (int) Math.round(this.money * (1.0 + this.interest));
	}
	
	public int getRepaidMoney() {
		return this.repaidMoney;
	}
	
	public void setRepaidMoney(int repaidMoney) {
		this.repaidMoney = repaidMoney;
	}
	
	public boolean isRepaid() {
		return this.repaidMoney >= this.getMoneyForRepaid();
	}
	
	public int getMissingRepaidMoney() {
		if (this.isRepaid()) {
			return 0;
		}
		return this.getMoneyForRepaid() - this.repaidMoney;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		} else if (object instanceof BankCredit bankCredit) {
			if (this.id == bankCredit.id && this.money == bankCredit.money && this.interest == bankCredit.interest) {
				return this.repaidMoney == bankCredit.repaidMoney;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("BankCredit:{");
		builder.append("id=").append(this.id).append(", ");
		builder.append("money=").append(this.money).append(", ");
		builder.append("interest=").append(this.interest).append(", ");
		builder.append("repaidMoney=").append(this.repaidMoney).append("}");
		return builder.toString();
	}
	
}
