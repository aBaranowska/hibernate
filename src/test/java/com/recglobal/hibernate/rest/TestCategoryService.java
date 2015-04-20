package com.recglobal.hibernate.rest;

import static com.reglobal.hibernate.dao.TestUtil.cleanup;
import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;

import com.recglobal.hibernate.rest.json.JsonCategory;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.test.framework.JerseyTest;

public class TestCategoryService extends JerseyTest {

	private JsonCategory jsonCategory;
	private List<JsonCategory> jsonCategories;
	private ObjectMapper mapper = new ObjectMapper();
	private String jsonStr;
	private ClientResponse response;

	public TestCategoryService() throws Exception {
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
	public void testAddCategory() throws Exception {
		jsonCategory = new JsonCategory();
		jsonCategory.setName("category");

		jsonStr = mapper.writeValueAsString(jsonCategory);

		response = webResource.path("/categories").type(MediaType.APPLICATION_JSON_TYPE)
				.post(ClientResponse.class, jsonStr);

		assertEquals(200, response.getStatus());
	}

	@Test
	public void testGetCategory() throws Exception {
		jsonCategory = new JsonCategory();
		jsonCategory.setName("category");
		jsonStr = mapper.writeValueAsString(jsonCategory);
		response = webResource.path("/categories").type(MediaType.APPLICATION_JSON_TYPE)
				.post(ClientResponse.class, jsonStr);
		assertEquals(200, response.getStatus());

		response = webResource.path("/categories").get(ClientResponse.class);
		assertEquals(200, response.getStatus());

		jsonStr = response.getEntity(String.class);
		jsonCategories = mapper.readValue(jsonStr, new TypeReference<List<JsonCategory>>() {
		});
		assertEquals(1, jsonCategories.size());
	}

}
