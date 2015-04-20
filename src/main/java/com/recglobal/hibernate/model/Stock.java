package com.recglobal.hibernate.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Class created only to show support for polymorphic relationships
 */
@Entity
@Table(name = "stock")
// TABLE_PER_CLASS provides poor support for polymorphic relationships
// JOINED and SINGLE_TABLE - good
@Inheritance(strategy = InheritanceType.JOINED)
public class Stock {

	@Id
	// TABLE_PER_CLASS would require TableGenerator
	// JOINED and SINGLE_TABLE - not
	@GeneratedValue
	@Column(name = "stock_id")
	private Integer stockId;

	@OneToMany(mappedBy = "stock")
	private Set<StockDailyRecord> stockDailyRecords;

	public Stock() {

	}

	public Integer getStockId() {
		return stockId;
	}

	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	public Set<StockDailyRecord> getStockDailyRecords() {
		return stockDailyRecords;
	}

	public void setStockDailyRecords(Set<StockDailyRecord> stockDailyRecords) {
		this.stockDailyRecords = stockDailyRecords;
	}

}
