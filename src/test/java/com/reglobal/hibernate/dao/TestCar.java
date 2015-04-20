package com.reglobal.hibernate.dao;

import static com.recglobal.hibernate.dao.Dao.close;
import static com.recglobal.hibernate.dao.Dao.getSession;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.junit.Test;

import com.recglobal.hibernate.model.Car;

public class TestCar {

	@Test
	public void testSave() {
		try {
			getSession().beginTransaction();
			getSession().save(new Car());
			getSession().getTransaction().commit();
		} catch (HibernateException ex) {
			Transaction tx = getSession().getTransaction();
			if (tx.isActive()) {
				tx.rollback();
			}
		} finally {
			close();
		}
	}

}
