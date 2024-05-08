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
@Table(name = "wms_out")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class WmsOut implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "corpid")
	private Long corpid;
	
	@Column(name = "inid")
	private Long inid;

	public Long getCorpid() {
		return corpid;
	}

	public void setCorpid(Long corpid) {
		this.corpid = corpid;
	}
	
	@Column(name="linktbl")
	private String linktbl;

	public String getLinktbl() {
		return linktbl;
	}

	public void setLinktbl(String linktbl) {
		this.linktbl = linktbl;
	}

	/**
	 *@generated
	 */
	@Column(name = "nos", length = 30)
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
	@Column(name = "wmstype", length = 1)
	private java.lang.String wmstype;
	/**
	 *@generated
	 */
	@Column(name = "customerabbr")
	private java.lang.String customerabbr;

	/**
	 *@generated
	 */
	@Column(name = "islocal ")
	private java.lang.Boolean islocal ;
	
	
	/**
	 *@generated
	 */
	@Column(name = "isubmit")
	private java.lang.Boolean isubmit;

	/**
	 *@generated
	 */
	@Column(name = "submitime", length = 29)
	private java.util.Date submitime;

	/**
	 *@generated
	 */
	@Column(name = "submiter", length = 30)
	private java.lang.String submiter;

	/**
	 *@generated
	 */
	@Column(name = "ischeck")
	private java.lang.Boolean ischeck;

	/**
	 *@generated
	 */
	@Column(name = "checktime", length = 29)
	private java.util.Date checktime;

	/**
	 *@generated
	 */
	@Column(name = "checkter", length = 30)
	private java.lang.String checkter;

	/**
	 *@generated
	 */
	@Column(name = "remark")
	private java.lang.String remark;
	
	/**
	 *@generated
	 */
	@Column(name = "consignee")
	private java.lang.String consignee;
	
	@Column(name = "refno", length = 30)
	private java.lang.String refno;
	
	
	@Column(name = "channelid")
	private java.lang.Long channelid;

	public java.lang.String getConsignee() {
		return consignee;
	}

	public void setConsignee(java.lang.String consignee) {
		this.consignee = consignee;
	}

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
	
	
	@Column(name = "outime", length = 29)
	private java.util.Date outime;

	public java.lang.String getCustomerabbr() {
		return customerabbr;
	}

	public void setCustomerabbr(java.lang.String customerabbr) {
		this.customerabbr = customerabbr;
	}

	/**
	 *@generated
	 */
	@Column(name = "jobid")
	private java.lang.Long jobid;

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
	public java.lang.String getNos() {
		return this.nos;
	}

	/**
	 *@generated
	 */
	public void setNos(java.lang.String value) {
		this.nos = value;
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
	public java.util.Date getSubmitime() {
		return this.submitime;
	}

	/**
	 *@generated
	 */
	public void setSubmitime(java.util.Date value) {
		this.submitime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getSubmiter() {
		return this.submiter;
	}

	/**
	 *@generated
	 */
	public void setSubmiter(java.lang.String value) {
		this.submiter = value;
	}


	public java.lang.Boolean getIsubmit() {
		return isubmit;
	}

	public void setIsubmit(java.lang.Boolean isubmit) {
		this.isubmit = isubmit;
	}

	public java.lang.Boolean getIslocal() {
		return islocal;
	}

	public void setIslocal(java.lang.Boolean islocal) {
		this.islocal = islocal;
	}

	public java.lang.Boolean getIscheck() {
		return ischeck;
	}

	public void setIscheck(java.lang.Boolean ischeck) {
		this.ischeck = ischeck;
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

	public java.lang.String getWmstype() {
		return wmstype;
	}

	public void setWmstype(java.lang.String wmstype) {
		this.wmstype = wmstype;
	}

	public Long getInid() {
		return inid;
	}

	public void setInid(Long inid) {
		this.inid = inid;
	}

	public java.lang.String getRefno() {
		return refno;
	}

	public void setRefno(java.lang.String refno) {
		this.refno = refno;
	}

	public java.util.Date getOutime() {
		return outime;
	}

	public void setOutime(java.util.Date outime) {
		this.outime = outime;
	}

	public java.lang.Long getChannelid() {
		return channelid;
	}

	public void setChannelid(java.lang.Long channelid) {
		this.channelid = channelid;
	}
	
}