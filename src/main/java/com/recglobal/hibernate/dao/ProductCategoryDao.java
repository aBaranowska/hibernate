package com.recglobal.hibernate.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.recglobal.hibernate.model.ProductCategory;

public class ProductCategoryDao extends Dao {

	public int deleteAll() {
		try {
			begin();
			int deleted = getSession().createQuery("delete from ProductCategory").executeUpdate();
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
	public List<ProductCategory> getAll() {
		try {
			begin();
			List<ProductCategory> rows = getSession().createQuery("from ProductCategory").list();
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
