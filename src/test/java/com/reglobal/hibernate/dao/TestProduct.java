package com.reglobal.hibernate.dao;

import static com.reglobal.hibernate.dao.TestUtil.categoryDao;
import static com.reglobal.hibernate.dao.TestUtil.cleanup;
import static com.reglobal.hibernate.dao.TestUtil.getCategoryIds;
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

public class TestProduct {

	@Before
	public void init() {
		cleanup();
	}

	@Test
	public void testSave_noCategory() {
		Product product = new Product("product1", 10.0);
		// save to database
		assertTrue(productDao.saveOrUpdate(product));

		// get from database
		checkAll(product.getId());
	}

	@Test
	public void testSave_oneCategory() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		Product product = new Product("product1", 10.0, category);
		// save to database
		assertTrue(productDao.saveOrUpdate(product));

		// get from database
		checkAll(category.getId(), product.getId());
	}

	@Test
	public void testSave_threeCategories() {
		List<Category> categories = new ArrayList<Category>();
		List<Integer> categoryIds = new ArrayList<Integer>();
		for (int i = 1; i <= 3; i++) {
			String name = "category" + i;
			Category category = new Category(name);
			// save to database
			assertTrue(categoryDao.saveOrUpdate(category));
			categories.add(category);
			categoryIds.add(category.getId());
		}
		Collections.sort(categoryIds);

		Product product = new Product("product1", 10.0, categories.toArray(new Category[3]));
		// save to database
		assertTrue(productDao.saveOrUpdate(product));

		// get from database
		checkAll(categoryIds, product.getId());
	}

	@Test
	public void testGet() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		Product product = new Product("product1", 3.0, category);
		// save to database
		assertTrue(productDao.saveOrUpdate(product));

		// get from database
		checkAll(category.getId(), product.getId());

		Product dbProduct = productDao.get(product.getId());
		assertEquals(product.getId(), dbProduct.getId());
		assertEquals("product1", dbProduct.getName());
		assertEquals(Double.valueOf(3.0), dbProduct.getPrice());
		assertEquals(1, dbProduct.getCategories().size());
		assertEquals(category.getId(), dbProduct.getCategory(0).getId());
		assertEquals("category1", dbProduct.getCategory(0).getName());
	}

	private void checkAll(Integer productId) {
		checkAll(new ArrayList<Integer>(), productId);
	}

	private void checkAll(Integer categoryId, Integer productId) {
		checkAll(Arrays.asList(categoryId), productId);
	}

	private void checkAll(List<Integer> categoryIds, Integer productId) {
		Collections.sort(categoryIds);

		List<Category> dbCategories = categoryDao.getAll();
		assertEquals(categoryIds.size(), dbCategories.size());
		assertArrayEquals(categoryIds.toArray(), getCategoryIds(dbCategories));

		List<Product> dbProducts = productDao.getAll();
		assertEquals(1, dbProducts.size());
		assertEquals(productId, dbProducts.get(0).getId());

		for (Integer categoryId : categoryIds) {
			Category dbCategory = categoryDao.get(categoryId);
			assertEquals(1, dbCategory.getProducts().size());
			assertEquals(productId, dbCategory.getProduct(0).getId());
		}

		Product dbProduct = productDao.get(productId);
		assertEquals(categoryIds.size(), dbProduct.getCategories().size());
		assertArrayEquals(categoryIds.toArray(), getCategoryIds(dbProduct.getCategories()));
	}

}
