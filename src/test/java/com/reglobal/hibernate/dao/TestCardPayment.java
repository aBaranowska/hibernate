package com.reglobal.hibernate.dao;

import static com.reglobal.hibernate.dao.TestUtil.cleanup;
import static com.reglobal.hibernate.dao.TestUtil.paymentDao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.recglobal.hibernate.model.CardPayment;
import com.recglobal.hibernate.model.Payment;
import com.recglobal.hibernate.model.PaymentStatus;

public class TestCardPayment {

	@Before
	public void init() {
		cleanup();
	}

	@Test
	public void testSave() {
		CardPayment payment = new CardPayment("type1");
		// save to database
		assertTrue(paymentDao.saveOrUpdate(payment));

		// get from database
		List<Payment> dbPayments = paymentDao.getAll();
		assertEquals(1, dbPayments.size());
		assertEquals(payment.getId(), dbPayments.get(0).getId());
	}

	@Test
	public void testGet() {
		CardPayment payment = new CardPayment("type1");
		// save to database
		assertTrue(paymentDao.saveOrUpdate(payment));

		// get from database
		List<Payment> dbPayments = paymentDao.getAll();
		assertEquals(1, dbPayments.size());
		assertEquals(payment.getId(), dbPayments.get(0).getId());

		CardPayment dbPayment = (CardPayment) paymentDao.get(payment.getId());
		assertEquals(payment.getId(), dbPayment.getId());
		assertEquals("type1", dbPayment.getCardType());
		assertEquals(PaymentStatus.UNKNOWN, dbPayment.getStatus());
	}

}
