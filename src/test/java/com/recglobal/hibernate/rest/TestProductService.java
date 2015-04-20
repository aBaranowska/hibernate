package com.recglobal.hibernate.rest;

import static com.reglobal.hibernate.dao.TestUtil.categoryDao;
import static com.reglobal.hibernate.dao.TestUtil.cleanup;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;

import com.recglobal.hibernate.model.Category;
import com.recglobal.hibernate.rest.json.JsonProduct;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.test.framework.JerseyTest;

public class TestProductService extends JerseyTest {

	private JsonProduct jsonProduct;
	private List<JsonProduct> jsonProducts;
	private ObjectMapper mapper = new ObjectMapper();
	private String jsonStr;
	private ClientResponse response;
	private Category category;

	public TestProductService() throws Exception {
		super("dummy");
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		webResource = jerseyClient.resource("http://localhost/hibernateApp/rest");
		cleanup();

		category = new Category("category");
		categoryDao.saveOrUpdate(category);
	}

	@Test
	public void testAddProduct() throws Exception {
		jsonProduct = new JsonProduct();
		jsonProduct.setName("product");
		jsonProduct.setPrice(10.0);
		jsonProduct.setCategoryIds(Arrays.asList(category.getId()));

		jsonStr = mapper.writeValueAsString(jsonProduct);

		response = webResource.path("/products").type(MediaType.APPLICATION_JSON_TYPE)
				.post(ClientResponse.class, jsonStr);

		assertEquals(200, response.getStatus());
	}

	@Test
	public void testGetProduct() throws Exception {
		jsonProduct = new JsonProduct();
		jsonProduct.setName("product");
		jsonProduct.setPrice(10.0);
		jsonProduct.setCategoryIds(Arrays.asList(category.getId()));
		jsonStr = mapper.writeValueAsString(jsonProduct);
		response = webResource.path("/products").type(MediaType.APPLICATION_JSON_TYPE)
				.post(ClientResponse.class, jsonStr);
		assertEquals(200, response.getStatus());

		response = webResource.path("/categories").get(ClientResponse.class);
		assertEquals(200, response.getStatus());

		jsonStr = response.getEntity(String.class);
		jsonProducts = mapper.readValue(jsonStr, new TypeReference<List<JsonProduct>>() {
		});
		assertEquals(1, jsonProducts.size());
	}

}
