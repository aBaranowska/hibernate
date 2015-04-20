package com.recglobal.hibernate.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.recglobal.hibernate.model.Payment;

public class PaymentDao extends Dao {

	public boolean saveOrUpdate(Payment payment) {
		try {
			begin();
			getSession().saveOrUpdate(payment);
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

	public Payment get(Integer id) {
		try {
			begin();
			Payment payment = (Payment) getSession().get(Payment.class, id);
			commit();
			return payment;
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
			int deleted = getSession().createQuery("delete from Payment").executeUpdate();
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
	public List<Payment> getAll() {
		try {
			begin();
			List<Payment> payments = getSession().createQuery("from Payment").list();
			commit();
			return payments;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			rollback();
		} finally {
			close();
		}
		return null;
	}

}
