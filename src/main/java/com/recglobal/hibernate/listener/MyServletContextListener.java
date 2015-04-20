package com.recglobal.hibernate.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.recglobal.hibernate.dao.Dao;

public class MyServletContextListener implements ServletContextListener {

	private static final Logger log = Logger.getLogger(MyServletContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		log.info("contextInitialized");

		// to create/update schema
		Dao.getSessionFactory();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		log.info("contextDestroyed");

		Dao.getSessionFactory().close();
	}

}
