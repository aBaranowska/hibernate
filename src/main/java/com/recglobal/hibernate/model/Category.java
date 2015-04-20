package com.recglobal.hibernate.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "category")
public class Category {

	@Id
	@GeneratedValue
	@Column(name = "category_id")
	private Integer id;

	@Column(name = "name", length = 30, nullable = false, unique = true)
	private String name;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pk.category", orphanRemoval = true)
	private Set<ProductCategory> products;

	public Category() {

	}

	/**
	 * Create category<br>
	 * Required unique name<br>
	 * Category has zero or many products<br>
	 * Product in category must have unique name
	 */
	public Category(String name, Product... products) {
		this.name = name;
		this.products = new HashSet<ProductCategory>();
		for (Product product : products) {
			ProductCategoryId productCategoryId = new ProductCategoryId(product, this);
			ProductCategory productCategory = new ProductCategory(productCategoryId, product.getName(), name);
			this.products.add(productCategory);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ProductCategory> getProducts() {
		return products;
	}

	public void setProducts(Set<ProductCategory> products) {
		this.products = products;
	}

	public Product getProduct(int index) {
		Iterator<ProductCategory> it = products.iterator();
		int i = 0;
		while (it.hasNext()) {
			ProductCategory productCategory = it.next();
			if (i == index) {
				return productCategory.getPk().getProduct();
			}
			i++;
		}
		return null;
	}

	public void setProducts(Product... products) {
		List<Product> productList = new ArrayList<Product>();
		List<Integer> idList = new ArrayList<Integer>();
		for (Product product : products) {
			productList.add(product);
			idList.add(product.getId());
		}

		List<Product> productToRemove = new ArrayList<Product>();
		List<ProductCategory> productCategoryToRemove = new ArrayList<ProductCategory>();
		for (ProductCategory productCategory : this.products) {
			Product product = productCategory.getPk().getProduct();
			if (idList.contains(product.getId())) {
				int index = idList.indexOf(product.getId());
				productToRemove.add(productList.get(index));
			} else {
				productCategoryToRemove.add(productCategory);
			}
		}

		productList.removeAll(productToRemove);
		this.products.removeAll(productCategoryToRemove);

		for (Product product : productList) {
			ProductCategoryId productCategoryId = new ProductCategoryId(product, this);
			ProductCategory productCategory = new ProductCategory(productCategoryId, product.getName(), name);
			this.products.add(productCategory);
		}
	}

}
