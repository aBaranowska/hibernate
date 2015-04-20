package com.recglobal.hibernate.main;

import static com.recglobal.hibernate.dao.Dao.close;
import static com.recglobal.hibernate.dao.Dao.getSession;
import static com.recglobal.hibernate.dao.Dao.getSessionFactory;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import com.recglobal.hibernate.model.Category;

public class EHCacheMain {

	public static final Integer CATEGORY_ID = 48;

	public static void print() {
		System.out.println("getEntityFetchCount=" + getSessionFactory().getStatistics().getEntityFetchCount());
		System.out.println("getSecondLevelCacheHitCount="
				+ getSessionFactory().getStatistics().getSecondLevelCacheHitCount());
		System.out.println("getSecondLevelCacheMissCount="
				+ getSessionFactory().getStatistics().getSecondLevelCacheMissCount());
		System.out.println("getSecondLevelCachePutCount="
				+ getSessionFactory().getStatistics().getSecondLevelCachePutCount());
	}

	public static void main(String[] args) {
		getSessionFactory().getStatistics().setStatisticsEnabled(true);

		try {
			getSession().beginTransaction();

			print();

			Category category = (Category) getSession().load(Category.class, CATEGORY_ID);
			System.out.println(category.getName());

			print();

			category = (Category) getSession().load(Category.class, CATEGORY_ID);
			System.out.println(category.getName());

			print();

			getSession().evict(category);

			category = (Category) getSession().load(Category.class, CATEGORY_ID);
			System.out.println(category.getName());

			print();

			getSession().getTransaction().commit();
		} catch (HibernateException ex) {
			Transaction tx = getSession().getTransaction();
			if (tx.isActive()) {
				tx.rollback();
			}
		} finally {
			close();
		}

		try {
			getSession().beginTransaction();

			Category category = (Category) getSession().load(Category.class, CATEGORY_ID);
			System.out.println(category.getName());

			print();

			getSession().getTransaction().commit();
		} catch (HibernateException ex) {
			Transaction tx = getSession().getTransaction();
			if (tx.isActive()) {
				tx.rollback();
			}
		} finally {
			close();
		}

		getSessionFactory().close();
	}

}
