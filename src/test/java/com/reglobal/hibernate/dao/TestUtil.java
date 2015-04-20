package com.reglobal.hibernate.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.recglobal.hibernate.dao.CategoryDao;
import com.recglobal.hibernate.dao.OrderDao;
import com.recglobal.hibernate.dao.PaymentDao;
import com.recglobal.hibernate.dao.ProductCategoryDao;
import com.recglobal.hibernate.dao.ProductDao;
import com.recglobal.hibernate.dao.UserDao;
import com.recglobal.hibernate.model.Category;
import com.recglobal.hibernate.model.Product;
import com.recglobal.hibernate.model.ProductCategory;
import com.recglobal.hibernate.model.ProductOrder;

public class TestUtil {

	public static final UserDao userDao = new UserDao();
	public static final PaymentDao paymentDao = new PaymentDao();
	public static final ProductCategoryDao productCategoryDao = new ProductCategoryDao();
	public static final CategoryDao categoryDao = new CategoryDao();
	public static final ProductDao productDao = new ProductDao();
	public static final OrderDao orderDao = new OrderDao();

	public static void cleanup() {
		// in this order
		productCategoryDao.deleteAll();
		categoryDao.deleteAll();
		productDao.deleteAll();
		orderDao.deleteAll();
		userDao.deleteAll();
		paymentDao.deleteAll();

		assertEquals(0, userDao.getAll().size());
		assertEquals(0, paymentDao.getAll().size());
		assertEquals(0, productCategoryDao.getAll().size());
		assertEquals(0, categoryDao.getAll().size());
		assertEquals(0, productDao.getAll().size());
		assertEquals(0, orderDao.getAll().size());
	}

	public static <T> Object[] getProductIds(Collection<T> collection) {
		List<Integer> ids = new ArrayList<Integer>();
		for (T element : collection) {
			if (element instanceof Product) {
				ids.add(((Product) element).getId());
			} else if (element instanceof ProductOrder) {
				ids.add(((ProductOrder) element).getId());
			} else if (element instanceof ProductCategory) {
				ids.add(((ProductCategory) element).getPk().getProduct().getId());
			}
		}
		Collections.sort(ids);
		return ids.toArray();
	}

	public static <T> Object[] getCategoryIds(Collection<T> collection) {
		List<Integer> ids = new ArrayList<Integer>();
		for (T element : collection) {
			if (element instanceof Category) {
				ids.add(((Category) element).getId());
			} else if (element instanceof ProductCategory) {
				ids.add(((ProductCategory) element).getPk().getCategory().getId());
			}
		}
		Collections.sort(ids);
		return ids.toArray();
	}

	public static <T> Object[] getProductNames(Collection<T> collection) {
		List<String> names = new ArrayList<String>();
		for (T element : collection) {
			if (element instanceof ProductCategory) {
				names.add(((ProductCategory) element).getPk().getProduct().getName());
			}
		}
		Collections.sort(names);
		return names.toArray();
	}

}
