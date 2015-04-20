package com.reglobal.hibernate.dao;

import static com.reglobal.hibernate.dao.TestUtil.categoryDao;
import static com.reglobal.hibernate.dao.TestUtil.cleanup;
import static com.reglobal.hibernate.dao.TestUtil.getProductIds;
import static com.reglobal.hibernate.dao.TestUtil.orderDao;
import static com.reglobal.hibernate.dao.TestUtil.paymentDao;
import static com.reglobal.hibernate.dao.TestUtil.productDao;
import static com.reglobal.hibernate.dao.TestUtil.userDao;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.recglobal.hibernate.model.CardPayment;
import com.recglobal.hibernate.model.Category;
import com.recglobal.hibernate.model.ChequePayment;
import com.recglobal.hibernate.model.CorporationUser;
import com.recglobal.hibernate.model.IndividualUser;
import com.recglobal.hibernate.model.Order;
import com.recglobal.hibernate.model.Payment;
import com.recglobal.hibernate.model.Product;
import com.recglobal.hibernate.model.ProductOrder;
import com.recglobal.hibernate.model.User;

public class TestOrder {

	@Before
	public void init() {
		cleanup();
	}

	@Test
	public void testSave_noProduct() {
		IndividualUser user = new IndividualUser("firstname1", "lastname1");
		CardPayment payment = new CardPayment("type1");
		Order order = new Order(user, payment);
		// save to database
		assertTrue(orderDao.saveOrUpdate(order));

		// get from database
		checkAll(order, user, payment, false);
	}

	@Test
	public void testSave_oneProduct() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		CorporationUser user = new CorporationUser("name1");
		ChequePayment payment = new ChequePayment("bank1");
		Product product = new Product("product1", 10.0, category);
		ProductOrder productOrder = new ProductOrder(product, 1);
		Order order = new Order(user, payment, productOrder);
		// save to database
		assertTrue(orderDao.saveOrUpdate(order));

