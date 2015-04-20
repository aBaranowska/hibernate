package com.recglobal.hibernate.rest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.recglobal.hibernate.model.CardPayment;
import com.recglobal.hibernate.model.ChequePayment;
import com.recglobal.hibernate.model.Order;
import com.recglobal.hibernate.model.OrderStatus;
import com.recglobal.hibernate.model.PaymentStatus;
import com.recglobal.hibernate.model.Product;
import com.recglobal.hibernate.model.ProductOrder;
import com.recglobal.hibernate.model.User;
import com.recglobal.hibernate.rest.json.JsonCardOrder;
import com.recglobal.hibernate.rest.json.JsonChequeOrder;
import com.recglobal.hibernate.rest.json.JsonProductOrder;

@Path("/orders")
public class OrderService extends BaseService {

	@POST
	@Path("/card")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addCardOrder(JsonCardOrder jsonOrder) {
		User user = null;
		if (jsonOrder.getUserId() != null) {
			user = userDao.get(jsonOrder.getUserId());
		}

		CardPayment payment = null;
		if (jsonOrder.getCard() != null) {
			payment = new CardPayment(jsonOrder.getCard());
		}

		List<ProductOrder> products = new ArrayList<ProductOrder>();
		if (jsonOrder.getProducts() != null) {
			products = getProductOrders(jsonOrder.getProducts());
		}

		if (user != null && payment != null && products != null) {
			Order order = new Order(user, payment, products.toArray(new ProductOrder[products.size()]));
			if (jsonOrder.getCreateDate() != null) {
				order.setCreateDate(jsonOrder.getCreateDate());
			}

			if (orderDao.saveOrUpdate(order)) {
				int status = STATUS_OK;
				String result = "Saved order: " + jsonOrder.toString();
				return Response.status(status).entity(result).build();
			}
		}

		int status = STATUS_ERROR;
		String result = "Can't save order: " + jsonOrder.toString();
		return Response.status(status).entity(result).build();
	}

	@POST
	@Path("/cheque")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addChequeOrder(JsonChequeOrder jsonOrder) {
		User user = null;
		if (jsonOrder.getUserId() != null) {
			user = userDao.get(jsonOrder.getUserId());
		}

		ChequePayment payment = null;
		if (jsonOrder.getBank() != null) {
			payment = new ChequePayment(jsonOrder.getBank());
		}

		List<ProductOrder> products = new ArrayList<ProductOrder>();
		if (jsonOrder.getProducts() != null) {
			products = getProductOrders(jsonOrder.getProducts());
		}

		if (user != null && payment != null && products != null) {
			Order order = new Order(user, payment, products.toArray(new ProductOrder[products.size()]));
			if (jsonOrder.getCreateDate() != null) {
				order.setCreateDate(jsonOrder.getCreateDate());
			}

			if (orderDao.saveOrUpdate(order)) {
				int status = STATUS_OK;
				String result = "Saved order: " + jsonOrder.toString();
				return Response.status(status).entity(result).build();
			}
		}

		int status = STATUS_ERROR;
		String result = "Can't save order: " + jsonOrder.toString();
		return Response.status(status).entity(result).build();
	}

	@GET
	@Path("/card")
	@Produces(MediaType.APPLICATION_JSON)
	public List<JsonCardOrder> getCardOrders(@QueryParam("page") Integer pageNumber) {
		pageNumber = getPageNumber(pageNumber);

		List<JsonCardOrder> jsonCardOrders = getJsonCardOrders(null, null, pageNumber);

		return jsonCardOrders;
	}

	@GET
	@Path("/cheque")
	@Produces(MediaType.APPLICATION_JSON)
	public List<JsonChequeOrder> getChequeOrders(@QueryParam("page") Integer pageNumber) {
		pageNumber = getPageNumber(pageNumber);

		List<JsonChequeOrder> jsonChequeOrders = getJsonChequeOrders(null, null, pageNumber);

		return jsonChequeOrders;
	}

	@GET
	@Path("/card/{orderIds}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<JsonCardOrder> getSelectedCardOrders(@PathParam("orderIds") String orderIds) {
		List<Integer> ids = getNumericIds(orderIds);

		List<JsonCardOrder> jsonCardOrders = getJsonCardOrders(null, ids, null);

		return jsonCardOrders;
	}

	@GET
	@Path("/cheque/{orderIds}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<JsonChequeOrder> getSelectedChequeOrders(@PathParam("orderIds") String orderIds) {
		List<Integer> ids = getNumericIds(orderIds);

		List<JsonChequeOrder> jsonChequeOrders = getJsonChequeOrders(null, ids, null);

		return jsonChequeOrders;
	}

