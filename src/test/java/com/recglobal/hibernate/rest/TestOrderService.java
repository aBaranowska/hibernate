package com.recglobal.hibernate.rest;

import static com.reglobal.hibernate.dao.TestUtil.categoryDao;
import static com.reglobal.hibernate.dao.TestUtil.cleanup;
import static com.reglobal.hibernate.dao.TestUtil.productDao;
import static com.reglobal.hibernate.dao.TestUtil.userDao;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;

import com.recglobal.hibernate.model.Category;
import com.recglobal.hibernate.model.IndividualUser;
import com.recglobal.hibernate.model.Product;
import com.recglobal.hibernate.rest.json.JsonCardOrder;
import com.recglobal.hibernate.rest.json.JsonChequeOrder;
import com.recglobal.hibernate.rest.json.JsonProductOrder;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.test.framework.JerseyTest;

public class TestOrderService extends JerseyTest {

	private JsonCardOrder jsonCardOrder;
	private List<JsonCardOrder> jsonCardOrders;
	private JsonChequeOrder jsonChequeOrder;
	private List<JsonChequeOrder> jsonChequeOrders;
	private JsonProductOrder jsonProductOrder;
	private ObjectMapper mapper = new ObjectMapper();
	private String jsonStr;
	private ClientResponse response;
	private Category category;
	private Product product;
	private IndividualUser individualUser;

	public TestOrderService() throws Exception {
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

		product = new Product("product", 10.0, category);
		productDao.saveOrUpdate(product);

		individualUser = new IndividualUser("firstanem", "lastname");
		userDao.saveOrUpdate(individualUser);

		jsonProductOrder = new JsonProductOrder();
		jsonProductOrder.setProductId(product.getId());
		jsonProductOrder.setQuantity(5);
	}

	@Test
	public void testAddCardOrder() throws Exception {
		jsonCardOrder = new JsonCardOrder();
		jsonCardOrder.setCard("card");
		jsonCardOrder.setUserId(individualUser.getId());
		jsonCardOrder.setProducts(Arrays.asList(jsonProductOrder));

		jsonStr = mapper.writeValueAsString(jsonCardOrder);

		response = webResource.path("/orders/card").type(MediaType.APPLICATION_JSON_TYPE)
				.post(ClientResponse.class, jsonStr);

		assertEquals(200, response.getStatus());
	}

	@Test
	public void testGetCardOrder() throws Exception {
		jsonCardOrder = new JsonCardOrder();
		jsonCardOrder.setCard("card");
		jsonCardOrder.setUserId(individualUser.getId());
		jsonCardOrder.setProducts(Arrays.asList(jsonProductOrder));
		jsonStr = mapper.writeValueAsString(jsonCardOrder);
		response = webResource.path("/orders/card").type(MediaType.APPLICATION_JSON_TYPE)
				.post(ClientResponse.class, jsonStr);
		assertEquals(200, response.getStatus());

		response = webResource.path("/orders/card").get(ClientResponse.class);
		assertEquals(200, response.getStatus());

		jsonStr = response.getEntity(String.class);
		jsonCardOrders = mapper.readValue(jsonStr, new TypeReference<List<JsonCardOrder>>() {
		});
		assertEquals(1, jsonCardOrders.size());
	}

	@Test
	public void testAddChequeOrder() throws Exception {
		jsonChequeOrder = new JsonChequeOrder();
		jsonChequeOrder.setBank("bank");
		jsonChequeOrder.setUserId(individualUser.getId());
		jsonChequeOrder.setProducts(Arrays.asList(jsonProductOrder));

		jsonStr = mapper.writeValueAsString(jsonChequeOrder);

		response = webResource.path("/orders/cheque").type(MediaType.APPLICATION_JSON_TYPE)
				.post(ClientResponse.class, jsonStr);

		assertEquals(200, response.getStatus());
	}

	@Test
	public void testGetChequeOrder() throws Exception {
		jsonChequeOrder = new JsonChequeOrder();
		jsonChequeOrder.setBank("bank");
		jsonChequeOrder.setUserId(individualUser.getId());
		jsonChequeOrder.setProducts(Arrays.asList(jsonProductOrder));
		jsonStr = mapper.writeValueAsString(jsonChequeOrder);
		response = webResource.path("/orders/cheque").type(MediaType.APPLICATION_JSON_TYPE)
				.post(ClientResponse.class, jsonStr);
		assertEquals(200, response.getStatus());

		response = webResource.path("/orders/cheque").get(ClientResponse.class);
		assertEquals(200, response.getStatus());

		jsonStr = response.getEntity(String.class);
		jsonChequeOrders = mapper.readValue(jsonStr, new TypeReference<List<JsonChequeOrder>>() {
		});
		assertEquals(1, jsonChequeOrders.size());
	}

}
