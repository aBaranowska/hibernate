package com.recglobal.hibernate.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "product_order")
public class ProductOrder extends Product {

	@Column(name = "quantity")
	private Integer quantity;

	@OneToOne
	@Cascade(value = { CascadeType.SAVE_UPDATE })
	@JoinColumn(name = "fk_product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "fk_order_id")
	private Order order;

	public ProductOrder() {

	}

	/**
	 * Create product order<br>
	 * Copy name and price from product<br>
	 * No category (otherwise row in join table will be created)
	 */
	public ProductOrder(Product product, Integer quantity) {
		name = product.getName();
		price = product.getPrice();
		categories = null;
		this.quantity = quantity;
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public Set<ProductCategory> getCategories() {
		return product.getCategories();
	}

	@Override
	public Category getCategory(int index) {
		return product.getCategory(index);
	}

}
