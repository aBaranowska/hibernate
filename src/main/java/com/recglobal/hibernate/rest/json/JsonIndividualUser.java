package com.recglobal.hibernate.rest.json;

public class JsonIndividualUser extends JsonUser {

	private String firstName;
	private String lastName;

	public JsonIndividualUser() {

	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "[firstName=" + firstName + ", lastName=" + lastName + ", id=" + id + ", email=" + email + ", address="
				+ address + "]";
	}

}
