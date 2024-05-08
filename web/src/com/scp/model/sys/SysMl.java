package com.scp.model.sys;

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
@Table(name = "sys_ml")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysMl implements java.io.Serializable {

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
	@Column(name = "ch")
	private java.lang.String ch;

	/**
	 *@generated
	 */
	@Column(name = "en")
	private java.lang.String en;

	/**
	 *@generated
	 */
	@Column(name = "tw")
	private java.lang.String tw;

	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

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
	public java.lang.String getCh() {
		return this.ch;
	}

	/**
	 *@generated
	 */
	public void setCh(java.lang.String value) {
		this.ch = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getEn() {
		return this.en;
	}

	/**
	 *@generated
	 */
	public void setEn(java.lang.String value) {
		this.en = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getTw() {
		return this.tw;
	}

	/**
	 *@generated
	 */
	public void setTw(java.lang.String value) {
		this.tw = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIsdelete() {
		return this.isdelete;
	}

	/**
	 *@generated
	 */
	public void setIsdelete(java.lang.Boolean value) {
		this.isdelete = value;
	}
}