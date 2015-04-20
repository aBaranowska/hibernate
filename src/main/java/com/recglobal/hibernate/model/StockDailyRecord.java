package com.recglobal.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Class created only to show support for polymorphic relationships
 */
@Entity
@Table(name = "stock_daily_record")
public class StockDailyRecord {

	@Id
	@GeneratedValue
	@Column(name = "record_id")
	private Integer recordId;

	@ManyToOne
	@JoinColumn(name = "fk_stock_id", nullable = false)
	private Stock stock;

	public StockDailyRecord() {

	}

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

}
