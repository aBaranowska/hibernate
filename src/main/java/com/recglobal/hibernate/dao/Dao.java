package com.recglobal.hibernate.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.recglobal.hibernate.interceptor.MyInterceptor;

public class Dao {

	static {
		try {
			Configuration configuration = new Configuration().configure();
			StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
					configuration.getProperties()).build();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	private static final SessionFactory sessionFactory;

	private static final ThreadLocal<Session> var = new ThreadLocal<Session>();

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static Session getSession() {
		Session session = var.get();
		if (session == null) {
			session = sessionFactory.withOptions().interceptor(new MyInterceptor()).openSession();
			var.set(session);
		}
		return session;
	}

	protected void begin() {
		getSession().beginTransaction();
	}

	protected void commit() {
		getSession().getTransaction().commit();
	}

	protected void rollback() {
		try {
			getSession().getTransaction().rollback();
		} catch (HibernateException ex) {
			ex.printStackTrace();
		}
	}

	public static void close() {
		try {
			getSession().close();
		} catch (HibernateException ex) {
			ex.printStackTrace();
		}
		var.set(null);
	}

}
