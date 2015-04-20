package com.recglobal.hibernate.model;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@NamedQueries({ @NamedQuery(name = Order.TOP_CATEGORIES, query = Order.TOP_CATEGORIES_QUERY),
		@NamedQuery(name = Order.TOP_PRODUCTS, query = Order.TOP_PRODUCTS_QUERY),
		@NamedQuery(name = Order.TOP_INDIVIDUAL_USERS, query = Order.TOP_INDIVIDUAL_USERS_QUERY),
		@NamedQuery(name = Order.TOP_CORPORATION_USERS, query = Order.TOP_CORPORATION_USERS_QUERY),
		@NamedQuery(name = Order.ORDERS_BY_CATEGORY, query = Order.ORDERS_BY_CATEGORY_QUERY),
		@NamedQuery(name = Order.ORDERS_BY_PRODUCT, query = Order.ORDERS_BY_PRODUCT_QUERY),
		@NamedQuery(name = Order.ORDERS_BY_CREATE_DATE, query = Order.ORDERS_BY_CREATE_DATE_QUERY),
		@NamedQuery(name = Order.ORDERS_BY_STATUS, query = Order.ORDERS_BY_STATUS_QUERY) })
@Entity
// 'user_order' because cannot call table 'order'
@Table(name = "user_order")
public class Order {

	public static final String TOP_CATEGORIES = "TOP_CATEGORIES";
	public static final String TOP_PRODUCTS = "TOP_PRODUCTS";
	public static final String TOP_INDIVIDUAL_USERS = "TOP_INDIVIDUAL_USERS";
	public static final String TOP_CORPORATION_USERS = "TOP_CORPORATION_USERS";
	public static final String ORDERS_BY_CATEGORY = "ORDERS_BY_CATEGORY";
	public static final String ORDERS_BY_PRODUCT = "ORDERS_BY_PRODUCT";
	public static final String ORDERS_BY_CREATE_DATE = "ORDERS_BY_CREATE_DATE";
	public static final String ORDERS_BY_STATUS = "ORDERS_BY_STATUS";
	//@formatter:off
	public static final String TOP_CATEGORIES_QUERY = ""
			+ "SELECT     count(o.id) AS n, "
			+ "           c.name "
			+ "FROM       Order o "
			+ "INNER JOIN o.products po "
			+ "INNER JOIN po.product p "
			+ "INNER JOIN p.categories pc "
			+ "INNER JOIN pc.pk.category c "
			+ "GROUP BY   c.id "
			+ "ORDER BY   n DESC";
	public static final String TOP_PRODUCTS_QUERY = ""
			+ "SELECT     count(o.id) AS n, "
			+ "           p.name, "
			+ "           c.name "
			+ "FROM       Order o "
			+ "INNER JOIN o.products po "
			+ "INNER JOIN po.product p "
			+ "INNER JOIN p.categories pc "
			+ "INNER JOIN pc.pk.category c "
			+ "GROUP BY   p.id "
			+ "ORDER BY   n DESC";
	public static final String TOP_INDIVIDUAL_USERS_QUERY = ""
			+ "SELECT     count(o.id) AS n, "
			+ "           u.firstName, "
			+ "           u.lastName "
			+ "FROM       Order o "
			+ "INNER JOIN o.user u "
			+ "WHERE      u.class = IndividualUser "
			+ "GROUP BY   u.id "
			+ "ORDER BY   n DESC";
	public static final String TOP_CORPORATION_USERS_QUERY = ""
			+ "SELECT     count(o.id) AS n, "
			+ "           u.companyName "
			+ "FROM       Order o "
			+ "INNER JOIN o.user u "
			+ "WHERE      u.class = CorporationUser "
			+ "GROUP BY   u.id "
			+ "ORDER BY   n DESC";
	public static final String ORDERS_BY_CATEGORY_QUERY = ""
			+ "SELECT     distinct o.id "
			+ "FROM       Order o "
			+ "INNER JOIN o.payment pa "
			+ "INNER JOIN o.products po "
			+ "INNER JOIN po.product pr "
			+ "INNER JOIN pr.categories pc "
			+ "INNER JOIN pc.pk.category c "
			+ "WHERE      c.name = :name "
			+ "ORDER BY   pa.class, o.id";
	public static final String ORDERS_BY_PRODUCT_QUERY = ""
			+ "SELECT     o.id "
			+ "FROM       Order o "
			+ "INNER JOIN o.payment pa "
			+ "INNER JOIN o.products po "
			+ "INNER JOIN po.product pr "
			+ "WHERE      pr.name LIKE :name "
			+ "ORDER BY   pa.class, o.id";
	public static final String ORDERS_BY_CREATE_DATE_QUERY = ""
			+ "SELECT     o.id "
			+ "FROM       Order o "
			+ "INNER JOIN o.payment p "
			+ "WHERE      o.createDate BETWEEN :startDate AND :endDate "
			+ "ORDER BY   p.class, o.id";
	public static final String ORDERS_BY_STATUS_QUERY = ""
			+ "SELECT     o.id "
			+ "FROM       Order o "
			+ "INNER JOIN o.payment p "
			+ "WHERE      o.status IN (:statuses) "
			+ "ORDER BY   p.class, o.id";
	
	//@formatter:on

	@Id
	@GeneratedValue
	@Column(name = "order_id")
	private Integer id;

	@Temporal(TemporalType.DATE)
	@Column(name = "create_date", nullable = false)
	private Date createDate;

	@Column(name = "status", length = 15, nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "fk_user_id", nullable = false)
	private User user;

	@OneToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "fk_payment_id", nullable = false)
	private Payment payment;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
	private Set<ProductOrder> products;

	public Order() {

	}

	public Order(User user, Payment payment, ProductOrder... products) {
		createDate = Calendar.getInstance().getTime();
		status = OrderStatus.NOT_PAID;
		this.products = new HashSet<ProductOrder>();
		for (ProductOrder product : products) {
			addProduct(product);
		}
		this.user = user;
		this.payment = payment;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Set<ProductOrder> getProducts() {
		return products;
	}

	public void setProducts(Set<ProductOrder> products) {
		this.products = products;
	}

	public ProductOrder addProduct(ProductOrder newProduct) {
		ProductOrder oldProduct = findParentProduct(newProduct.getProduct().getId());
		if (oldProduct != null) {
			int oldQuantity = oldProduct.getQuantity();
			int newQuantity = oldQuantity + newProduct.getQuantity();
			oldProduct.setQuantity(newQuantity);
			return oldProduct;
		} else {
			newProduct.setOrder(this);
			products.add(newProduct);
			return newProduct;
		}
	}

	public boolean removeProduct(ProductOrder product) {
		ProductOrder oldProduct = findProduct(product.getId());
		if (oldProduct != null) {
			return products.remove(oldProduct);
		}
		return false;
	}

	public ProductOrder findParentProduct(Integer id) {
		if (id != null) {
			for (ProductOrder product : products) {
				if (product.getProduct().getId().equals(id)) {
					return product;
				}
			}
		}
		return null;
	}

	public ProductOrder findProduct(Integer id) {
		if (id != null) {
			for (ProductOrder product : products) {
				if (product.getId().equals(id)) {
					return product;
				}
			}
		}
		return null;
	}

	public ProductOrder getProduct(int index) {
		Iterator<ProductOrder> it = products.iterator();
		int i = 0;
		while (it.hasNext()) {
			ProductOrder product = it.next();
			if (i == index) {
				return product;
			}
			i++;
		}
		return null;
	}

}
