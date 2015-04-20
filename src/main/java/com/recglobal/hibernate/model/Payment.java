package com.recglobal.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "payment")
@Inheritance(strategy = InheritanceType.JOINED)
public class Payment {

	@Id
	@GeneratedValue
	@Column(name = "payment_id")
	protected Integer id;

	@Column(name = "status", length = 15, nullable = false)
	@Enumerated(EnumType.STRING)
	protected PaymentStatus status;

	@OneToOne(mappedBy = "payment")
	protected Order order;

	public Payment() {
		status = PaymentStatus.UNKNOWN;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}
