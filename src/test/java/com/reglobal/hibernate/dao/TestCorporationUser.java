package com.reglobal.hibernate.dao;

import static com.reglobal.hibernate.dao.TestUtil.cleanup;
import static com.reglobal.hibernate.dao.TestUtil.userDao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.recglobal.hibernate.model.Address;
import com.recglobal.hibernate.model.CorporationUser;
import com.recglobal.hibernate.model.User;

public class TestCorporationUser {

	@Before
	public void init() {
		cleanup();
	}

	@Test
	public void testSave() {
		CorporationUser user = new CorporationUser("name1");
		// save to database
		assertTrue(userDao.saveOrUpdate(user));

		// get from database
		List<User> dbUsers = userDao.getAll();
		assertEquals(1, dbUsers.size());
		assertEquals(user.getId(), dbUsers.get(0).getId());

		CorporationUser user2 = new CorporationUser("name1");
		assertFalse(userDao.saveOrUpdate(user2));
	}

	@Test
	public void testGet() {
		CorporationUser user = new CorporationUser("name1");
		user.setEmail("email1");
		user.setAddress(new Address("city1"));
		// save to database
		assertTrue(userDao.saveOrUpdate(user));

		// get from database
		List<User> dbUsers = userDao.getAll();
		assertEquals(1, dbUsers.size());
		assertEquals(user.getId(), dbUsers.get(0).getId());

		CorporationUser dbUser = (CorporationUser) userDao.get(user.getId());
		assertEquals(user.getId(), dbUser.getId());
		assertEquals("name1", dbUser.getCompanyName());
		assertEquals("email1", dbUser.getEmail());
		assertEquals("city1", dbUser.getAddress().getCity());
	}

}