	@GET
	@Path("card/users/{userIds}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<JsonCardOrder> getUserCardOrders(@PathParam("userIds") String userIds,
			@QueryParam("page") Integer pageNumber) {
		pageNumber = getPageNumber(pageNumber);

		List<Integer> ids = getNumericIds(userIds);

		List<JsonCardOrder> jsonCardOrders = getJsonCardOrders(ids, null, pageNumber);

		return jsonCardOrders;
	}

	@GET
	@Path("cheque/users/{userIds}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<JsonChequeOrder> getUserChequeOrders(@PathParam("userIds") String userIds,
			@QueryParam("page") Integer pageNumber) {
		pageNumber = getPageNumber(pageNumber);

		List<Integer> ids = getNumericIds(userIds);

		List<JsonChequeOrder> jsonChequeOrders = getJsonChequeOrders(ids, null, pageNumber);

		return jsonChequeOrders;
	}

	@SuppressWarnings("rawtypes")
	@GET
	@Path("/top/categories")
	@Produces(MediaType.TEXT_PLAIN)
	public String getTopCategories() {
		List rows = orderDao.getTopCategories();
		String result = getText(rows);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@GET
	@Path("/top/products")
	@Produces(MediaType.TEXT_PLAIN)
	public String getTopProducts() {
		List rows = orderDao.getTopProducts();
		String result = getText(rows);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@GET
	@Path("/top/users/individual")
	@Produces(MediaType.TEXT_PLAIN)
	public String getTopUsersIndividual() {
		List rows = orderDao.getTopIndividualUsers();
		String result = getText(rows);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@GET
	@Path("/top/users/corporation")
	@Produces(MediaType.TEXT_PLAIN)
	public String getTopUsersCorporation() {
		List rows = orderDao.getTopCorporationUsers();
		String result = getText(rows);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@GET
	@Path("/category")
	@Produces(MediaType.TEXT_PLAIN)
	public String getOrderIdsByCategory(@QueryParam("name") String categoryName) {
		List rows = orderDao.getByCategory(categoryName);
		String result = getText(rows);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@GET
	@Path("/product")
	@Produces(MediaType.TEXT_PLAIN)
	public String getOrderIdsByProduct(@QueryParam("name") String productName) {
		List rows = orderDao.getByProduct(productName);
		String result = getText(rows);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@GET
	@Path("/create")
	@Produces(MediaType.TEXT_PLAIN)
	public String getOrderIdsByDate(@QueryParam("from") String fromDate, @QueryParam("to") String toDate) {
		try {
			Date from = Date.valueOf(fromDate);
			Date to = Date.valueOf(toDate);
			List rows = orderDao.getByDate(from, to);
			String result = getText(rows);
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@GET
	@Path("/status")
	@Produces(MediaType.TEXT_PLAIN)
	public String getOrderIdsByStatus(@QueryParam("name") String orderStatuses) {
		List<OrderStatus> statusList = getStatusList(orderStatuses);
		List rows = orderDao.getByStatus(statusList.toArray(new OrderStatus[statusList.size()]));
		String result = getText(rows);
		return result;
	}

	@PUT
	@Path("/{orderIds}/cancel")
	public Response cancelOrders(@PathParam("orderIds") String orderIds) {
		List<Order> orders = new ArrayList<Order>();
		List<Integer> ids = getNumericIds(orderIds);
		for (Integer id : ids) {
			Order order = orderDao.get(id);
			if (order != null && order.getStatus().equals(OrderStatus.NOT_PAID)
					&& order.getPayment().getStatus().equals(PaymentStatus.UNKNOWN)) {
				order.setStatus(OrderStatus.CANCELLED);
				orders.add(order);
			} else {
				orders = null;
				break;
			}
		}

		if (orders != null) {
			if (orderDao.saveOrUpdate(orders.toArray(new Order[orders.size()]))) {
				int status = STATUS_OK;
				String result = "Orders cancelled";
				return Response.status(status).entity(result).build();
			}
		}

		int status = STATUS_ERROR;
		String result = "Can't cancel orders";
		return Response.status(status).entity(result).build();
	}

	@PUT
	@Path("/{orderIds}/accept")
	public Response acceptPayments(@PathParam("orderIds") String orderIds) {
		List<Order> orders = new ArrayList<Order>();
		List<Integer> ids = getNumericIds(orderIds);
		for (Integer id : ids) {
			Order order = orderDao.get(id);
			if (order != null && order.getProducts().size() > 0 && order.getStatus().equals(OrderStatus.NOT_PAID)
					&& order.getPayment().getStatus().equals(PaymentStatus.UNKNOWN)) {
				order.getPayment().setStatus(PaymentStatus.ACCEPTED);
				order.setStatus(OrderStatus.PAID);
				orders.add(order);
			} else {
				orders = null;
				break;
			}
		}

		if (orders != null) {
			if (orderDao.saveOrUpdate(orders.toArray(new Order[orders.size()]))) {
				int status = STATUS_OK;
				String result = "Payments accepted";
				return Response.status(status).entity(result).build();
			}
		}

		int status = STATUS_ERROR;
		String result = "Can't accept payments";
		return Response.status(status).entity(result).build();
	}

	@PUT
	@Path("/{orderIds}/reject")
	public Response rejectPayments(@PathParam("orderIds") String orderIds) {
		List<Order> orders = new ArrayList<Order>();
		List<Integer> ids = getNumericIds(orderIds);
		for (Integer id : ids) {
			Order order = orderDao.get(id);
			if (order != null && order.getProducts().size() > 0 && order.getStatus().equals(OrderStatus.NOT_PAID)
					&& order.getPayment().getStatus().equals(PaymentStatus.UNKNOWN)) {
				order.getPayment().setStatus(PaymentStatus.REJECTED);
				orders.add(order);
			} else {
				orders = null;
				break;
			}
		}

		if (orders != null) {
			if (orderDao.saveOrUpdate(orders.toArray(new Order[orders.size()]))) {
				int status = STATUS_OK;
				String result = "Payments rejected";
				return Response.status(status).entity(result).build();
			}
		}

		int status = STATUS_ERROR;
		String result = "Can't reject payments";
		return Response.status(status).entity(result).build();
	}

	@PUT
	@Path("/{orderId}/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProduct(@PathParam("orderId") Integer orderId, JsonProductOrder jsonProductOrder) {
		if (jsonProductOrder.getProductId() != null && jsonProductOrder.getQuantity() != null) {
			Order order = orderDao.get(orderId);
			if (order != null && order.getStatus().equals(OrderStatus.NOT_PAID)
					&& order.getPayment().getStatus().equals(PaymentStatus.UNKNOWN)) {
				List<ProductOrder> productOrders = getProductOrders(Arrays.asList(jsonProductOrder));
				if (productOrders != null && productOrders.size() == 1) {
					ProductOrder productOrder = productOrders.get(0);
					order.addProduct(productOrder);

					if (orderDao.saveOrUpdate(order)) {
						int status = STATUS_OK;
						String result = "Product added";
						return Response.status(status).entity(result).build();
					}
				}
			}
		}

		int status = STATUS_ERROR;
		String result = "Can't add product";
		return Response.status(status).entity(result).build();
	}

	@PUT
	@Path("/{orderId}/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteProduct(@PathParam("orderId") Integer orderId, JsonProductOrder jsonProductOrder) {
		if (jsonProductOrder.getProductId() != null && jsonProductOrder.getQuantity() != null) {
			Order order = orderDao.get(orderId);
			if (order != null && order.getStatus().equals(OrderStatus.NOT_PAID)
					&& order.getPayment().getStatus().equals(PaymentStatus.UNKNOWN)) {
				for (ProductOrder productOrder : order.getProducts()) {
					if (productOrder.getId().equals(jsonProductOrder.getProductId())) {
						int oldQuantity = productOrder.getQuantity();
						int newQuantity = oldQuantity - jsonProductOrder.getQuantity();
						if (newQuantity > 0) {
							productOrder.setQuantity(newQuantity);
						} else {
							order.removeProduct(productOrder);
						}

						if (orderDao.saveOrUpdate(order)) {
							int status = STATUS_OK;
							String result = "Product deleted";
							return Response.status(status).entity(result).build();
						}

						break;
					}
				}
			}
		}

		int status = STATUS_ERROR;
		String result = "Can't delete product";
		return Response.status(status).entity(result).build();
	}

	/**
	 * return empty list if there was no product<br>
	 * order can exist without products<br>
	 * return null in case of error (bad product id)
	 */
	private List<ProductOrder> getProductOrders(List<JsonProductOrder> jsonProductOrders) {
		List<ProductOrder> productOrders = new ArrayList<ProductOrder>();

		for (JsonProductOrder jsonProductOrder : jsonProductOrders) {
			Product product = null;
			if (jsonProductOrder.getProductId() != null) {
				product = productDao.get(jsonProductOrder.getProductId());
			}
			Integer quantity = jsonProductOrder.getQuantity();

			if (product != null && quantity != null) {
				ProductOrder productOrder = new ProductOrder(product, quantity);
				productOrders.add(productOrder);
			} else {
				return null;
			}
		}

		return productOrders;
	}

	private List<JsonCardOrder> getJsonCardOrders(List<Integer> userIds, List<Integer> orderIds, Integer pageNumber) {
		List<JsonCardOrder> jsonCardOrders = new ArrayList<JsonCardOrder>();

		List<Order> cardOrders = orderDao.getAll(CardPayment.class, userIds, orderIds, pageNumber, ORDERS_PER_PAGE,
				CREATE_DATE_ORDER_PROPERTY);
		for (Order cardOrder : cardOrders) {
			Set<ProductOrder> productOrders = cardOrder.getProducts();

			JsonCardOrder jsonCardOrder = new JsonCardOrder();
			jsonCardOrder.setId(cardOrder.getId());
			jsonCardOrder.setCreateDate(cardOrder.getCreateDate());
			jsonCardOrder.setUserId(cardOrder.getUser().getId());
			jsonCardOrder.setProducts(getJsonProductOrders(productOrders));
			jsonCardOrder.setCard(((CardPayment) cardOrder.getPayment()).getCardType());
			jsonCardOrder.setOrderStatus(cardOrder.getStatus().toString());
			jsonCardOrder.setPaymentStatus(cardOrder.getPayment().getStatus().toString());
			jsonCardOrder.setAmount(getAmount(productOrders));

			jsonCardOrders.add(jsonCardOrder);
		}

		return jsonCardOrders;
	}

	private List<JsonChequeOrder> getJsonChequeOrders(List<Integer> userIds, List<Integer> orderIds, Integer pageNumber) {
		List<JsonChequeOrder> jsonChequeOrders = new ArrayList<JsonChequeOrder>();

		List<Order> chequeOrders = orderDao.getAll(ChequePayment.class, userIds, orderIds, pageNumber, ORDERS_PER_PAGE,
				CREATE_DATE_ORDER_PROPERTY);
		for (Order chequeOrder : chequeOrders) {
			Set<ProductOrder> productOrders = chequeOrder.getProducts();

			JsonChequeOrder jsonChequeOrder = new JsonChequeOrder();
			jsonChequeOrder.setId(chequeOrder.getId());
			jsonChequeOrder.setCreateDate(chequeOrder.getCreateDate());
			jsonChequeOrder.setUserId(chequeOrder.getUser().getId());
			jsonChequeOrder.setProducts(getJsonProductOrders(productOrders));
			jsonChequeOrder.setBank(((ChequePayment) chequeOrder.getPayment()).getBankName());
			jsonChequeOrder.setOrderStatus(chequeOrder.getStatus().toString());
			jsonChequeOrder.setPaymentStatus(chequeOrder.getPayment().getStatus().toString());
			jsonChequeOrder.setAmount(getAmount(productOrders));

			jsonChequeOrders.add(jsonChequeOrder);
		}

		return jsonChequeOrders;
	}

	private List<JsonProductOrder> getJsonProductOrders(Set<ProductOrder> productOrders) {
		List<JsonProductOrder> jsonProductOrders = new ArrayList<JsonProductOrder>();

		for (ProductOrder productOrder : productOrders) {
			JsonProductOrder jsonProductOrder = new JsonProductOrder();
			jsonProductOrder.setId(productOrder.getId());
			jsonProductOrder.setName(productOrder.getName());
			jsonProductOrder.setPrice(productOrder.getPrice());
			jsonProductOrder.setCategoryIds(getCategoryIds(productOrder.getCategories()));
			jsonProductOrder.setQuantity(productOrder.getQuantity());
			jsonProductOrder.setProductId(productOrder.getProduct().getId());

			jsonProductOrders.add(jsonProductOrder);
		}

		return jsonProductOrders;
	}

	private Double getAmount(Set<ProductOrder> productOrders) {
		Double total = 0.0;
		for (ProductOrder productOrder : productOrders) {
			double product = productOrder.getQuantity() * productOrder.getPrice();
			total += product;
		}
		return total;
	}

	@SuppressWarnings("rawtypes")
	private String getText(List rows) {
		StringBuilder sb = new StringBuilder();
		if (rows != null) {
			for (Object object : rows) {
				if (object instanceof Object[]) {
					Object[] oa = (Object[]) object;
					for (Object o : oa) {
						sb.append(o + ",");
					}
					sb.append("\n");
				} else {
					sb.append(object + ",");
				}
			}
		}
		return sb.toString();
	}

	private List<OrderStatus> getStatusList(String statutes) {
		List<OrderStatus> list = new ArrayList<OrderStatus>();
		String[] sa = statutes.split(",");
		for (String s : sa) {
			OrderStatus status = OrderStatus.valueOf(s);
			list.add(status);
		}
		return list;
	}

}
