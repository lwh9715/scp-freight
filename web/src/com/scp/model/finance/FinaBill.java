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
@Table(name = "fina_bill")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class FinaBill implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "corpid")
	private Long corpid;
	
	@Column(name = "jobid")
	private Long jobid;

	public Long getCorpid() {
		return corpid;
	}

	public void setCorpid(Long corpid) {
		this.corpid = corpid;
	}

	/**
	 *@generated
	 */
	@Column(name = "billno", length = 30)
	private java.lang.String billno;
	
	/**
	 *@generated
	 */
	@Column(name = "billladingno", length = 100)
	private java.lang.String billladingno;

	/**
	 *@generated
	 */
	@Column(name = "billdate", length = 13)
	private java.util.Date billdate;

	/**
	 *@generated
	 */
	@Column(name = "clientid")
	private java.lang.Long clientid;

	/**
	 *@generated
	 */
	@Column(name = "clientname", length = 260)
	private java.lang.String clientname;
	
	/**
	 *@generated
	 */
	@Column(name = "contact")
	private java.lang.String contact;
	
	 

	/**
	 *@generated
	 */
	@Column(name = "accountid")
	private java.lang.Long accountid;

	/**
	 *@generated
	 */
	@Column(name = "accountcont")
	private java.lang.String accountcont;

	/**
	 *@generated
	 */
	@Column(name = "currency", length = 10)
	private java.lang.String currency;

	/**
	 *@generated
	 */
	@Column(name = "notesdesc")
	private java.lang.String notesdesc;

	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String remarks;

	/**
	 *@generated
	 */
	@Column(name = "isconfirmed", length = 1)
	private java.lang.String isconfirmed;

	/**
	 *@generated
	 */
	@Column(name = "confirmdesc", length = 13)
	private java.util.Date confirmdesc;

	/**
	 *@generated
	 */
	@Column(name = "isprinted")
	private java.lang.Boolean isprinted;

	/**
	 *@generated
	 */
	@Column(name = "printremark")
	private java.lang.String printremark;

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
	@Column(name = "reportid")
	private java.lang.Long reportid;
	
	/**
	 *@generated
	 */
	@Column(name = "clientitle")
	private java.lang.String clientitle ;
	
	@Column(name = "capitalen")
	private java.lang.String capitalen ;
	
	@Column(name = "corpidtitile")
	private Long corpidtitile;
	
	@Column(name = "isconfirm")
	private java.lang.Boolean isconfirm;
	
	@Column(name = "confirmdate")
	private java.util.Date confirmdate;
	
	@Column(name = "confirmer",length=30)
	private java.lang.String confirmer;
	
	@Column(name = "ischeck")
	private java.lang.Boolean ischeck;
	
	@Column(name = "checkdate")
	private java.util.Date checkdate;
	
	@Column(name = "checker",length=30)
	private java.lang.String checker;
	
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
	public java.lang.String getBillno() {
		return this.billno;
	}

	/**
	 *@generated
	 */
	public void setBillno(java.lang.String value) {
		this.billno = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getBilldate() {
		return this.billdate;
	}

	/**
	 *@generated
	 */
	public void setBilldate(java.util.Date value) {
		this.billdate = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getClientid() {
		return this.clientid;
	}

	/**
	 *@generated
	 */
	public void setClientid(java.lang.Long value) {
		this.clientid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getClientname() {
		return this.clientname;
	}

	/**
	 *@generated
	 */
	public void setClientname(java.lang.String value) {
		this.clientname = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getAccountid() {
		return this.accountid;
	}

	/**
	 *@generated
	 */
	public void setAccountid(java.lang.Long value) {
		this.accountid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getAccountcont() {
		return this.accountcont;
	}

	/**
	 *@generated
	 */
	public void setAccountcont(java.lang.String value) {
		this.accountcont = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCurrency() {
		return this.currency;
	}

	/**
	 *@generated
	 */
	public void setCurrency(java.lang.String value) {
		this.currency = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getNotesdesc() {
		return this.notesdesc;
	}

	/**
	 *@generated
	 */
	public void setNotesdesc(java.lang.String value) {
		this.notesdesc = value;
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
	public java.lang.String getIsconfirmed() {
		return this.isconfirmed;
	}

	/**
	 *@generated
	 */
	public void setIsconfirmed(java.lang.String value) {
		this.isconfirmed = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getConfirmdesc() {
		return this.confirmdesc;
	}

	/**
	 *@generated
	 */
	public void setConfirmdesc(java.util.Date value) {
		this.confirmdesc = value;
	}

	
	/**
	 *@generated
	 */
	public java.lang.String getPrintremark() {
		return this.printremark;
	}

	/**
	 *@generated
	 */
	public void setPrintremark(java.lang.String value) {
		this.printremark = value;
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

	/**
	 *@generated
	 */
	public java.lang.Long getReportid() {
		return this.reportid;
	}

	/**
	 *@generated
	 */
	public void setReportid(java.lang.Long value) {
		this.reportid = value;
	}

	public Long getJobid() {
		return jobid;
	}

	public java.lang.String getContact() {
		return contact;
	}

	public void setContact(java.lang.String contact) {
		this.contact = contact;
	}

	public void setJobid(Long jobid) {
		this.jobid = jobid;
	}

	public java.lang.Boolean getIsprinted() {
		return isprinted;
	}

	public void setIsprinted(java.lang.Boolean isprinted) {
		this.isprinted = isprinted;
	}

	public java.lang.String getClientitle() {
		return clientitle;
	}

	public void setClientitle(java.lang.String clientitle) {
		this.clientitle = clientitle;
	}

	public java.lang.String getCapitalen() {
		return capitalen;
	}

	public void setCapitalen(java.lang.String capitalen) {
		this.capitalen = capitalen;
	}

	public java.lang.String getBillladingno() {
		return billladingno;
	}

	public void setBillladingno(java.lang.String billladingno) {
		this.billladingno = billladingno;
	}

	public Long getCorpidtitile() {
		return corpidtitile;
	}

	public void setCorpidtitile(Long corpidtitile) {
		this.corpidtitile = corpidtitile;
	}

	public java.lang.Boolean getIsconfirm() {
		return isconfirm;
	}

	public void setIsconfirm(java.lang.Boolean isconfirm) {
		this.isconfirm = isconfirm;
	}

	public java.util.Date getConfirmdate() {
		return confirmdate;
	}

	public void setConfirmdate(java.util.Date confirmdate) {
		this.confirmdate = confirmdate;
	}

	public java.lang.String getConfirmer() {
		return confirmer;
	}

	public void setConfirmer(java.lang.String confirmer) {
		this.confirmer = confirmer;
	}

	public java.lang.Boolean getIscheck() {
		return ischeck;
	}

	public void setIscheck(java.lang.Boolean ischeck) {
		this.ischeck = ischeck;
	}

	public java.util.Date getCheckdate() {
		return checkdate;
	}

	public void setCheckdate(java.util.Date checkdate) {
		this.checkdate = checkdate;
	}

	public java.lang.String getChecker() {
		return checker;
	}

	public void setChecker(java.lang.String checker) {
		this.checker = checker;
	}
	
	
}