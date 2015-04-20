package com.recglobal.hibernate.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Class created only to show support for polymorphic relationships
 */
@Entity
@Table(name = "service_stock")
public class ServiceStock extends Stock {

}
