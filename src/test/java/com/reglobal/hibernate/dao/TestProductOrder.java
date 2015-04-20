package com.reglobal.hibernate.dao;

import static com.reglobal.hibernate.dao.TestUtil.categoryDao;
import static com.reglobal.hibernate.dao.TestUtil.cleanup;
import static com.reglobal.hibernate.dao.TestUtil.getProductIds;
import static com.reglobal.hibernate.dao.TestUtil.productDao;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.recglobal.hibernate.model.Category;
import com.recglobal.hibernate.model.Product;
import com.recglobal.hibernate.model.ProductOrder;

public class TestProductOrder {

	@Before
	public void init() {
		cleanup();
	}

	@Test
	public void testSave() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		Product product = new Product("product1", 10.0, category);
		ProductOrder productOrder = new ProductOrder(product, 1);
		// save to database
		assertTrue(productDao.saveOrUpdate(productOrder));

		// get from database
		checkAll(category.getId(), product.getId(), productOrder.getId());
	}

	@Test
	public void testGet() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		Product product = new Product("product1", 3.0, category);
		ProductOrder productOrder = new ProductOrder(product, 6);
		// save to database
		assertTrue(productDao.saveOrUpdate(productOrder));

		// get from database
		checkAll(category.getId(), product.getId(), productOrder.getId());

		ProductOrder dbProduct = (ProductOrder) productDao.get(productOrder.getId());
		assertEquals(productOrder.getId(), dbProduct.getId());
		assertEquals("product1", dbProduct.getName());
		assertEquals(Double.valueOf(3.0), dbProduct.getPrice());
		assertEquals(1, dbProduct.getCategories().size());
		assertEquals(category.getId(), dbProduct.getCategory(0).getId());
		assertEquals("category1", dbProduct.getCategory(0).getName());
		assertEquals(Integer.valueOf(6), dbProduct.getQuantity());
		assertEquals(product.getId(), dbProduct.getProduct().getId());
	}

	@Test
	public void testRemoveProductOrder() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		Product product = new Product("product1", 10.0, category);
		ProductOrder productOrder = new ProductOrder(product, 1);
		// save to database
		assertTrue(productDao.saveOrUpdate(productOrder));

		// get from database
		checkAll(category.getId(), product.getId(), productOrder.getId());

		// delete from database
		assertTrue(productDao.delete(productOrder));

		// get from database
		checkAll(category.getId(), product.getId());
	}

	@Test
	public void testRemoveProduct() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		Product product = new Product("product1", 10.0, category);
		ProductOrder productOrder = new ProductOrder(product, 1);
		// save to database
		assertTrue(productDao.saveOrUpdate(productOrder));

		// get from database
		checkAll(category.getId(), product.getId(), productOrder.getId());

		// delete from database
		assertTrue(productDao.delete(productOrder));
		assertTrue(productDao.delete(product));

		// get from database
		checkAll(category.getId());
	}

	@Test
	public void testUpdateProductPrice() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		Product product = new Product("product1", 10.0, category);
		ProductOrder productOrder = new ProductOrder(product, 1);
		// save to database
		assertTrue(productDao.saveOrUpdate(productOrder));

		// get from database
		checkPrice(product, productOrder, Double.valueOf(10.0), Double.valueOf(10.0));

		product.setPrice(Double.valueOf(20.0));
		// save to database
		assertTrue(productDao.saveOrUpdate(productOrder));

		// get from database
		checkPrice(product, productOrder, Double.valueOf(20.0), Double.valueOf(10.0));
	}

	@Test
	public void testUpdateProductOrderPrice() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		Product product = new Product("product1", 10.0, category);
		ProductOrder productOrder = new ProductOrder(product, 1);
		// save to database
		assertTrue(productDao.saveOrUpdate(productOrder));

		// get from database
		checkPrice(product, productOrder, Double.valueOf(10.0), Double.valueOf(10.0));

		productOrder.setPrice(Double.valueOf(20.0));
		// save to database
		assertTrue(productDao.saveOrUpdate(productOrder));

		// get from database
		checkPrice(product, productOrder, Double.valueOf(10.0), Double.valueOf(20.0));
	}

	private void checkAll(Integer categoryId, Integer productId, Integer productOrderId) {
		checkAll(categoryId, Arrays.asList(productId), Arrays.asList(productOrderId));
	}

	private void checkAll(Integer categoryId, Integer productId) {
		checkAll(categoryId, Arrays.asList(productId), new ArrayList<Integer>());
	}

	private void checkAll(Integer categoryId) {
		checkAll(categoryId, new ArrayList<Integer>(), new ArrayList<Integer>());
	}

	private void checkAll(Integer categoryId, List<Integer> productIds, List<Integer> productOrderIds) {
		List<Integer> ids = new ArrayList<Integer>();
		ids.addAll(productIds);
		ids.addAll(productOrderIds);

		Collections.sort(productIds);
		Collections.sort(productOrderIds);
		Collections.sort(ids);

		List<Category> dbCategories = categoryDao.getAll();
		assertEquals(1, dbCategories.size());
		assertEquals(categoryId, dbCategories.get(0).getId());

		List<Product> dbProducts = productDao.getAll();
		assertEquals(ids.size(), dbProducts.size());
		assertArrayEquals(ids.toArray(), getProductIds(dbProducts));

		Category dbCategory = categoryDao.get(categoryId);
		assertEquals(productIds.size(), dbCategory.getProducts().size());
		assertArrayEquals(productIds.toArray(), getProductIds(dbCategory.getProducts()));

		for (Integer productId : productIds) {
			Product dbProduct = productDao.get(productId);
			assertEquals(1, dbProduct.getCategories().size());
			assertEquals(categoryId, dbProduct.getCategory(0).getId());
		}

		for (Integer productOrderId : productOrderIds) {
			Product dbProduct = productDao.get(productOrderId);
			assertEquals(1, dbProduct.getCategories().size());
			assertEquals(categoryId, dbProduct.getCategory(0).getId());
		}
	}

	private void checkPrice(Product product, ProductOrder productOrder, Double productPrice, Double productOrderPrice) {
		Product dbProduct = productDao.get(product.getId());
		ProductOrder dbProductOrder = (ProductOrder) productDao.get(productOrder.getId());
		assertEquals(productPrice, dbProduct.getPrice());
		assertEquals(productOrderPrice, dbProductOrder.getPrice());
		assertEquals(productPrice, dbProductOrder.getProduct().getPrice());
	}

}
