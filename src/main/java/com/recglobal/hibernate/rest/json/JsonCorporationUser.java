package com.recglobal.hibernate.rest.json;

public class JsonCorporationUser extends JsonUser {

	private String companyName;

	public JsonCorporationUser() {

	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public String toString() {
		return "[companyName=" + companyName + ", id=" + id + ", email=" + email + ", address=" + address + "]";
	}

}
