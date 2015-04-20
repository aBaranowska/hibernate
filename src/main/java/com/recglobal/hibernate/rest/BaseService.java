package com.recglobal.hibernate.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.recglobal.hibernate.dao.CategoryDao;
import com.recglobal.hibernate.dao.OrderDao;
import com.recglobal.hibernate.dao.ProductDao;
import com.recglobal.hibernate.dao.UserDao;
import com.recglobal.hibernate.model.Category;
import com.recglobal.hibernate.model.ProductCategory;

public class BaseService {

	protected static final int USERS_PER_PAGE = 5;
	protected static final int CATEGORIES_PER_PAGE = 5;
	protected static final int PRODUCTS_PER_PAGE = 5;
	protected static final int ORDERS_PER_PAGE = 5;

	protected static final String LAST_NAME_USER_PROPERTY = "lastName";
	protected static final String COMPANY_NAME_USER_PROPERTY = "companyName";
	protected static final String NAME_CATEGORY_PROPERTY = "name";
	protected static final String NAME_PRODUCT_PROPERTY = "name";
	protected static final String CREATE_DATE_ORDER_PROPERTY = "createDate";

	protected static final int STATUS_OK = 200;
	protected static final int STATUS_ERROR = 800;

	protected static final UserDao userDao = new UserDao();
	protected static final CategoryDao categoryDao = new CategoryDao();
	protected static final ProductDao productDao = new ProductDao();
	protected static final OrderDao orderDao = new OrderDao();

	protected List<Integer> getCategoryIds(Set<ProductCategory> productCategories) {
		List<Integer> categoryIds = new ArrayList<Integer>();
		for (ProductCategory productCategory : productCategories) {
			Category category = productCategory.getPk().getCategory();
			categoryIds.add(category.getId());
		}
		return categoryIds;
	}

	protected int getPageNumber(Integer pageNumber) {
		if (pageNumber == null || pageNumber < 1) {
			pageNumber = 1;
		}
		return pageNumber;
	}

	protected List<Integer> getNumericIds(String strIds) {
		List<Integer> numIds = new ArrayList<Integer>();

		String[] sa = strIds.split(",");
		for (String s : sa) {
			try {
				Integer i = Integer.valueOf(s);
				numIds.add(i);
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
		}

		if (sa.length > 0 && sa.length == numIds.size()) {
			return numIds;
		}
		return null;
	}

}
