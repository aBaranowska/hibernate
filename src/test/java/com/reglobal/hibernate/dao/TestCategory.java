package com.reglobal.hibernate.dao;

import static com.reglobal.hibernate.dao.TestUtil.categoryDao;
import static com.reglobal.hibernate.dao.TestUtil.cleanup;
import static com.reglobal.hibernate.dao.TestUtil.getCategoryIds;
import static com.reglobal.hibernate.dao.TestUtil.getProductIds;
import static com.reglobal.hibernate.dao.TestUtil.getProductNames;
import static com.reglobal.hibernate.dao.TestUtil.productDao;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.recglobal.hibernate.model.Category;
import com.recglobal.hibernate.model.Product;

public class TestCategory {

	@Before
	public void init() {
		cleanup();
	}

	@Test
	public void testSave() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		// get from database
		checkAll(category.getId());

		Category category2 = new Category("category1");
		assertFalse(categoryDao.saveOrUpdate(category2));
	}

	@Test
	public void testGet_noProduct() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		// get from database
		checkAll(category.getId());

		Category dbCategory = categoryDao.get(category.getId());
		assertEquals(category.getId(), dbCategory.getId());
		assertEquals("category1", dbCategory.getName());
		assertEquals(0, dbCategory.getProducts().size());
	}

	@Test
	public void testGet_oneProduct() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		Product product = new Product("product1", 10.0, category);
		// save to database
		assertTrue(productDao.saveOrUpdate(product));

		// get from database
		checkAll(category.getId(), product.getId());

		Category dbCategory = categoryDao.get(category.getId());
		assertEquals(category.getId(), dbCategory.getId());
		assertEquals("category1", dbCategory.getName());
		assertEquals(1, dbCategory.getProducts().size());
		assertEquals(product.getId(), dbCategory.getProduct(0).getId());
		assertEquals("product1", dbCategory.getProduct(0).getName());
	}

	@Test
	public void testGet_oneProduct_firstProduct() {
		Product product = new Product("product1", 10.0);
		// save to database
		assertTrue(productDao.saveOrUpdate(product));

		Category category = new Category("category1", product);
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		// get from database
		checkAll(category.getId(), product.getId());

		Category dbCategory = categoryDao.get(category.getId());
		assertEquals(category.getId(), dbCategory.getId());
		assertEquals("category1", dbCategory.getName());
		assertEquals(1, dbCategory.getProducts().size());
		assertEquals(product.getId(), dbCategory.getProduct(0).getId());
		assertEquals("product1", dbCategory.getProduct(0).getName());
	}

	@Test
	public void testGet_threeProducts() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		List<Integer> productIds = new ArrayList<Integer>();
		for (int i = 1; i <= 3; i++) {
			String name = "product" + i;
			Product product = new Product(name, 10.0, category);
			// save to database
			assertTrue(productDao.saveOrUpdate(product));
			productIds.add(product.getId());
		}
		Collections.sort(productIds);

		// get from database
		checkAll(category.getId(), productIds);

		Category dbCategory = categoryDao.get(category.getId());
		assertEquals(category.getId(), dbCategory.getId());
		assertEquals("category1", dbCategory.getName());
		assertEquals(3, dbCategory.getProducts().size());
		assertArrayEquals(productIds.toArray(), getProductIds(dbCategory.getProducts()));
		assertArrayEquals(new String[] { "product1", "product2", "product3" },
				getProductNames(dbCategory.getProducts()));
	}

	@Test
	public void testSave_theSameProductName() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		Product product = new Product("product1", 10.0, category);
		// save to database
		assertTrue(productDao.saveOrUpdate(product));

		Product product2 = new Product("product1", 10.0, category);
		// save to database
		assertFalse(productDao.saveOrUpdate(product2));

		// get from database
		checkAll(category.getId(), product.getId());
	}

	@Test
	public void testSave_theSameProductName_firstProduct() {
		List<Product> products = new ArrayList<Product>();
		List<Integer> productIds = new ArrayList<Integer>();
		for (int i = 0; i < 2; i++) {
			Product product = new Product("product1", 10.0);
			// save to database
			assertTrue(productDao.saveOrUpdate(product));
			products.add(product);
			productIds.add(product.getId());
		}
		Collections.sort(productIds);

		Category category = new Category("category1", products.toArray(new Product[2]));
		// save to database
		assertFalse(categoryDao.saveOrUpdate(category));

		// get from database
		checkAll(productIds);
	}

	@Test
	public void testUpdate_theSameProductName() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		Product product = new Product("product1", 10.0, category);
		// save to database
		assertTrue(productDao.saveOrUpdate(product));

		Product product2 = new Product("product2", 10.0, category);
		// save to database
		assertTrue(productDao.saveOrUpdate(product2));

		product2.setName("product1");
		// save to database
		assertFalse(productDao.saveOrUpdate(product2));
	}

	@Test
	public void testUpdate_theSameProductName_noCategory() {
		Product product = new Product("product1", 10.0);
		// save to database
		assertTrue(productDao.saveOrUpdate(product));

		Product product2 = new Product("product2", 10.0);
		// save to database
		assertTrue(productDao.saveOrUpdate(product2));

		product2.setName("product1");
		// save to database
		assertTrue(productDao.saveOrUpdate(product2));
	}

	@Test
	public void testUpdate_theSameProductName_oneWithoutCategory() {
		Category category = new Category("category1");
		// save to database
		assertTrue(categoryDao.saveOrUpdate(category));

		Product product = new Product("product1", 10.0, category);
		// save to database
		assertTrue(productDao.saveOrUpdate(product));

		Product product2 = new Product("product2", 10.0);
		// save to database
		assertTrue(productDao.saveOrUpdate(product2));

		product2.setName("product1");
		// save to database
		assertTrue(productDao.saveOrUpdate(product2));
	}

	@Test
	public void testUpdateProductCategories() {
		Category category = new Category("category1");
		Category category2 = new Category("category2");
		Category category3 = new Category("category3");
		assertTrue(categoryDao.saveOrUpdate(category));
		assertTrue(categoryDao.saveOrUpdate(category2));
		assertTrue(categoryDao.saveOrUpdate(category3));

		Product product = new Product("product1", 10.0, new Category[] { category, category2 });
		assertTrue(productDao.saveOrUpdate(product));

		Product dbProduct = productDao.get(product.getId());
		assertEquals(2, dbProduct.getCategories().size());
		assertArrayEquals(getCategoryIds(Arrays.asList(category, category2)), getCategoryIds(dbProduct.getCategories()));

		// add category
		product.setCategories(new Category[] { category, category2, category3 });
		assertTrue(productDao.saveOrUpdate(product));

		dbProduct = productDao.get(product.getId());
		assertEquals(3, dbProduct.getCategories().size());
		assertArrayEquals(getCategoryIds(Arrays.asList(category, category2, category3)),
				getCategoryIds(dbProduct.getCategories()));

		// remove category
		product.setCategories(new Category[] { category });
		assertTrue(productDao.saveOrUpdate(product));

		dbProduct = productDao.get(product.getId());
		assertEquals(1, dbProduct.getCategories().size());
		assertEquals(category.getId(), dbProduct.getCategory(0).getId());

		// make no category
		product.setCategories();
		assertTrue(productDao.saveOrUpdate(product));

		dbProduct = productDao.get(product.getId());
		assertEquals(0, dbProduct.getCategories().size());
	}

	@Test
	public void testUpdateCategoryProducts() {
		Product product = new Product("product1", 10.0);
		Product product2 = new Product("product2", 10.0);
		Product product3 = new Product("product3", 10.0);
		assertTrue(productDao.saveOrUpdate(product));
		assertTrue(productDao.saveOrUpdate(product2));
		assertTrue(productDao.saveOrUpdate(product3));

		Category category = new Category("category1", new Product[] { product, product2 });
		assertTrue(categoryDao.saveOrUpdate(category));

		Category dbCategory = categoryDao.get(category.getId());
		assertEquals(2, dbCategory.getProducts().size());
		assertArrayEquals(getProductIds(Arrays.asList(product, product2)), getProductIds(dbCategory.getProducts()));

		// add product
		category.setProducts(new Product[] { product, product2, product3 });
		assertTrue(categoryDao.saveOrUpdate(category));

		dbCategory = categoryDao.get(category.getId());
		assertEquals(3, dbCategory.getProducts().size());
		assertArrayEquals(getProductIds(Arrays.asList(product, product2, product3)),
				getProductIds(dbCategory.getProducts()));

		// remove product
		category.setProducts(new Product[] { product });
		assertTrue(categoryDao.saveOrUpdate(category));

		dbCategory = categoryDao.get(category.getId());
		assertEquals(1, dbCategory.getProducts().size());
		assertEquals(product.getId(), dbCategory.getProduct(0).getId());

		// make no product
		category.setProducts();
		assertTrue(categoryDao.saveOrUpdate(category));

		dbCategory = categoryDao.get(category.getId());
		assertEquals(0, dbCategory.getProducts().size());
	}

	private void checkAll(Integer categoryId) {
		checkAll(categoryId, new ArrayList<Integer>());
	}

	private void checkAll(Integer categoryId, Integer productId) {
		checkAll(categoryId, Arrays.asList(productId));
	}

	private void checkAll(List<Integer> productIds) {
		Collections.sort(productIds);

		assertEquals(0, categoryDao.getAll().size());

		List<Product> dbProducts = productDao.getAll();
		assertEquals(productIds.size(), dbProducts.size());
		assertArrayEquals(productIds.toArray(), getProductIds(dbProducts));

		for (Integer productId : productIds) {
			Product dbProduct = productDao.get(productId);
			assertEquals(0, dbProduct.getCategories().size());
		}
	}

	private void checkAll(Integer categoryId, List<Integer> productIds) {
		Collections.sort(productIds);

		List<Category> dbCategories = categoryDao.getAll();
		assertEquals(1, dbCategories.size());
		assertEquals(categoryId, dbCategories.get(0).getId());

		List<Product> dbProducts = productDao.getAll();
		assertEquals(productIds.size(), dbProducts.size());
		assertArrayEquals(productIds.toArray(), getProductIds(dbProducts));

		Category dbCategory = categoryDao.get(categoryId);
		assertEquals(productIds.size(), dbCategory.getProducts().size());
		assertArrayEquals(productIds.toArray(), getProductIds(dbCategory.getProducts()));

		for (Integer productId : productIds) {
			Product dbProduct = productDao.get(productId);
			assertEquals(1, dbProduct.getCategories().size());
			assertEquals(categoryId, dbProduct.getCategory(0).getId());
		}
	}

}
