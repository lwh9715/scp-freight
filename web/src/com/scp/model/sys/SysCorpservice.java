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
@Table(name = "sys_corpservice")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysCorpservice implements java.io.Serializable {

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
	@Column(name = "servicestyle", nullable = false, length = 100)
	private java.lang.String servicestyle;

	/**
	 *@generated
	 */
	@Column(name = "servicetype", nullable = false, length = 1)
	private java.lang.String servicetype;

	/**
	 *@generated
	 */
	@Column(name = "corpcontactsid")
	private java.lang.Long corpcontactsid;

	/**
	 *@generated
	 */
	@Column(name = "timestart", length = 29)
	private java.util.Date timestart;

	/**
	 *@generated
	 */
	@Column(name = "timecost", length = 100)
	private java.lang.String timecost;

	/**
	 *@generated
	 */
	@Column(name = "servicestate", nullable = false, length = 1)
	private java.lang.String servicestate;

	/**
	 *@generated
	 */
	@Column(name = "servicecontent")
	private java.lang.String servicecontent;

	/**
	 *@generated
	 */
	@Column(name = "serviceresponse")
	private java.lang.String serviceresponse;

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
	public java.lang.String getServicestyle() {
		return this.servicestyle;
	}

	/**
	 *@generated
	 */
	public void setServicestyle(java.lang.String value) {
		this.servicestyle = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getServicetype() {
		return this.servicetype;
	}

	/**
	 *@generated
	 */
	public void setServicetype(java.lang.String value) {
		this.servicetype = value;
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
	public java.util.Date getTimestart() {
		return this.timestart;
	}

	/**
	 *@generated
	 */
	public void setTimestart(java.util.Date value) {
		this.timestart = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getTimecost() {
		return this.timecost;
	}

	/**
	 *@generated
	 */
	public void setTimecost(java.lang.String value) {
		this.timecost = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getServicestate() {
		return this.servicestate;
	}

	/**
	 *@generated
	 */
	public void setServicestate(java.lang.String value) {
		this.servicestate = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getServicecontent() {
		return this.servicecontent;
	}

	/**
	 *@generated
	 */
	public void setServicecontent(java.lang.String value) {
		this.servicecontent = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getServiceresponse() {
		return this.serviceresponse;
	}

	/**
	 *@generated
	 */
	public void setServiceresponse(java.lang.String value) {
		this.serviceresponse = value;
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