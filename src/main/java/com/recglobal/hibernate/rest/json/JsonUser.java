package com.recglobal.hibernate.rest.json;

public class JsonUser {

	protected Integer id;
	protected String email;
	protected JsonAddress address;

	public JsonUser() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public JsonAddress getAddress() {
		return address;
	}

	public void setAddress(JsonAddress address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", email=" + email + ", address=" + address + "]";
	}

}
