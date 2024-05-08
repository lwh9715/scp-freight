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
@Table(name = "dat_country")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class DatCountry implements java.io.Serializable {

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
	@Column(name = "code", length = 20)
	private java.lang.String code;

	/**
	 *@generated
	 */
	@Column(name = "namec", length = 50)
	private java.lang.String namec;

	/**
	 *@generated
	 */
	@Column(name = "namee", length = 50)
	private java.lang.String namee;
	
	
	
	
	@Column(name = "telcode", length = 10)
	private java.lang.String telcode;
	
	
	@Column(name = "timezone", length = 10)
	private java.lang.String timezone;

	/**
	 *@generated
	 */
	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;

	/**
	 *@generated
	 */
	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;

	/**
	 *@generated
	 */
	@Column(name = "updater", length = 100)
	private java.lang.String updater;

	/**
	 *@generated
	 */
	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;

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
	public java.lang.String getNamec() {
		return this.namec;
	}

	/**
	 *@generated
	 */
	public void setNamec(java.lang.String value) {
		this.namec = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getNamee() {
		return this.namee;
	}

	/**
	 *@generated
	 */
	public void setNamee(java.lang.String value) {
		this.namee = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getInputer() {
		return this.inputer;
	}

	/**
	 *@generated
	 */
	public void setInputer(java.lang.String value) {
		this.inputer = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getInputtime() {
		return this.inputtime;
	}

	/**
	 *@generated
	 */
	public void setInputtime(java.util.Date value) {
		this.inputtime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getUpdater() {
		return this.updater;
	}

	/**
	 *@generated
	 */
	public void setUpdater(java.lang.String value) {
		this.updater = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getUpdatetime() {
		return this.updatetime;
	}

	/**
	 *@generated
	 */
	public void setUpdatetime(java.util.Date value) {
		this.updatetime = value;
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

	public java.lang.String getTelcode() {
		return telcode;
	}

	public void setTelcode(java.lang.String telcode) {
		this.telcode = telcode;
	}

	public java.lang.String getTimezone() {
		return timezone;
	}

	public void setTimezone(java.lang.String timezone) {
		this.timezone = timezone;
	}
	
	
}