package com.recglobal.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cheque_payment")
public class ChequePayment extends Payment {

	@Column(name = "bank_name", length = 15, nullable = false)
	private String bankName;

	public ChequePayment() {

	}

	public ChequePayment(String bankName) {
		this.bankName = bankName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

}
