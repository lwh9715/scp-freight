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
@Table(name = "bus_shipjoinlink")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusShipjoinlink implements java.io.Serializable {

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
	@Column(name = "shipid")
	private java.lang.Long shipid;

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
	public java.lang.Long getShipid() {
		return this.shipid;
	}

	/**
	 *@generated
	 */
	public void setShipid(java.lang.Long value) {
		this.shipid = value;
	}
}