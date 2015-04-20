package com.recglobal.hibernate.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;

import com.recglobal.hibernate.model.Category;

public class CategoryDao extends Dao {

	public boolean saveOrUpdate(Category category) {
		try {
			begin();
			getSession().saveOrUpdate(category);
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

	public Category get(Integer id) {
		try {
			begin();
			Category category = (Category) getSession().get(Category.class, id);
			if (category != null) {
				category.getProducts().size();
			}
			commit();
			return category;
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
			int deleted = getSession().createQuery("delete from Category").executeUpdate();
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
	public List<Category> getAll() {
		try {
			begin();
			List<Category> categories = getSession().createQuery("from Category").list();
			commit();
			return categories;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			rollback();
		} finally {
			close();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Category> getAll(int pageNumber, int rowsNumber, String propertyName) {
		try {
			begin();
			Criteria criteria = getSession().createCriteria(Category.class);

			criteria.setFirstResult((pageNumber - 1) * rowsNumber);
			criteria.setMaxResults(rowsNumber);

			criteria.addOrder(Order.asc(propertyName));

			List<Category> categories = criteria.list();
			commit();
			return categories;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			rollback();
		} finally {
			close();
		}
		return null;
	}

}
