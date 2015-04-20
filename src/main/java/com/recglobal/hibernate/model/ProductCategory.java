package com.recglobal.hibernate.model;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "product_category", uniqueConstraints = @UniqueConstraint(columnNames = { "product_name", "category_name" }))
@AssociationOverrides({ @AssociationOverride(name = "pk.product", joinColumns = @JoinColumn(name = "product_id")),
		@AssociationOverride(name = "pk.category", joinColumns = @JoinColumn(name = "category_id")) })
public class ProductCategory {

	@EmbeddedId
	private ProductCategoryId pk;

	@Column(name = "product_name", length = 30, nullable = false)
	private String productName;

	@Column(name = "category_name", length = 30, nullable = false)
	private String categoryName;

	public ProductCategory() {

	}

	public ProductCategory(ProductCategoryId pk, String productName, String categoryName) {
		this.pk = pk;
		this.productName = productName;
		this.categoryName = categoryName;
	}

	public ProductCategoryId getPk() {
		return pk;
	}

	public void setPk(ProductCategoryId pk) {
		this.pk = pk;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
