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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "product")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Product {

	@Id
	@GeneratedValue
	@Column(name = "product_id")
	protected Integer id;

	@Column(name = "name", length = 30, nullable = false)
	protected String name;

	@Column(name = "price", nullable = false)
	protected Double price;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pk.product", orphanRemoval = true)
	protected Set<ProductCategory> categories;

	public Product() {

	}

	/**
	 * Create product<br>
	 * Required name and price<br>
	 * Product belongs to many categories (no category is also possible)
	 */
	public Product(String name, Double price, Category... categories) {
		this.name = name;
		this.price = price;
		this.categories = new HashSet<ProductCategory>();
		for (Category category : categories) {
			ProductCategoryId productCategoryId = new ProductCategoryId(this, category);
			ProductCategory productCategory = new ProductCategory(productCategoryId, name, category.getName());
			this.categories.add(productCategory);
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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Set<ProductCategory> getCategories() {
		return categories;
	}

	public void setCategories(Set<ProductCategory> categories) {
		this.categories = categories;
	}

	public Category getCategory(int index) {
		Iterator<ProductCategory> it = categories.iterator();
		int i = 0;
		while (it.hasNext()) {
			ProductCategory productCategory = it.next();
			if (i == index) {
				return productCategory.getPk().getCategory();
			}
			i++;
		}
		return null;
	}

	public void setCategories(Category... categories) {
		List<Category> categoryList = new ArrayList<Category>();
		List<Integer> idList = new ArrayList<Integer>();
		for (Category category : categories) {
			categoryList.add(category);
			idList.add(category.getId());
		}

		List<Category> categoryToRemove = new ArrayList<Category>();
		List<ProductCategory> productCategoryToRemove = new ArrayList<ProductCategory>();
		for (ProductCategory productCategory : this.categories) {
			Category category = productCategory.getPk().getCategory();
			if (idList.contains(category.getId())) {
				int index = idList.indexOf(category.getId());
				categoryToRemove.add(categoryList.get(index));
			} else {
				productCategoryToRemove.add(productCategory);
			}
		}

		categoryList.removeAll(categoryToRemove);
		this.categories.removeAll(productCategoryToRemove);

		for (Category category : categoryList) {
			ProductCategoryId productCategoryId = new ProductCategoryId(this, category);
			ProductCategory productCategory = new ProductCategory(productCategoryId, name, category.getName());
			this.categories.add(productCategory);
		}
	}

}
