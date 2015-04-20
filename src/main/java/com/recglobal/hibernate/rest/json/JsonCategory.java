package com.recglobal.hibernate.rest.json;

public class JsonCategory {

	private Integer id;
	private String name;

	public JsonCategory() {

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

	@Override
	public String toString() {
		return "[id=" + id + ", name=" + name + "]";
	}

}
