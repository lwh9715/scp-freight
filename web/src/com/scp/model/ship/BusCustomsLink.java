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
@Table(name = "bus_customs_link")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusCustomsLink implements java.io.Serializable {

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
	@Column(name = "customsid")
	private java.lang.Long customsid;

	/**
	 *@generated
	 */
	@Column(name = "containerid")
	private java.lang.Long containerid;

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
	public java.lang.Long getCustomsid() {
		return this.customsid;
	}

	/**
	 *@generated
	 */
	public void setCustomsid(java.lang.Long value) {
		this.customsid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getContainerid() {
		return this.containerid;
	}

	/**
	 *@generated
	 */
	public void setContainerid(java.lang.Long value) {
		this.containerid = value;
	}
}