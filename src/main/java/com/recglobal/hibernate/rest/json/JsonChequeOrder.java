package com.recglobal.hibernate.rest.json;

public class JsonChequeOrder extends JsonOrder {

	private String bank;

	public JsonChequeOrder() {

	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	@Override
	public String toString() {
		return "[bank=" + bank + ", id=" + id + ", createDate=" + createDate + ", userId=" + userId + ", products="
				+ products + ", orderStatus=" + orderStatus + ", paymentStatus=" + paymentStatus + ", amount=" + amount
				+ "]";
	}

}
