package com.recglobal.hibernate.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.recglobal.hibernate.rest.json.JsonCardOrder;
import com.recglobal.hibernate.rest.json.JsonCategory;
import com.recglobal.hibernate.rest.json.JsonChequeOrder;
import com.recglobal.hibernate.rest.json.JsonCorporationUser;
import com.recglobal.hibernate.rest.json.JsonIndividualUser;
import com.recglobal.hibernate.rest.json.JsonProduct;
import com.recglobal.hibernate.rest.json.JsonProductOrder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ClientMain {

	public static final String BASE_URI = "http://localhost/hibernateApp/rest";
	public static final String USERS_INDIVIDUAL_PATH = "/users/individual";
	public static final String USERS_CORPORATION_PATH = "/users/corporation";
	public static final String CATEGORIES_PATH = "/categories";
	public static final String PRODUCTS_PATH = "/products";
	public static final String ORDERS_CARD_PATH = "/orders/card";
	public static final String ORDERS_CHEQUE_PATH = "/orders/cheque";
	public static final String ORDERS_ACCEPT = "/orders/{ids}/accept";
	public static final String ORDERS_REJECT = "/orders/{ids}/reject";
	public static final String ORDERS_CANCEL = "/orders/{ids}/cancel";

	public static final int STATUS_OK = 200;

	public static final int MAX_USERS = 5;
	public static final int MAX_CATEGORIES = 5;
	public static final int MAX_PRODUCTS = 10;
	public static final int MAX_ORDERS = 20;
	public static final int MAX_PRODUCTS_IN_CATEGORY = 2;
	public static final int MAX_PRODUCTS_IN_ORDER = 2;
	public static final int MIN_PRICE = 10;
	public static final int MAX_PRICE = 50;
	public static final int MIN_QUANTITY = 1;
	public static final int MAX_QUANTITY = 5;

	private Random random = new Random();
	private ObjectMapper mapper = new ObjectMapper();
	private String jsonStr;
	private WebResource webResource;
	private ClientResponse response;

	private List<Integer> categoryIds = new ArrayList<Integer>();
	private List<Integer> productIds = new ArrayList<Integer>();
	private List<Integer> userIds = new ArrayList<Integer>();
	private List<Integer> orderIds = new ArrayList<Integer>();

	public ClientMain() {
		Client client = Client.create();
		webResource = client.resource(BASE_URI);
	}

	public int createCategories() throws Exception {
		int n = 0;
		for (int i = 1; i <= MAX_CATEGORIES; i++) {
			JsonCategory jsonCategory = new JsonCategory();
			jsonCategory.setName("category" + i);

			jsonStr = mapper.writeValueAsString(jsonCategory);

			response = webResource.path(CATEGORIES_PATH).type(MediaType.APPLICATION_JSON_TYPE)
					.post(ClientResponse.class, jsonStr);
			if (response.getStatus() != STATUS_OK) {
				break;
			}
			n++;
		}
		return n;
	}

	public void getCategories() throws Exception {
		int page = 1;
		do {
			response = webResource.path(CATEGORIES_PATH).queryParam("page", String.valueOf(page))
					.get(ClientResponse.class);
			if (response.getStatus() == STATUS_OK) {
				jsonStr = response.getEntity(String.class);
				List<JsonCategory> jsonCategories = mapper.readValue(jsonStr, new TypeReference<List<JsonCategory>>() {
				});
				for (JsonCategory jsonCategory : jsonCategories) {
					categoryIds.add(jsonCategory.getId());
					System.out.println("add categoryId = " + jsonCategory.getId() + ", size = " + categoryIds.size());
				}
				if (jsonCategories.size() > 0 && categoryIds.size() < MAX_CATEGORIES) {
					page++;
				} else {
					break;
				}
			} else {
				break;
			}
		} while (true);
	}

	public int createProducts() throws Exception {
		int n = 0;
		for (int i = 1; i <= MAX_PRODUCTS; i++) {
			Integer price = new Integer(random.nextInt(MAX_PRICE - MIN_PRICE + 1) + MIN_PRICE);
			int count = random.nextInt(MAX_PRODUCTS_IN_CATEGORY + 1);

			JsonProduct jsonProduct = new JsonProduct();
			jsonProduct.setName("product" + i);

			jsonProduct.setPrice(price.doubleValue());

			Set<Integer> ids = new HashSet<Integer>();
			for (int j = 0; j < count; j++) {
				int categoryIndex = random.nextInt(MAX_CATEGORIES);
				if (!ids.add(categoryIds.get(categoryIndex))) {
					j--;
				}
			}
			jsonProduct.setCategoryIds(new ArrayList<Integer>(ids));

			jsonStr = mapper.writeValueAsString(jsonProduct);

			response = webResource.path(PRODUCTS_PATH).type(MediaType.APPLICATION_JSON_TYPE)
					.post(ClientResponse.class, jsonStr);
			if (response.getStatus() != STATUS_OK) {
				break;
			}
			n++;
		}
		return n;
	}

	public void getProducts() throws Exception {
		int page = 1;
		do {
			response = webResource.path(PRODUCTS_PATH).queryParam("page", String.valueOf(page))
					.get(ClientResponse.class);
			if (response.getStatus() == STATUS_OK) {
				jsonStr = response.getEntity(String.class);
				List<JsonProduct> jsonProducts = mapper.readValue(jsonStr, new TypeReference<List<JsonProduct>>() {
				});
				for (JsonProduct jsonProduct : jsonProducts) {
					productIds.add(jsonProduct.getId());
					System.out.println("add productId = " + jsonProduct.getId() + ", size = " + productIds.size());
				}
				if (jsonProducts.size() > 0 && productIds.size() < MAX_PRODUCTS) {
					page++;
				} else {
					break;
				}
			} else {
				break;
			}
		} while (true);
	}

	public int createUsers() throws Exception {
		int n = 0;
		for (int i = 1; i <= MAX_USERS; i++) {
			int type = random.nextInt(2);
			if (type == 0) {
				JsonIndividualUser jsonIndividualUser = new JsonIndividualUser();
				jsonIndividualUser.setFirstName("firstname" + i);
				jsonIndividualUser.setLastName("lastname" + i);

				jsonStr = mapper.writeValueAsString(jsonIndividualUser);

				response = webResource.path(USERS_INDIVIDUAL_PATH).type(MediaType.APPLICATION_JSON_TYPE)
						.post(ClientResponse.class, jsonStr);
				if (response.getStatus() != STATUS_OK) {
					break;
				}
				n++;
			} else {
				JsonCorporationUser jsonCorporationUser = new JsonCorporationUser();
				jsonCorporationUser.setCompanyName("companyName" + i);

				jsonStr = mapper.writeValueAsString(jsonCorporationUser);

				response = webResource.path(USERS_CORPORATION_PATH).type(MediaType.APPLICATION_JSON_TYPE)
						.post(ClientResponse.class, jsonStr);
				if (response.getStatus() != STATUS_OK) {
					break;
				}
				n++;
			}
		}
		return n;
	}

	public void getUsers() throws Exception {
		int page = 1;
		do {
			response = webResource.path(USERS_INDIVIDUAL_PATH).queryParam("page", String.valueOf(page))
					.get(ClientResponse.class);
			if (response.getStatus() == STATUS_OK) {
				jsonStr = response.getEntity(String.class);
				List<JsonIndividualUser> jsonIndividualUsers = mapper.readValue(jsonStr,
						new TypeReference<List<JsonIndividualUser>>() {
						});
				for (JsonIndividualUser jsonIndividualUser : jsonIndividualUsers) {
					userIds.add(jsonIndividualUser.getId());
					System.out.println("add individualUserId = " + jsonIndividualUser.getId() + ", size = "
							+ userIds.size());
				}
				if (jsonIndividualUsers.size() > 0 && userIds.size() < MAX_USERS) {
					page++;
				} else {
					break;
				}
			} else {
				break;
			}
		} while (true);

		page = 1;
		do {
			response = webResource.path(USERS_CORPORATION_PATH).queryParam("page", String.valueOf(page))
					.get(ClientResponse.class);
			if (response.getStatus() == STATUS_OK) {
				jsonStr = response.getEntity(String.class);
				List<JsonCorporationUser> jsonCorporationUsers = mapper.readValue(jsonStr,
						new TypeReference<List<JsonCorporationUser>>() {
						});
				for (JsonCorporationUser jsonCorporationUser : jsonCorporationUsers) {
					userIds.add(jsonCorporationUser.getId());
					System.out.println("add corporationUserId = " + jsonCorporationUser.getId() + ", size = "
							+ userIds.size());
				}
				if (jsonCorporationUsers.size() > 0 && userIds.size() < MAX_USERS) {
					page++;
				} else {
					break;
				}
			} else {
				break;
			}
		} while (true);
	}

	public int createOrders() throws Exception {
		int n = 0;
		for (int i = 1; i <= MAX_ORDERS; i++) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, -random.nextInt(180));
			Date createDate = cal.getTime();

			int userIndex = random.nextInt(MAX_USERS);
			int count = random.nextInt(MAX_PRODUCTS_IN_ORDER + 1);

			int type = random.nextInt(2);
			if (type == 0) {
				JsonCardOrder jsonCardOrder = new JsonCardOrder();
				jsonCardOrder.setCard("card" + i);

				jsonCardOrder.setCreateDate(createDate);

				jsonCardOrder.setUserId(userIds.get(userIndex));

				List<JsonProductOrder> jsonProductOrders = new ArrayList<JsonProductOrder>();
				for (int j = 0; j < count; j++) {
					JsonProductOrder jsonProductOrder = new JsonProductOrder();

					int productIndex = random.nextInt(MAX_PRODUCTS);
					jsonProductOrder.setProductId(productIds.get(productIndex));

					int quantity = random.nextInt(MAX_QUANTITY - MIN_QUANTITY + 1) + MIN_QUANTITY;
					jsonProductOrder.setQuantity(quantity);

					jsonProductOrders.add(jsonProductOrder);
				}
				jsonCardOrder.setProducts(jsonProductOrders);

				jsonStr = mapper.writeValueAsString(jsonCardOrder);

				response = webResource.path(ORDERS_CARD_PATH).type(MediaType.APPLICATION_JSON_TYPE)
						.post(ClientResponse.class, jsonStr);
				if (response.getStatus() != STATUS_OK) {
					break;
				}
				n++;
			} else {
				JsonChequeOrder jsonChequeOrder = new JsonChequeOrder();
				jsonChequeOrder.setBank("bank" + i);

				jsonChequeOrder.setUserId(userIds.get(userIndex));

				List<JsonProductOrder> jsonProductOrders = new ArrayList<JsonProductOrder>();
				for (int j = 0; j < count; j++) {
					JsonProductOrder jsonProductOrder = new JsonProductOrder();

					int productIndex = random.nextInt(MAX_PRODUCTS);
					jsonProductOrder.setProductId(productIds.get(productIndex));

					int quantity = random.nextInt(MAX_PRICE - MIN_PRICE + 1) + MIN_PRICE;
					jsonProductOrder.setQuantity(quantity);

					jsonProductOrders.add(jsonProductOrder);
				}
				jsonChequeOrder.setProducts(jsonProductOrders);

				jsonStr = mapper.writeValueAsString(jsonChequeOrder);

				response = webResource.path(ORDERS_CHEQUE_PATH).type(MediaType.APPLICATION_JSON_TYPE)
						.post(ClientResponse.class, jsonStr);
				if (response.getStatus() != STATUS_OK) {
					break;
				}
				n++;
			}
		}
		return n;
	}

	public void getOrders() throws Exception {
		int page = 1;
		do {
			response = webResource.path(ORDERS_CARD_PATH).queryParam("page", String.valueOf(page))
					.get(ClientResponse.class);
			if (response.getStatus() == STATUS_OK) {
				jsonStr = response.getEntity(String.class);
				List<JsonCardOrder> jsonCardOrders = mapper.readValue(jsonStr,
						new TypeReference<List<JsonCardOrder>>() {
						});
				for (JsonCardOrder jsonCardOrder : jsonCardOrders) {
					orderIds.add(jsonCardOrder.getId());
					System.out.println("add cardOrderId = " + jsonCardOrder.getId() + ", size = " + orderIds.size());
				}
				if (jsonCardOrders.size() > 0 && orderIds.size() < MAX_ORDERS) {
					page++;
				} else {
					break;
				}
			} else {
				break;
			}
		} while (true);

		page = 1;
		do {
			response = webResource.path(ORDERS_CHEQUE_PATH).queryParam("page", String.valueOf(page))
					.get(ClientResponse.class);
			if (response.getStatus() == STATUS_OK) {
				jsonStr = response.getEntity(String.class);
				List<JsonChequeOrder> jsonChequeOrders = mapper.readValue(jsonStr,
						new TypeReference<List<JsonChequeOrder>>() {
						});
				for (JsonChequeOrder jsonChequeOrder : jsonChequeOrders) {
					orderIds.add(jsonChequeOrder.getId());
					System.out
							.println("add chequeOrderId = " + jsonChequeOrder.getId() + ", size = " + orderIds.size());
				}
				if (jsonChequeOrders.size() > 0 && userIds.size() < MAX_ORDERS) {
					page++;
				} else {
					break;
				}
			} else {
				break;
			}
		} while (true);
	}

	private void changeOrdersStatus() {
		for (int i = 0; i < orderIds.size(); i++) {
			Integer orderId = orderIds.get(i);

			int type = random.nextInt(4);
			if (type == 0) {
				response.setStatus(STATUS_OK);
			} else if (type == 1) {
				String path = ORDERS_ACCEPT;
				path = path.replace("{ids}", "" + orderId);
				response = webResource.path(path).put(ClientResponse.class);
				if (response.getStatus() != STATUS_OK) {
					System.err.println("can't accept orderId = " + orderId);
				}
			} else if (type == 2) {
				String path = ORDERS_REJECT;
				path = path.replace("{ids}", "" + orderId);
				response = webResource.path(path).put(ClientResponse.class);
				if (response.getStatus() != STATUS_OK) {
					System.err.println("can't reject orderId = " + orderId);
				}
			} else {
				String path = ORDERS_CANCEL;
				path = path.replace("{ids}", "" + orderId);
				response = webResource.path(path).put(ClientResponse.class);
				if (response.getStatus() != STATUS_OK) {
					System.err.println("can't cancel orderId = " + orderId);
				}
			}
		}
	}

	public static void main(String[] args) {
		ClientMain myClient = new ClientMain();
		try {
			if (myClient.createCategories() != MAX_CATEGORIES) {
				System.err.println("categories missing");
				return;
			}
			myClient.getCategories();

			if (myClient.createProducts() != MAX_PRODUCTS) {
				System.err.println("products missing");
				return;
			}
			myClient.getProducts();

			if (myClient.createUsers() != MAX_USERS) {
				System.err.println("users missing");
				return;
			}
			myClient.getUsers();

			if (myClient.createOrders() != MAX_ORDERS) {
				System.err.println("orders missing");
				return;
			}
			myClient.getOrders();

			myClient.changeOrdersStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
