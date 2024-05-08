package com.scp.model.finance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.Date;

/**
 *@generated
 */
@Table(name = "fina_rpreq")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class FinaRpreq implements java.io.Serializable {

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
	@Column(name = "srctype", length = 1)
	private java.lang.String srctype ="L";
	
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
	@Column(name = "customerabbr", length = 50)
	private java.lang.String customerabbr;

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
	@Column(name = "notesdesc")
	private java.lang.String notesdesc;

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
	@Column(name = "actpayrecid")
	private java.lang.Long actpayrecid;

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
	@Column(name = "remark")
	private java.lang.String remark;
	
	@Column(name = "rptype")
	private java.lang.String rptype;
	
	@Column(name = "reqtype")
	private java.lang.String reqtype;

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
	@Column(name = "showwmsinfilename", length = 30)
	private java.lang.String showwmsinfilename;
	
	/**
	 *@generated
	 */
	@Column(name = "ispay")
	private java.lang.Boolean ispay;
	
	/**
	 *@generated
	 */
	@Column(name = "paytype", length = 10)
	private java.lang.String paytype;
	
	/**
	 *@generated
	 */
	@Column(name = "invoiceno")
	private java.lang.String invoiceno;
	
	
	@Column(name = "isapprove")
	private java.lang.Boolean isapprove;

	@Column(name = "approvetime", length = 29)
	private java.util.Date approvetime;

	@Column(name = "approver", length = 30)
	private java.lang.String approver;
	
	
	@Column(name = "isprint")
	private java.lang.Boolean isprint;

	@Column(name = "printtime", length = 29)
	private java.util.Date printtime;

	@Column(name = "printer", length = 30)
	private java.lang.String printer;

	@Column(name = "islock")
	private java.lang.Boolean islock;

	@Column(name = "locker", length = 30)
	private java.lang.String locker;

	@Column(name = "locktime", length = 29)
	private java.util.Date locktime;

	public java.lang.String getShowwmsinfilename() {
		return showwmsinfilename;
	}

	public void setShowwmsinfilename(java.lang.String showwmsinfilename) {
		this.showwmsinfilename = showwmsinfilename;
	}

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
	public java.lang.Long getActpayrecid() {
		return this.actpayrecid;
	}

	/**
	 *@generated
	 */
	public void setActpayrecid(java.lang.Long value) {
		this.actpayrecid = value;
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

	public java.lang.Boolean getIsubmit() {
		return isubmit;
	}

	public void setIsubmit(java.lang.Boolean isubmit) {
		this.isubmit = isubmit;
	}

	public java.lang.Boolean getIscheck() {
		return ischeck;
	}

	public void setIscheck(java.lang.Boolean ischeck) {
		this.ischeck = ischeck;
	}

	public java.lang.String getRptype() {
		return rptype;
	}

	public void setRptype(java.lang.String rptype) {
		this.rptype = rptype;
	}

	public java.lang.String getReqtype() {
		return reqtype;
	}

	public void setReqtype(java.lang.String reqtype) {
		this.reqtype = reqtype;
	}

	public java.lang.String getSrctype() {
		return srctype;
	}

	public void setSrctype(java.lang.String srctype) {
		this.srctype = srctype;
	}

	public java.lang.Boolean getIspay() {
		return ispay;
	}

	public void setIspay(java.lang.Boolean ispay) {
		this.ispay = ispay;
	}

	public java.lang.String getPaytype() {
		return paytype;
	}

	public void setPaytype(java.lang.String paytype) {
		this.paytype = paytype;
	}

	public java.lang.String getInvoiceno() {
		return invoiceno;
	}

	public void setInvoiceno(java.lang.String invoiceno) {
		this.invoiceno = invoiceno;
	}

	public java.lang.Boolean getIsapprove() {
		return isapprove;
	}

	public void setIsapprove(java.lang.Boolean isapprove) {
		this.isapprove = isapprove;
	}

	public java.util.Date getApprovetime() {
		return approvetime;
	}

	public void setApprovetime(java.util.Date approvetime) {
		this.approvetime = approvetime;
	}

	public java.lang.String getApprover() {
		return approver;
	}

	public void setApprover(java.lang.String approver) {
		this.approver = approver;
	}

	public java.lang.Boolean getIsprint() {
		return isprint;
	}

	public void setIsprint(java.lang.Boolean isprint) {
		this.isprint = isprint;
	}

	public java.util.Date getPrinttime() {
		return printtime;
	}

	public void setPrinttime(java.util.Date printtime) {
		this.printtime = printtime;
	}

	public java.lang.String getPrinter() {
		return printer;
	}

	public void setPrinter(java.lang.String printer) {
		this.printer = printer;
	}

	public Boolean getIslock() {
		return islock;
	}

	public void setIslock(Boolean islock) {
		this.islock = islock;
	}

	public String getLocker() {
		return locker;
	}

	public void setLocker(String locker) {
		this.locker = locker;
	}

	public Date getLocktime() {
		return locktime;
	}

	public void setLocktime(Date locktime) {
		this.locktime = locktime;
	}
}