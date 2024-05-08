package com.scp.model.price;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Date:2016-6-20 FCL运价主表：price_fcl
 * 
 * @author bruce
 */
@Table(name = "price_homepage")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class PriceHomepage implements java.io.Serializable {
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;
	
	@Column(name = "pricetype", length = 1)
	private java.lang.String pricetype;

	@Column(name = "priceid")
	private java.lang.Long priceid;
	
	@Column(name = "cost20")
	private java.math.BigDecimal cost20;

	@Column(name = "cost40gp")
	private java.math.BigDecimal cost40gp;

	@Column(name = "cost40hq")
	private java.math.BigDecimal cost40hq;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getPricetype() {
		return pricetype;
	}

	public void setPricetype(java.lang.String pricetype) {
		this.pricetype = pricetype;
	}

	public java.lang.Long getPriceid() {
		return priceid;
	}

	public void setPriceid(java.lang.Long priceid) {
		this.priceid = priceid;
	}

	public java.math.BigDecimal getCost20() {
		return cost20;
	}

	public void setCost20(java.math.BigDecimal cost20) {
		this.cost20 = cost20;
	}

	public java.math.BigDecimal getCost40gp() {
		return cost40gp;
	}

	public void setCost40gp(java.math.BigDecimal cost40gp) {
		this.cost40gp = cost40gp;
	}

	public java.math.BigDecimal getCost40hq() {
		return cost40hq;
	}

	public void setCost40hq(java.math.BigDecimal cost40hq) {
		this.cost40hq = cost40hq;
	}

	
}
