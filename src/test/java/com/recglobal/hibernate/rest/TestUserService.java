package com.recglobal.hibernate.rest;

import static com.reglobal.hibernate.dao.TestUtil.cleanup;
import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;

import com.recglobal.hibernate.rest.json.JsonCorporationUser;
import com.recglobal.hibernate.rest.json.JsonIndividualUser;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.test.framework.JerseyTest;

public class TestUserService extends JerseyTest {

	private JsonIndividualUser jsonIndividualUser;
	private List<JsonIndividualUser> jsonIndividualUsers;
	private JsonCorporationUser jsonCorporationUser;
	private List<JsonCorporationUser> jsonCorporationUsers;
	private ObjectMapper mapper = new ObjectMapper();
	private String jsonStr;
	private ClientResponse response;

	public TestUserService() throws Exception {
		super("dummy");
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		webResource = jerseyClient.resource("http://localhost/hibernateApp/rest");
		cleanup();
	}

	@Test
	public void testAddIndividualUser() throws Exception {
		jsonIndividualUser = new JsonIndividualUser();
		jsonIndividualUser.setFirstName("firstname");
		jsonIndividualUser.setLastName("lastname");

		jsonStr = mapper.writeValueAsString(jsonIndividualUser);

		response = webResource.path("/users/individual").type(MediaType.APPLICATION_JSON_TYPE)
				.post(ClientResponse.class, jsonStr);

		assertEquals(200, response.getStatus());
	}

	@Test
	public void testGetIndividualUser() throws Exception {
		jsonIndividualUser = new JsonIndividualUser();
		jsonIndividualUser.setFirstName("firstname");
		jsonIndividualUser.setLastName("lastname");
		jsonStr = mapper.writeValueAsString(jsonIndividualUser);
		response = webResource.path("/users/individual").type(MediaType.APPLICATION_JSON_TYPE)
				.post(ClientResponse.class, jsonStr);
		assertEquals(200, response.getStatus());

		response = webResource.path("/users/individual").get(ClientResponse.class);
		assertEquals(200, response.getStatus());

		jsonStr = response.getEntity(String.class);
		jsonIndividualUsers = mapper.readValue(jsonStr, new TypeReference<List<JsonIndividualUser>>() {
		});
		assertEquals(1, jsonIndividualUsers.size());
	}

	@Test
	public void testAddCorporationUser() throws Exception {
		jsonCorporationUser = new JsonCorporationUser();
		jsonCorporationUser.setCompanyName("company");

		jsonStr = mapper.writeValueAsString(jsonCorporationUser);

		response = webResource.path("/users/corporation").type(MediaType.APPLICATION_JSON_TYPE)
				.post(ClientResponse.class, jsonStr);

		assertEquals(200, response.getStatus());
	}

	@Test
	public void testGetCorporationUser() throws Exception {
		jsonCorporationUser = new JsonCorporationUser();
		jsonCorporationUser.setCompanyName("company");
		jsonStr = mapper.writeValueAsString(jsonCorporationUser);
		response = webResource.path("/users/corporation").type(MediaType.APPLICATION_JSON_TYPE)
				.post(ClientResponse.class, jsonStr);
		assertEquals(200, response.getStatus());

		response = webResource.path("/users/corporation").get(ClientResponse.class);
		assertEquals(200, response.getStatus());

		jsonStr = response.getEntity(String.class);
		jsonCorporationUsers = mapper.readValue(jsonStr, new TypeReference<List<JsonCorporationUser>>() {
		});
		assertEquals(1, jsonCorporationUsers.size());
	}

}
