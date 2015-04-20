package com.recglobal.hibernate.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.recglobal.hibernate.model.Product;

public class ProductDao extends Dao {

	public boolean saveOrUpdate(Product product) {
		try {
			begin();
			getSession().saveOrUpdate(product);
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

	public boolean delete(Product product) {
		try {
			begin();
			getSession().delete(product);
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

	public Product get(Integer id) {
		try {
			begin();
			Product product = (Product) getSession().get(Product.class, id);
			if (product != null) {
				product.getCategories().size();
			}
			commit();
			return product;
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
			int orderDeleted = getSession().createQuery("delete from Product where product != null").executeUpdate();
			int deleted = getSession().createQuery("delete from Product").executeUpdate();
			commit();
			return orderDeleted + deleted;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			rollback();
		} finally {
			close();
		}
		return -1;
	}

	@SuppressWarnings("unchecked")
	public List<Product> getAll() {
		try {
			begin();
			List<Product> products = getSession().createQuery("from Product").list();
			commit();
			return products;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			rollback();
		} finally {
			close();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Product> getAll(List<Integer> categoryIds, int pageNumber, int rowsNumber, String propertyName) {
		try {
			begin();
			Criteria pCriteria = getSession().createCriteria(Product.class, "p");
			pCriteria.add(Restrictions.eq("p.class", Product.class));

			if (categoryIds != null) {
				DetachedCriteria dc = DetachedCriteria.forClass(Product.class);
				dc.setProjection(Property.forName("id"));
				dc.createAlias("categories", "pc");
				dc.add(Restrictions.in("pc.pk.category.id", categoryIds));

				pCriteria.add(Property.forName("p.id").in(dc));
			}

			pCriteria.setFirstResult((pageNumber - 1) * rowsNumber);
			pCriteria.setMaxResults(rowsNumber);

			pCriteria.addOrder(Order.asc(propertyName));

			List<Product> products = pCriteria.list();
			for (Product product : products) {
				product.getCategories().size();
			}
			commit();
			return products;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			rollback();
		} finally {
			close();
		}
		return null;
	}

}
