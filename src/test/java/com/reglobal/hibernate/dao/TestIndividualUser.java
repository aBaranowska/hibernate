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
import com.recglobal.hibernate.model.IndividualUser;
import com.recglobal.hibernate.model.User;

public class TestIndividualUser {

	@Before
	public void init() {
		cleanup();
	}

	@Test
	public void testSave() {
		IndividualUser user = new IndividualUser("firstname1", "lastname1");
		// save to database
		assertTrue(userDao.saveOrUpdate(user));

		// get from database
		List<User> dbUser = userDao.getAll();
		assertEquals(1, dbUser.size());
		assertEquals(user.getId(), dbUser.get(0).getId());

		IndividualUser user2 = new IndividualUser("firstname1", "lastname1");
		assertFalse(userDao.saveOrUpdate(user2));
	}

	@Test
	public void testGet() {
		IndividualUser user = new IndividualUser("firstname1", "lastname1");
		user.setEmail("email1");
		user.setAddress(new Address("city1"));
		// save to database
		assertTrue(userDao.saveOrUpdate(user));

		// get from database
		List<User> dbUsers = userDao.getAll();
		assertEquals(1, dbUsers.size());
		assertEquals(user.getId(), dbUsers.get(0).getId());

		IndividualUser dbUser = (IndividualUser) userDao.get(user.getId());
		assertEquals(user.getId(), dbUser.getId());
		assertEquals("firstname1", dbUser.getFirstName());
		assertEquals("lastname1", dbUser.getLastName());
		assertEquals("email1", dbUser.getEmail());
		assertEquals("city1", dbUser.getAddress().getCity());
	}

}
