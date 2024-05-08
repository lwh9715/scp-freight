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
@Table(name = "sys_corpcare")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysCorpcare implements java.io.Serializable {

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
	@Column(name = "customerid")
	private java.lang.Long customerid;

	/**
	 *@generated
	 */
	@Column(name = "customerabbr", length = 100)
	private java.lang.String customerabbr;

	/**
	 *@generated
	 */
	@Column(name = "caretopic", nullable = false, length = 100)
	private java.lang.String caretopic;

	/**
	 *@generated
	 */
	@Column(name = "corpcontactsid")
	private java.lang.Long corpcontactsid;

	/**
	 *@generated
	 */
	@Column(name = "caretime", length = 29)
	private java.util.Date caretime;

	/**
	 *@generated
	 */
	@Column(name = "carecontent")
	private java.lang.String carecontent;

	/**
	 *@generated
	 */
	@Column(name = "careresponse")
	private java.lang.String careresponse;

	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String remarks;

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
	public java.lang.Long getCustomerid() {
		return this.customerid;
	}

	/**
	 *@generated
	 */
	public void setCustomerid(java.lang.Long value) {
		this.customerid = value;
	}

	

	/**
	 *@generated
	 */
	public java.lang.String getCaretopic() {
		return this.caretopic;
	}

	/**
	 *@generated
	 */
	public void setCaretopic(java.lang.String value) {
		this.caretopic = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getCorpcontactsid() {
		return this.corpcontactsid;
	}

	/**
	 *@generated
	 */
	public void setCorpcontactsid(java.lang.Long value) {
		this.corpcontactsid = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getCaretime() {
		return this.caretime;
	}

	/**
	 *@generated
	 */
	public void setCaretime(java.util.Date value) {
		this.caretime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCarecontent() {
		return this.carecontent;
	}

	/**
	 *@generated
	 */
	public void setCarecontent(java.lang.String value) {
		this.carecontent = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCareresponse() {
		return this.careresponse;
	}

	/**
	 *@generated
	 */
	public void setCareresponse(java.lang.String value) {
		this.careresponse = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getRemarks() {
		return this.remarks;
	}

	/**
	 *@generated
	 */
	public void setRemarks(java.lang.String value) {
		this.remarks = value;
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

	public java.lang.String getCustomerabbr() {
		return customerabbr;
	}

	public void setCustomerabbr(java.lang.String customerabbr) {
		this.customerabbr = customerabbr;
	}
}