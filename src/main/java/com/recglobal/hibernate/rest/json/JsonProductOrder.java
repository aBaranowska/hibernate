package com.recglobal.hibernate.rest.json;

public class JsonProductOrder extends JsonProduct {

	private Integer quantity;
	/**
	 * productId has different meaning in post and get request<br>
	 * in post request means Product.id<br>
	 * in get request means ProductOrder.product.id<br>
	 * in /orders/{orderId}/add means Product.id<br>
	 * in /orders/{orderId}/delete means ProductOrder.id
	 */
	private Integer productId;

	public JsonProductOrder() {

	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	@Override
	public String toString() {
		return "[quantity=" + quantity + ", productId=" + productId + "]";
	}

}
