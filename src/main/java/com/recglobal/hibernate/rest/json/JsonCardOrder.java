package com.recglobal.hibernate.rest.json;

public class JsonCardOrder extends JsonOrder {

	private String card;

	public JsonCardOrder() {

	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	@Override
	public String toString() {
		return "[card=" + card + ", id=" + id + ", createDate=" + createDate + ", userId=" + userId + ", products="
				+ products + ", orderStatus=" + orderStatus + ", paymentStatus=" + paymentStatus + ", amount=" + amount
				+ "]";
	}

}
