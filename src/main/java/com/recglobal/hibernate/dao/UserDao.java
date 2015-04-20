package com.recglobal.hibernate.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;

import com.recglobal.hibernate.model.User;

public class UserDao extends Dao {

	public boolean saveOrUpdate(User user) {
		try {
			begin();
			getSession().saveOrUpdate(user);
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

	public User get(Integer id) {
		try {
			begin();
			User user = (User) getSession().get(User.class, id);
			commit();
			return user;
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
			int deleted = getSession().createQuery("delete from User").executeUpdate();
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
	public List<User> getAll() {
		try {
			begin();
			List<User> users = getSession().createQuery("from User").list();
			commit();
			return users;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			rollback();
		} finally {
			close();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T extends User> List<T> getAll(Class<T> entityClass, int pageNumber, int rowsNumber, String propertyName) {
		try {
			begin();
			Criteria criteria = getSession().createCriteria(entityClass);

			criteria.setFirstResult((pageNumber - 1) * rowsNumber);
			criteria.setMaxResults(rowsNumber);

			criteria.addOrder(Order.asc(propertyName));

			List<T> users = criteria.list();
			commit();
			return users;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			rollback();
		} finally {
			close();
		}
		return null;
	}

}
