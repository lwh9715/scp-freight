package com.scp.model.ship;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *@generated
 */
@Table(name = "bus_shipjoingoods")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusShipjoingoods implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy")
	private long id;

	/**
	 *@generated
	 */
	@Column(name = "shipjoin")
	private java.lang.Long shipjoin;

	/**
	 *@generated
	 */
	@Column(name = "goodsname")
	private java.lang.String goodsname;

	/**
	 *@generated
	 */
	@Column(name = "hscode")
	private java.lang.String hscode;

	/**
	 *@generated
	 */
	public long getId() {
		return this.id;
	}

	/**
	 *@generated
	 */
	public void setId(long value) {
		this.id = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getShipjoin() {
		return this.shipjoin;
	}

	/**
	 *@generated
	 */
	public void setShipjoin(java.lang.Long value) {
		this.shipjoin = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getGoodsname() {
		return this.goodsname;
	}

	/**
	 *@generated
	 */
	public void setGoodsname(java.lang.String value) {
		this.goodsname = value;
	}

	/**
	 *@generated
	 */
	public String getHscode() {
		return hscode;
	}

	/**
	 *@generated
	 */
	public void setHscode(String hscode) {
		this.hscode = hscode;
	}
}