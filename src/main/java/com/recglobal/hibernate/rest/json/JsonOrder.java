package com.recglobal.hibernate.rest.json;

import java.util.Date;
import java.util.List;

public class JsonOrder {

	protected Integer id;
	protected Date createDate;
	protected Integer userId;
	protected List<JsonProductOrder> products;
	protected String orderStatus;
	protected String paymentStatus;
	protected Double amount;

	public JsonOrder() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<JsonProductOrder> getProducts() {
		return products;
	}

	public void setProducts(List<JsonProductOrder> products) {
		this.products = products;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", createDate=" + createDate + ", userId=" + userId + ", products=" + products
				+ ", orderStatus=" + orderStatus + ", paymentStatus=" + paymentStatus + ", amount=" + amount + "]";
	}

}
