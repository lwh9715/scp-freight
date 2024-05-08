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
@Table(name = "wms_price")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class WmsPrice implements java.io.Serializable {

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
	@Column(name = "feeitemid ")
	private java.lang.Long feeitemid ;

	public java.lang.Long getFeeitemid() {
		return feeitemid;
	}

	public void setFeeitemid(java.lang.Long feeitemid) {
		this.feeitemid = feeitemid;
	}

	/**
	 *@generated
	 */
	@Column(name = "warehouseid")
	private java.lang.Long warehouseid;

	/**
	 *@generated
	 */
	@Column(name = "pricename")
	private java.lang.String pricename;

	/**
	 *@generated
	 */
	@Column(name = "araptype", nullable = false, length = 6)
	private java.lang.String araptype;

	
	
	
	public java.lang.String getAraptype() {
		return araptype;
	}

	public void setAraptype(java.lang.String araptype) {
		this.araptype = araptype;
	}

	/**
	 *@generated
	 */
	@Column(name = "calctype", nullable = false)
	private java.lang.String calctype;

	/**
	 *@generated
	 */
	@Column(name = "timestart", length = 13)
	private java.util.Date timestart;

	/**
	 *@generated
	 */
	@Column(name = "timeend", length = 13)
	private java.util.Date timeend;

	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	/**
	 *@generated
	 */
	@Column(name = "isdefault")
	private java.lang.Boolean isdefault;


	public java.lang.Boolean getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(java.lang.Boolean isdefault) {
		this.isdefault = isdefault;
	}

	/**
	 *@generated
	 */
	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;
	
	
	/**
	 *@generated
	 */
	@Column(name = "pricetype ", length = 2)
	private java.lang.String pricetype ;

	public java.lang.String getPricetype() {
		return pricetype;
	}

	public void setPricetype(java.lang.String pricetype) {
		this.pricetype = pricetype;
	}

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
	public java.lang.String getPricename() {
		return this.pricename;
	}

	/**
	 *@generated
	 */
	public void setPricename(java.lang.String value) {
		this.pricename = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCalctype() {
		return this.calctype;
	}

	/**
	 *@generated
	 */
	public void setCalctype(java.lang.String value) {
		this.calctype = value;
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
	public java.util.Date getTimeend() {
		return this.timeend;
	}

	/**
	 *@generated
	 */
	public void setTimeend(java.util.Date value) {
		this.timeend = value;
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
}