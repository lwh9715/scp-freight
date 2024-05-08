package com.scp.model.wms;

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
@Table(name = "wms_dispatch")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class WmsDispatch implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "corpid")
	private Long corpid;

	public Long getCorpid() {
		return corpid;
	}

	public void setCorpid(Long corpid) {
		this.corpid = corpid;
	}
	/**
	 *@generated
	 */
	@Column(name = "nos")
	private java.lang.String nos;

	/**
	 *@generated
	 */
	@Column(name = "singtime", length = 29)
	private java.util.Date singtime;

	/**
	 *@generated
	 */
	@Column(name = "customerid")
	private java.lang.Long customerid;

	/**
	 *@generated
	 */
	@Column(name = "customerabbr", length = 50)
	private java.lang.String customerabbr;

	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	/**
	 *@generated
	 */
	@Column(name = "warehouseid")
	private java.lang.Long warehouseid;

	/**
	 *@generated
	 */
	@Column(name = "jobid")
	private java.lang.Long jobid;

	/**
	 *@generated
	 */
	@Column(name = "dispatchtype", nullable = false, length = 1)
	private java.lang.String dispatchtype;

	/**
	 *@generated
	 */
	@Column(name = "ischeck")
	private java.lang.Boolean ischeck;

	/**
	 *@generated
	 */
	@Column(name = "checkter", length = 30)
	private java.lang.String checkter;

	/**
	 *@generated
	 */
	@Column(name = "checktime", length = 29)
	private java.util.Date checktime;

	/**
	 *@generated
	 */
	@Column(name = "remark")
	private java.lang.String remark;

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

	

	public java.lang.String getNos() {
		return nos;
	}

	public void setNos(java.lang.String nos) {
		this.nos = nos;
	}

	/**
	 *@generated
	 */
	public java.util.Date getSingtime() {
		return this.singtime;
	}

	/**
	 *@generated
	 */
	public void setSingtime(java.util.Date value) {
		this.singtime = value;
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
	public java.lang.String getCustomerabbr() {
		return this.customerabbr;
	}

	/**
	 *@generated
	 */
	public void setCustomerabbr(java.lang.String value) {
		this.customerabbr = value;
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
	public java.lang.Long getJobid() {
		return this.jobid;
	}

	/**
	 *@generated
	 */
	public void setJobid(java.lang.Long value) {
		this.jobid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getDispatchtype() {
		return this.dispatchtype;
	}

	/**
	 *@generated
	 */
	public void setDispatchtype(java.lang.String value) {
		this.dispatchtype = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIscheck() {
		return this.ischeck;
	}

	/**
	 *@generated
	 */
	public void setIscheck(java.lang.Boolean value) {
		this.ischeck = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCheckter() {
		return this.checkter;
	}

	/**
	 *@generated
	 */
	public void setCheckter(java.lang.String value) {
		this.checkter = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getChecktime() {
		return this.checktime;
	}

	/**
	 *@generated
	 */
	public void setChecktime(java.util.Date value) {
		this.checktime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getRemark() {
		return this.remark;
	}

	/**
	 *@generated
	 */
	public void setRemark(java.lang.String value) {
		this.remark = value;
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