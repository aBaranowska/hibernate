package com.recglobal.hibernate.rest.json;

public class JsonAddress {

	private String city;

	public JsonAddress() {

	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "[city=" + city + "]";
	}

}
