package net.luis.xeconomy.common.economy.player.bank;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

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
	
	public int getRepaidMoney() {
		return this.repaidMoney;
	}
	
	public boolean isRepaid() {
		return this.repaidMoney >= (this.money * (1.0 + this.interest));
	}
	
	public boolean canRemove(ServerPlayer player) {
		if (this.isRepaid()) {
			player.sendMessage(new TextComponent("[Bank Credit]").withStyle(ChatFormatting.GREEN).append(":")
					.append(new TextComponent("Remove Bank Credi with ID " + this.id + ", since it's repaid").withStyle(ChatFormatting.GRAY)), player.getUUID()); // TODO: use option to disable Feedback 
			return true;
		}
		return false;
	}
	
	public void defaultUpdate() {
		this.repaidMoney += (this.money * (1.0 + this.interest)) * 0.01;
	}
	
	public void update(ServerPlayer player, int repaidMoney) {
		this.repaidMoney += repaidMoney;
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
