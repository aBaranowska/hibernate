package com.recglobal.hibernate.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.recglobal.hibernate.model.Address;
import com.recglobal.hibernate.model.CorporationUser;
import com.recglobal.hibernate.model.IndividualUser;
import com.recglobal.hibernate.rest.json.JsonAddress;
import com.recglobal.hibernate.rest.json.JsonCorporationUser;
import com.recglobal.hibernate.rest.json.JsonIndividualUser;

@Path("/users")
public class UserService extends BaseService {

	@POST
	@Path("/individual")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addIndividualUser(JsonIndividualUser jsonUser) {
		IndividualUser user = new IndividualUser(jsonUser.getFirstName(), jsonUser.getLastName());
		user.setEmail(jsonUser.getEmail());
		if (jsonUser.getAddress() != null) {
			user.setAddress(new Address(jsonUser.getAddress().getCity()));
		}

		if (userDao.saveOrUpdate(user)) {
			int status = STATUS_OK;
			String result = "Saved user: " + jsonUser.toString();
			return Response.status(status).entity(result).build();
		}

		int status = STATUS_ERROR;
		String result = "Can't save user: " + jsonUser.toString();
		return Response.status(status).entity(result).build();
	}

	@POST
	@Path("/corporation")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addCorporationUser(JsonCorporationUser jsonUser) {
		CorporationUser user = new CorporationUser(jsonUser.getCompanyName());
		user.setEmail(jsonUser.getEmail());
		if (jsonUser.getAddress() != null) {
			user.setAddress(new Address(jsonUser.getAddress().getCity()));
		}

		if (userDao.saveOrUpdate(user)) {
			int status = STATUS_OK;
			String result = "Saved user: " + jsonUser.toString();
			return Response.status(status).entity(result).build();
		}

		int status = STATUS_ERROR;
		String result = "Can't save user: " + jsonUser.toString();
		return Response.status(status).entity(result).build();
	}

	@GET
	@Path("/individual")
	@Produces(MediaType.APPLICATION_JSON)
	public List<JsonIndividualUser> getIndividualUsers(@QueryParam("page") Integer pageNumber) {
		pageNumber = getPageNumber(pageNumber);

		List<JsonIndividualUser> jsonUsers = new ArrayList<JsonIndividualUser>();

		List<IndividualUser> users = userDao.getAll(IndividualUser.class, pageNumber, USERS_PER_PAGE,
				LAST_NAME_USER_PROPERTY);
		for (IndividualUser user : users) {
			JsonIndividualUser jsonUser = new JsonIndividualUser();
			jsonUser.setId(user.getId());
			jsonUser.setFirstName(user.getFirstName());
			jsonUser.setLastName(user.getLastName());
			jsonUser.setEmail(user.getEmail());
			if (user.getAddress() != null) {
				JsonAddress address = new JsonAddress();
				address.setCity(user.getAddress().getCity());
				jsonUser.setAddress(address);
			}

			jsonUsers.add(jsonUser);
		}

		return jsonUsers;
	}

	@GET
	@Path("/corporation")
	@Produces(MediaType.APPLICATION_JSON)
	public List<JsonCorporationUser> getCorporationUsers(@QueryParam("page") Integer pageNumber) {
		pageNumber = getPageNumber(pageNumber);

		List<JsonCorporationUser> jsonUsers = new ArrayList<JsonCorporationUser>();

		List<CorporationUser> users = userDao.getAll(CorporationUser.class, pageNumber, USERS_PER_PAGE,
				COMPANY_NAME_USER_PROPERTY);
		for (CorporationUser user : users) {
			JsonCorporationUser jsonUser = new JsonCorporationUser();
			jsonUser.setId(user.getId());
			jsonUser.setCompanyName(user.getCompanyName());
			jsonUser.setEmail(user.getEmail());
			if (user.getAddress() != null) {
				JsonAddress address = new JsonAddress();
				address.setCity(user.getAddress().getCity());
				jsonUser.setAddress(address);
			}

			jsonUsers.add(jsonUser);
		}

		return jsonUsers;
	}

}
