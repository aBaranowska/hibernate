package com.recglobal.hibernate.interceptor;

import java.io.Serializable;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.recglobal.hibernate.model.Category;
import com.recglobal.hibernate.model.Product;
import com.recglobal.hibernate.model.ProductCategory;
import com.recglobal.hibernate.model.ProductOrder;

public class MyInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(MyInterceptor.class);

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		log.debug(entity.getClass().getName());
		if (entity instanceof Category) {
			Category category = (Category) entity;
			log.debug(category.getName());
			log.debug(category.getId());
		} else if (entity instanceof Product) {
			Product product = (Product) entity;
			log.debug(product.getName());
			log.debug(product.getId());
		} else if (entity instanceof ProductOrder) {
			ProductOrder productOrder = (ProductOrder) entity;
			log.debug(productOrder.getName());
			log.debug(productOrder.getId());
		} else if (entity instanceof ProductCategory) {
			ProductCategory productCategory = (ProductCategory) entity;
			log.debug(productCategory.getPk().getCategory().getName());
			log.debug(productCategory.getPk().getCategory().getId());
			log.debug(productCategory.getPk().getProduct().getName());
			log.debug(productCategory.getPk().getProduct().getId());
		}
		return super.onSave(entity, id, state, propertyNames, types);
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		log.debug(entity.getClass().getName());
		if (entity instanceof Category) {
			Category category = (Category) entity;
			log.debug(category.getName());
			log.debug(category.getId());
		} else if (entity instanceof Product) {
			Product product = (Product) entity;
			log.debug(product.getName());
			log.debug(product.getId());

			Set<ProductCategory> categories = product.getCategories();
			for (ProductCategory category : categories) {
				category.setProductName(product.getName());
			}
		} else if (entity instanceof ProductOrder) {
			ProductOrder productOrder = (ProductOrder) entity;
			log.debug(productOrder.getName());
			log.debug(productOrder.getId());
		} else if (entity instanceof ProductCategory) {
			ProductCategory productCategory = (ProductCategory) entity;
			log.debug(productCategory.getPk().getCategory().getName());
			log.debug(productCategory.getPk().getCategory().getId());
			log.debug(productCategory.getPk().getProduct().getName());
			log.debug(productCategory.getPk().getProduct().getId());
		}
		return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}

}
