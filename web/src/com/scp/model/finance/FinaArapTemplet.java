package com.scp.model.finance;

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
@Table(name = "fina_arap_templet")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class FinaArapTemplet implements java.io.Serializable {

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
	@Column(name = "namec", length = 100)
	private java.lang.String namec;

	/**
	 *@generated
	 */
	@Column(name = "namee", length = 100)
	private java.lang.String namee;

	/**
	 *@generated
	 */
	@Column(name = "isprivate")
	private java.lang.Boolean isprivate;

	/**
	 *@generated
	 */
	@Column(name = "iswarehouse")
	private java.lang.Boolean iswarehouse;

	/**
	 *@generated
	 */
	@Column(name = "isshipping")
	private java.lang.Boolean isshipping;

	/**
	 *@generated
	 */
	@Column(name = "isair")
	private java.lang.Boolean isair;

	/**
	 *@generated
	 */
	@Column(name = "istruck")
	private java.lang.Boolean istruck;

	/**
	 *@generated
	 */
	@Column(name = "ispublic")
	private java.lang.Boolean ispublic;

	/**
	 *@generated
	 */
	@Column(name = "corpid")
	private java.lang.Long corpid;

	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

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
	public java.lang.Boolean getIsprivate() {
		return this.isprivate;
	}

	/**
	 *@generated
	 */
	public void setIsprivate(java.lang.Boolean value) {
		this.isprivate = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIswarehouse() {
		return this.iswarehouse;
	}

	/**
	 *@generated
	 */
	public void setIswarehouse(java.lang.Boolean value) {
		this.iswarehouse = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIsshipping() {
		return this.isshipping;
	}

	/**
	 *@generated
	 */
	public void setIsshipping(java.lang.Boolean value) {
		this.isshipping = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIsair() {
		return this.isair;
	}

	/**
	 *@generated
	 */
	public void setIsair(java.lang.Boolean value) {
		this.isair = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIstruck() {
		return this.istruck;
	}

	/**
	 *@generated
	 */
	public void setIstruck(java.lang.Boolean value) {
		this.istruck = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIspublic() {
		return this.ispublic;
	}

	/**
	 *@generated
	 */
	public void setIspublic(java.lang.Boolean value) {
		this.ispublic = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getCorpid() {
		return this.corpid;
	}

	/**
	 *@generated
	 */
	public void setCorpid(java.lang.Long value) {
		this.corpid = value;
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
}