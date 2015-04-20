package com.recglobal.hibernate.rest.json;

import java.util.List;

public class JsonProduct {

	protected Integer id;
	protected String name;
	protected Double price;
	protected List<Integer> categoryIds;

	public JsonProduct() {

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

	public List<Integer> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Integer> categoryIds) {
		this.categoryIds = categoryIds;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", name=" + name + ", price=" + price + ", categoryIds=" + categoryIds + "]";
	}

}
