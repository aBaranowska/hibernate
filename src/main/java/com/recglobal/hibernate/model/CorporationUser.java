package com.recglobal.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "corporation_user")
public class CorporationUser extends User {

	@Column(name = "company_name", length = 20, nullable = false, unique = true)
	private String companyName;

	public CorporationUser() {

	}

	public CorporationUser(String name) {
		this.companyName = name;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String name) {
		this.companyName = name;
	}

}