		// get from database
		checkAll(order, user, payment, category, Arrays.asList(product.getId()), Arrays.asList(productOrder.getId()));
	}

	@Test
	public void testAddProduct_first() {
		IndividualUser user = new IndividualUser("firstname1", "lastname1");
		CardPayment payment = new CardPayment("type1");
		Order order = new Order(user, payment);
		// save to database
		assertTrue(orderDao.saveOrUpdate(order));

		// get from database
		checkAll(order, user, payment, false);

		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		Product product = new Product("product1", 10.0, category);
		ProductOrder productOrder = new ProductOrder(product, 1);
		productOrder = order.addProduct(productOrder);
		// save to database
		assertTrue(orderDao.saveOrUpdate(order));

		// get from database
		checkAll(order, user, payment, category, Arrays.asList(product.getId()), Arrays.asList(productOrder.getId()));
	}

	@Test
	public void testAddProduct_next_theSame() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		IndividualUser user = new IndividualUser("firstname1", "lastname1");
		CardPayment payment = new CardPayment("type1");
		Product product = new Product("product1", 10.0, category);
		ProductOrder productOrder = new ProductOrder(product, 1);
		Order order = new Order(user, payment, productOrder);
		// save to database
		assertTrue(orderDao.saveOrUpdate(order));

		// get from database
		checkAll(order, user, payment, category, Arrays.asList(product.getId()), Arrays.asList(productOrder.getId()));
		checkQuantity(order, productOrder, 1);

		ProductOrder productOrder2 = new ProductOrder(product, 1);
		productOrder2 = order.addProduct(productOrder2);
		assertEquals(productOrder, productOrder2);
		// save to database
		assertTrue(orderDao.saveOrUpdate(order));

		// get from database
		checkAll(order, user, payment, category, Arrays.asList(product.getId()), Arrays.asList(productOrder.getId()));
		checkQuantity(order, productOrder, 2);
	}

	@Test
	public void testAddProduct_next_different() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		IndividualUser user = new IndividualUser("firstname1", "lastname1");
		CardPayment payment = new CardPayment("type1");
		Product product = new Product("product1", 10.0, category);
		ProductOrder productOrder = new ProductOrder(product, 1);
		Order order = new Order(user, payment, productOrder);
		// save to database
		assertTrue(orderDao.saveOrUpdate(order));

		// get from database
		checkAll(order, user, payment, category, Arrays.asList(product.getId()), Arrays.asList(productOrder.getId()));
		checkQuantity(order, productOrder, 1);

		Product product2 = new Product("product2", 10.0, category);
		ProductOrder productOrder2 = new ProductOrder(product2, 1);
		productOrder2 = order.addProduct(productOrder2);
		assertNotEquals(productOrder, productOrder2);
		// save to database
		assertTrue(orderDao.saveOrUpdate(order));

		// get from database
		checkAll(order, user, payment, category, Arrays.asList(product.getId(), product2.getId()),
				Arrays.asList(productOrder.getId(), productOrder2.getId()));
		checkQuantity(order, productOrder, 1);
		checkQuantity(order, productOrder2, 1);
	}

	@Test
	public void testSave_twoProducts() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		IndividualUser user = new IndividualUser("firstname1", "lastname1");
		CardPayment payment = new CardPayment("type1");
		Product product = new Product("product1", 10.0, category);
		ProductOrder productOrder = new ProductOrder(product, 1);
		Product product2 = new Product("product2", 10.0, category);
		ProductOrder productOrder2 = new ProductOrder(product2, 1);
		Order order = new Order(user, payment, productOrder, productOrder2);
		// save to database
		assertTrue(orderDao.saveOrUpdate(order));

		// get from database
		checkAll(order, user, payment, category, Arrays.asList(product.getId(), product2.getId()),
				Arrays.asList(productOrder.getId(), productOrder2.getId()));
	}

	@Test
	public void testRemoveProduct() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		IndividualUser user = new IndividualUser("firstname1", "lastname1");
		CardPayment payment = new CardPayment("type1");
		Product product = new Product("product1", 10.0, category);
		ProductOrder productOrder = new ProductOrder(product, 1);
		Product product2 = new Product("product2", 10.0, category);
		ProductOrder productOrder2 = new ProductOrder(product2, 1);
		Order order = new Order(user, payment, productOrder, productOrder2);
		// save to database
		assertTrue(orderDao.saveOrUpdate(order));

		// get from database
		checkAll(order, user, payment, category, Arrays.asList(product.getId(), product2.getId()),
				Arrays.asList(productOrder.getId(), productOrder2.getId()));

		assertTrue(order.removeProduct(productOrder));
		// save to database
		assertTrue(orderDao.saveOrUpdate(order));

		// get from database
		checkAll(order, user, payment, category, Arrays.asList(product.getId(), product2.getId()),
				Arrays.asList(productOrder2.getId()));
	}

	private void checkAll(Order order, User user, Payment payment, boolean skip) {
		List<Order> dbOrders = orderDao.getAll();
		assertEquals(1, dbOrders.size());
		assertEquals(order.getId(), dbOrders.get(0).getId());

		List<User> dbUsers = userDao.getAll();
		assertEquals(1, dbUsers.size());
		assertEquals(user.getId(), dbUsers.get(0).getId());
		assertEquals(order.getUser().getId(), dbUsers.get(0).getId());

		List<Payment> dbPayments = paymentDao.getAll();
		assertEquals(1, dbPayments.size());
		assertEquals(payment.getId(), dbPayments.get(0).getId());
		assertEquals(order.getPayment().getId(), dbPayments.get(0).getId());

		if (!skip) {
			assertEquals(0, categoryDao.getAll().size());
			assertEquals(0, productDao.getAll().size());

			Order dbOrder = orderDao.get(order.getId());
			assertEquals(user.getId(), dbOrder.getUser().getId());
			assertEquals(payment.getId(), dbOrder.getPayment().getId());
		}
	}

	private void checkAll(Order order, User user, Payment payment, Category category, List<Integer> productIds,
			List<Integer> productOrderIds) {
		List<Integer> ids = new ArrayList<Integer>();
		ids.addAll(productIds);
		ids.addAll(productOrderIds);

		Collections.sort(productIds);
		Collections.sort(productOrderIds);
		Collections.sort(ids);

		checkAll(order, user, payment, true);

		List<Category> dbCategories = categoryDao.getAll();
		assertEquals(1, dbCategories.size());
		assertEquals(category.getId(), dbCategories.get(0).getId());

		List<Product> dbProducts = productDao.getAll();
		assertEquals(ids.size(), dbProducts.size());
		assertArrayEquals(ids.toArray(), getProductIds(dbProducts));

		Category dbCategory = categoryDao.get(category.getId());
		assertEquals(productIds.size(), dbCategory.getProducts().size());
		assertArrayEquals(productIds.toArray(), getProductIds(dbCategory.getProducts()));

		Order dbOrder = orderDao.get(order.getId());
		assertEquals(user.getId(), dbOrder.getUser().getId());
		assertEquals(payment.getId(), dbOrder.getPayment().getId());
		assertEquals(productOrderIds.size(), dbOrder.getProducts().size());
		assertArrayEquals(productOrderIds.toArray(), getProductIds(dbOrder.getProducts()));
	}

	private void checkQuantity(Order order, ProductOrder product, int quantity) {
		Order dbOrder = orderDao.get(order.getId());
		assertEquals(Integer.valueOf(quantity), dbOrder.findProduct(product.getId()).getQuantity());
	}

}
