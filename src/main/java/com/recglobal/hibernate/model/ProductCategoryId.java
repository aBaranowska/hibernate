package com.recglobal.hibernate.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

// Composite primary key must implement equals() and hashcode() and be serializable
@Embeddable
public class ProductCategoryId implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Product product;

	@ManyToOne
	private Category category;

	public ProductCategoryId() {

	}

	public ProductCategoryId(Product product, Category category) {
		this.product = product;
		this.category = category;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (product != null ? product.hashCode() : 0);
		result = prime * result + (category != null ? category.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		ProductCategoryId other = (ProductCategoryId) obj;
		if (product == null) {
			if (other.product != null) {
				return false;
			}
		} else if (!product.equals(other.product)) {
			return false;
		}
		if (category == null) {
			if (other.category != null) {
				return false;
			}
		} else if (!category.equals(other.category)) {
			return false;
		}
		return true;
	}

}
