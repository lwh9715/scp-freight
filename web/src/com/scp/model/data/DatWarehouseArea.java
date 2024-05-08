package com.scp.model.data;

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
@Table(name = "dat_warehouse_area")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class DatWarehouseArea implements java.io.Serializable {

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
	@Column(name = "warehouseid")
	private java.lang.Long warehouseid;

	/**
	 *@generated
	 */
	@Column(name = "code", nullable = false, length = 20)
	private java.lang.String code;

	/**
	 *@generated
	 */
	@Column(name = "colcount")
	private java.lang.Short colcount;

	/**
	 *@generated
	 */
	@Column(name = "rowcount")
	private java.lang.Short rowcount;
	
	/**
	 *@generated
	 */
	@Column(name = "islock")
	private java.lang.Boolean islock;
	
	@Column(name = "isdamage")
	private java.lang.Boolean isdamage;
	
	@Column(name = "isplit")
	private java.lang.Boolean isplit;
	

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
	public java.lang.Long getWarehouseid() {
		return this.warehouseid;
	}

	/**
	 *@generated
	 */
	public void setWarehouseid(java.lang.Long value) {
		this.warehouseid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCode() {
		return this.code;
	}

	/**
	 *@generated
	 */
	public void setCode(java.lang.String value) {
		this.code = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getColcount() {
		return this.colcount;
	}

	/**
	 *@generated
	 */
	public void setColcount(java.lang.Short value) {
		this.colcount = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getRowcount() {
		return this.rowcount;
	}

	/**
	 *@generated
	 */
	public void setRowcount(java.lang.Short value) {
		this.rowcount = value;
	}

	public java.lang.Boolean getIslock() {
		return islock;
	}

	public void setIslock(java.lang.Boolean islock) {
		this.islock = islock;
	}

	public java.lang.Boolean getIsdamage() {
		return isdamage;
	}

	public void setIsdamage(java.lang.Boolean isdamage) {
		this.isdamage = isdamage;
	}

	public java.lang.Boolean getIsplit() {
		return isplit;
	}

	public void setIsplit(java.lang.Boolean isplit) {
		this.isplit = isplit;
	}


	
}