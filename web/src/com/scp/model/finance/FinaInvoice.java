package com.scp.model.finance;

import java.util.Date;

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
@Table(name = "fina_invoice")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class FinaInvoice implements java.io.Serializable {

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
	@Column(name = "invoiceno", length = 30)
	private java.lang.String invoiceno;

	/**
	 *@generated
	 */
	@Column(name = "invoicedate", length = 13)
	private java.util.Date invoicedate;

	/**
	 *@generated
	 */
	@Column(name = "clientid")
	private java.lang.Long clientid;
	
	
	@Column(name = "jobid")
	private java.lang.Long jobid;
	
	@Column(name = "clientitle")
	private java.lang.String clientitle;
	
	
	@Column(name = "contact")
	private java.lang.String contact;

	/**
	 *@generated
	 */
	@Column(name = "clientname")
	private java.lang.String clientname;

	/**
	 *@generated
	 */
	@Column(name = "accountid")
	private java.lang.Long accountid;

	/**
	 *@generated
	 */
	@Column(name = "accountno", length = 30)
	private java.lang.String accountno;

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

	
	@Column(name = "isprinted")
	private java.lang.Boolean isprinted;

	@Column(name = "businvoiceconfirm")
	private java.lang.Boolean businvoiceconfirm;

	@Column(name = "businvoiceconfirm_time", length = 29)
	private java.util.Date businvoiceconfirm_time;

	@Column(name = "businvoiceconfirm_user", length = 100)
	private java.lang.String businvoiceconfirm_user;

	/**
	 *@generated
	 */
	@Column(name = "reportid")
	private java.lang.Long reportid;

	/**
	 *@generated
	 */
	@Column(name = "paymenterms", length = 100)
	private java.lang.String paymenterms;

	/**
	 *@generated
	 */
	@Column(name = "dono", length = 30)
	private java.lang.String dono;

	/**
	 *@generated
	 */
	@Column(name = "lpono", length = 30)
	private java.lang.String lpono;
	
	@Column(name = "capitalch")
	private java.lang.String capitalch;
	
	@Column(name = "invoicedesc")
	private java.lang.String invoicedesc;
	
	@Column(name = "feeitemdesc")
	private java.lang.String feeitemdesc;
	
	@Column(name = "munberdesc")
	private java.lang.String munberdesc;
	
	@Column(name = "amountdesc")
	private java.lang.String amountdesc;
	
	@Column(name = "amounts")
	private java.math.BigDecimal amounts;
	
	@Column(name = "invoicetype")
	private java.lang.String invoicetype;
	
	@Column(name = "payee", length = 10)
	private java.lang.String payee;
	
	@Column(name = "checker", length = 10)
	private java.lang.String checker;
	
	@Column(name = "issuer", length = 10)
	private java.lang.String issuer;
	
	@Column(name = "productname", length = 1)
	private java.lang.String productname;
	
	@Column(name = "invoicecode", length = 100)
	private java.lang.String invoicecode;
	
	@Column(name = "isinvsvr")
	private java.lang.Boolean isinvsvr;
	
	@Column(name = "iscancel")
	private java.lang.Boolean iscancel;
	
	@Column(name = "parentid")
	private java.lang.Long parentid;
	
	@Column(name = "amountto")
	private java.math.BigDecimal amountto;
	
	@Column(name = "ismodifyamountto")
	private java.lang.Boolean ismodifyamountto;
	
	@Column(name = "seqno", length = 100)
	private java.lang.String seqno;
	
	@Column(name = "editype", length = 100)
	private java.lang.String editype;
	
	@Column(name = "fpqqlsh", length = 200)
	private java.lang.String fpqqlsh;
	
	
	@Column(name = "invstatus")
	private java.lang.String invstatus;
	
	@Column(name = "urlpdf")
	private java.lang.String urlpdf;

	@Column(name = "remark")
	private java.lang.String remark;

	@Column(name = "customername")
	private java.lang.String customername;

	@Column(name = "taxpayernumber")
	private java.lang.String taxpayernumber;

	@Column(name = "address")
	private java.lang.String address;

	@Column(name = "accountbankandaccountnumber")
	private java.lang.String accountbankandaccountnumber;
	
	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getTaxpayernumber() {
		return taxpayernumber;
	}

	public void setTaxpayernumber(String taxpayernumber) {
		this.taxpayernumber = taxpayernumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAccountbankandaccountnumber() {
		return accountbankandaccountnumber;
	}

	public void setAccountbankandaccountnumber(String accountbankandaccountnumber) {
		this.accountbankandaccountnumber = accountbankandaccountnumber;
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
	public java.lang.String getInvoiceno() {
		return this.invoiceno;
	}

	/**
	 *@generated
	 */
	public void setInvoiceno(java.lang.String value) {
		this.invoiceno = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getInvoicedate() {
		return this.invoicedate;
	}

	/**
	 *@generated
	 */
	public void setInvoicedate(java.util.Date value) {
		this.invoicedate = value;
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
	public java.lang.String getAccountno() {
		return this.accountno;
	}

	/**
	 *@generated
	 */
	public void setAccountno(java.lang.String value) {
		this.accountno = value;
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

	/**
	 *@generated
	 */
	public java.lang.String getPaymenterms() {
		return this.paymenterms;
	}

	/**
	 *@generated
	 */
	public void setPaymenterms(java.lang.String value) {
		this.paymenterms = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getDono() {
		return this.dono;
	}

	/**
	 *@generated
	 */
	public void setDono(java.lang.String value) {
		this.dono = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getLpono() {
		return this.lpono;
	}

	/**
	 *@generated
	 */
	public void setLpono(java.lang.String value) {
		this.lpono = value;
	}

	public java.lang.Long getJobid() {
		return jobid;
	}

	public void setJobid(java.lang.Long jobid) {
		this.jobid = jobid;
	}

	public java.lang.String getClientitle() {
		return clientitle;
	}

	public void setClientitle(java.lang.String clientitle) {
		this.clientitle = clientitle;
	}

	public java.lang.String getContact() {
		return contact;
	}

	public void setContact(java.lang.String contact) {
		this.contact = contact;
	}

	public java.lang.Boolean getIsprinted() {
		return isprinted;
	}

	public void setIsprinted(java.lang.Boolean isprinted) {
		this.isprinted = isprinted;
	}

	public java.lang.String getCapitalch() {
		return capitalch;
	}

	public void setCapitalch(java.lang.String capitalch) {
		this.capitalch = capitalch;
	}

	public java.lang.String getInvoicedesc() {
		return invoicedesc;
	}

	public void setInvoicedesc(java.lang.String invoicedesc) {
		this.invoicedesc = invoicedesc;
	}

	public java.lang.String getFeeitemdesc() {
		return feeitemdesc;
	}

	public void setFeeitemdesc(java.lang.String feeitemdesc) {
		this.feeitemdesc = feeitemdesc;
	}

	public java.lang.String getMunberdesc() {
		return munberdesc;
	}

	public void setMunberdesc(java.lang.String munberdesc) {
		this.munberdesc = munberdesc;
	}

	public java.lang.String getAmountdesc() {
		return amountdesc;
	}

	public void setAmountdesc(java.lang.String amountdesc) {
		this.amountdesc = amountdesc;
	}

	public java.math.BigDecimal getAmounts() {
		return amounts;
	}

	public void setAmounts(java.math.BigDecimal amounts) {
		this.amounts = amounts;
	}

	public java.lang.String getInvoicetype() {
		return invoicetype;
	}

	public void setInvoicetype(java.lang.String invoicetype) {
		this.invoicetype = invoicetype;
	}

	public java.lang.String getPayee() {
		return payee;
	}

	public void setPayee(java.lang.String payee) {
		this.payee = payee;
	}

	public java.lang.String getChecker() {
		return checker;
	}

	public void setChecker(java.lang.String checker) {
		this.checker = checker;
	}

	public java.lang.String getIssuer() {
		return issuer;
	}

	public void setIssuer(java.lang.String issuer) {
		this.issuer = issuer;
	}

	public java.lang.String getProductname() {
		return productname;
	}

	public void setProductname(java.lang.String productname) {
		this.productname = productname;
	}

	public java.lang.String getInvoicecode() {
		return invoicecode;
	}

	public void setInvoicecode(java.lang.String invoicecode) {
		this.invoicecode = invoicecode;
	}

	public java.lang.Boolean getIsinvsvr() {
		return isinvsvr;
	}

	public void setIsinvsvr(java.lang.Boolean isinvsvr) {
		this.isinvsvr = isinvsvr;
	}

	public java.lang.Boolean getIscancel() {
		return iscancel;
	}

	public void setIscancel(java.lang.Boolean iscancel) {
		this.iscancel = iscancel;
	}

	public java.lang.Long getParentid() {
		return parentid;
	}

	public void setParentid(java.lang.Long parentid) {
		this.parentid = parentid;
	}

	public java.math.BigDecimal getAmountto() {
		return amountto;
	}

	public void setAmountto(java.math.BigDecimal amountto) {
		this.amountto = amountto;
	}

	public java.lang.Boolean getIsmodifyamountto() {
		return ismodifyamountto;
	}

	public void setIsmodifyamountto(java.lang.Boolean ismodifyamountto) {
		this.ismodifyamountto = ismodifyamountto;
	}

	public java.lang.String getSeqno() {
		return seqno;
	}

	public void setSeqno(java.lang.String seqno) {
		this.seqno = seqno;
	}

	public java.lang.String getEditype() {
		return editype;
	}

	public void setEditype(java.lang.String editype) {
		this.editype = editype;
	}

	public java.lang.String getFpqqlsh() {
		return fpqqlsh;
	}

	public void setFpqqlsh(java.lang.String fpqqlsh) {
		this.fpqqlsh = fpqqlsh;
	}

	public java.lang.String getInvstatus() {
		return invstatus;
	}

	public void setInvstatus(java.lang.String invstatus) {
		this.invstatus = invstatus;
	}

	public java.lang.String getUrlpdf() {
		return urlpdf;
	}

	public void setUrlpdf(java.lang.String urlpdf) {
		this.urlpdf = urlpdf;
	}

	public Boolean getBusinvoiceconfirm() {
		return businvoiceconfirm;
	}

	public void setBusinvoiceconfirm(Boolean businvoiceconfirm) {
		this.businvoiceconfirm = businvoiceconfirm;
	}

	public Date getBusinvoiceconfirm_time() {
		return businvoiceconfirm_time;
	}

	public void setBusinvoiceconfirm_time(Date businvoiceconfirm_time) {
		this.businvoiceconfirm_time = businvoiceconfirm_time;
	}

	public String getBusinvoiceconfirm_user() {
		return businvoiceconfirm_user;
	}

	public void setBusinvoiceconfirm_user(String businvoiceconfirm_user) {
		this.businvoiceconfirm_user = businvoiceconfirm_user;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}