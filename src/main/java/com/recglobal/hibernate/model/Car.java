package com.recglobal.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * Class created only to show table generator
 */
@Entity
@Table(name = "car")
public class Car {

	@Id
	@TableGenerator(name = "car_generator", table = "generated_keys", pkColumnName = "pk_column", valueColumnName = "value_column", pkColumnValue = "car_id", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "car_generator")
	@Column(name = "car_id")
	private Integer cardId;

	public Car() {

	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

}
