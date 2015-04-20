package com.recglobal.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "card_payment")
public class CardPayment extends Payment {

	@Column(name = "card_type", length = 15, nullable = false)
	private String cardType;

	public CardPayment() {

	}

	public CardPayment(String cardType) {
		this.cardType = cardType;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

}
