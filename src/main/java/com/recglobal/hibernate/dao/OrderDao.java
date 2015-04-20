package com.recglobal.hibernate.dao;

import static com.recglobal.hibernate.model.Order.ORDERS_BY_CATEGORY;
import static com.recglobal.hibernate.model.Order.ORDERS_BY_CREATE_DATE;
import static com.recglobal.hibernate.model.Order.ORDERS_BY_PRODUCT;
import static com.recglobal.hibernate.model.Order.ORDERS_BY_STATUS;
import static com.recglobal.hibernate.model.Order.TOP_CATEGORIES;
import static com.recglobal.hibernate.model.Order.TOP_CORPORATION_USERS;
import static com.recglobal.hibernate.model.Order.TOP_INDIVIDUAL_USERS;
import static com.recglobal.hibernate.model.Order.TOP_PRODUCTS;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.recglobal.hibernate.model.Order;
import com.recglobal.hibernate.model.OrderStatus;
import com.recglobal.hibernate.model.ProductOrder;

public class OrderDao extends Dao {

	public boolean saveOrUpdate(Order... orders) {
		try {
			begin();
			for (Order order : orders) {
				getSession().saveOrUpdate(order);
			}
			commit();
			return true;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			rollback();
		} finally {
			close();
		}
		return false;
	}

	public Order get(Integer id) {
		try {
			begin();
			Order order = (Order) getSession().get(Order.class, id);
			if (order != null) {
				Set<ProductOrder> products = order.getProducts();
				for (ProductOrder product : products) {
					product.getCategories().size();
				}
			}
			commit();
			return order;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			rollback();
		} finally {
			close();
		}
		return null;
	}

	public int deleteAll() {
		try {
			begin();
			int deleted = getSession().createQuery("delete from Order").executeUpdate();
			commit();
			return deleted;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			rollback();
		} finally {
			close();
		}
		return -1;
	}

	@SuppressWarnings("unchecked")
	public List<Order> getAll() {
		try {
			begin();
			List<Order> orders = getSession().createQuery("from Order").list();
			commit();
			return orders;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			rollback();
		} finally {
			close();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Order> getAll(Class<?> entityClass, List<Integer> userIds, List<Integer> orderIds, Integer pageNumber,
			int rowsNumber, String propertyName) {
		try {
			begin();
			Criteria oCriteria = getSession().createCriteria(Order.class, "o");
			if (orderIds != null) {
				oCriteria.add(Restrictions.in("o.id", orderIds));
			}

			Criteria pCriteria = oCriteria.createCriteria("o.payment", "p");
			pCriteria.add(Restrictions.eq("p.class", entityClass));

			if (userIds != null) {
				Criteria uCriteria = oCriteria.createCriteria("o.user", "u");
				uCriteria.add(Restrictions.in("u.id", userIds));
			}

			if (pageNumber != null) {
				oCriteria.setFirstResult((pageNumber - 1) * rowsNumber);
				oCriteria.setMaxResults(rowsNumber);
			}

			oCriteria.addOrder(org.hibernate.criterion.Order.asc(propertyName));

			List<Order> orders = oCriteria.list();
			for (Order order : orders) {
				Set<ProductOrder> products = order.getProducts();
				for (ProductOrder product : products) {
					product.getCategories().size();
				}
			}
			commit();
			return orders;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			rollback();
		} finally {
			close();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private List getTop(String name) {
		try {
			begin();
			Query query = getSession().getNamedQuery(name);
			query.setComment(name);
			query.setMaxResults(3);
			List rows = query.list();
			commit();
			return rows;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			rollback();
		} finally {
			close();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public List getTopCategories() {
		return getTop(TOP_CATEGORIES);
	}

	@SuppressWarnings("rawtypes")
	public List getTopProducts() {
		return getTop(TOP_PRODUCTS);
	}

	@SuppressWarnings("rawtypes")
	public List getTopIndividualUsers() {
		return getTop(TOP_INDIVIDUAL_USERS);
	}

	@SuppressWarnings("rawtypes")
	public List getTopCorporationUsers() {
		return getTop(TOP_CORPORATION_USERS);
	}

	@SuppressWarnings("rawtypes")
	public List getByCategory(String categoryName) {
		try {
			begin();
			Query query = getSession().getNamedQuery(ORDERS_BY_CATEGORY);
			query.setComment(ORDERS_BY_CATEGORY);
			query.setString("name", categoryName);
			List rows = query.list();
			commit();
			return rows;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			rollback();
		} finally {
			close();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public List getByProduct(String productName) {
		try {
			begin();
			Query query = getSession().getNamedQuery(ORDERS_BY_PRODUCT);
			query.setComment(ORDERS_BY_PRODUCT);
			query.setString("name", productName);
			List rows = query.list();
			commit();
			return rows;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			rollback();
		} finally {
			close();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public List getByDate(Date startDate, Date endDate) {
		try {
			begin();
			Query query = getSession().getNamedQuery(ORDERS_BY_CREATE_DATE);
			query.setComment(ORDERS_BY_CREATE_DATE);
			query.setTimestamp("startDate", startDate);
			query.setTimestamp("endDate", endDate);
			List rows = query.list();
			commit();
			return rows;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			rollback();
		} finally {
			close();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public List getByStatus(OrderStatus... statuses) {
		try {
			begin();
			Query query = getSession().getNamedQuery(ORDERS_BY_STATUS);
			query.setComment(ORDERS_BY_STATUS);
			query.setParameterList("statuses", Arrays.asList(statuses));
			List rows = query.list();
			commit();
			return rows;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			rollback();
		} finally {
			close();
		}
		return null;
	}

}
