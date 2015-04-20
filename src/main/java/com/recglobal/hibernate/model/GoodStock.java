package com.recglobal.hibernate.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Class created only to show support for polymorphic relationships
 */
@Entity
@Table(name = "good_stock")
public class GoodStock extends Stock {

}
