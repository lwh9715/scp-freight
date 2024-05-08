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
@Table(name = "fina_arap")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class FinaArap implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "corpid")
	private Long corpid;
	
	@Column(name = "corpid2")
	private Long corpid2;
	
	@Column(name = "parentid")
	private Long parentid;
	
	@Column(name = "agenbillid")
	private Long agenbillid;

	@Column(name = "cntid")
	private Long cntid;
	
	/**
	 *@generated
	 */
	@Column(name = "araptype", nullable = false, length = 2)
	private java.lang.String araptype;
	
	/**
	 *@generated
	 */
	@Column(name = "sharetype", length = 1)
	private java.lang.String sharetype;
	
	@Column(name = "billxtype" , length = 1)
	private java.lang.String billxtype;
	
	@Column(name = "invoicextype" , length = 1)
	private java.lang.String invoicextype;
	

	/**
	 *@generated
	 */
	@Column(name = "arapdate", length = 13)
	private java.util.Date arapdate;

	/**
	 *@generated
	 */
	@Column(name = "fkid")
	private java.lang.Long fkid;

	/**
	 *@generated
	 */
	@Column(name = "customerid")
	private java.lang.Long customerid;

	/**
	 *@generated
	 */
	@Column(name = "feeitemid")
	private java.lang.Long feeitemid;

	/**
	 *@generated
	 */
	@Column(name = "pricenotax")
	private java.math.BigDecimal pricenotax;
	
	/**
	 *@generated
	 */
	@Column(name = "taxrate")
	private java.math.BigDecimal taxrate;
	
	/**
	 *@generated
	 */
	@Column(name = "amount")
	private java.math.BigDecimal amount;
	
	/**
	 *@generated
	 */
	@Column(name = "amtstl",updatable=false)
	private java.math.BigDecimal amtstl;
	
	@Column(name = "amtstl2",updatable=false)
	private java.math.BigDecimal amtstl2 ;
	
	@Column(name = "isfinish",updatable=false)
	private Boolean isfinish = false;
	
	@Column(name = "isfinish2",updatable=false)
	private Boolean isfinish2 = false;
	
	
	@Column(name = "confirmer", length = 30)
	private java.lang.String confirmer;
	
	
	@Column(name = "confirmtime", length = 29)
	private java.util.Date confirmtime;

	/**
	 *@generated
	 */
	@Column(name = "currency", length = 10)
	private java.lang.String currency;
	
	@Column(name = "fktbl", length = 30)
	private java.lang.String fktbl;
	
	/**
	 *@generated
	 */
	@Column(name = "ppcc ", length = 50)
	private java.lang.String ppcc ;
	
	@Column(name = "amtcost")
	private java.math.BigDecimal amtcost;
	
	public java.lang.String getPpcc() {
		return ppcc;
	}

	public void setPpcc(java.lang.String ppcc) {
		this.ppcc = ppcc;
	}

	/**
	 *@generated
	 */
	@Column(name = "customercode")
	private java.lang.String customercode;
	

	/**
	 *@generated
	 */
	@Column(name = "billid")
	private java.lang.Long billid;

	/**
	 *@generated
	 */
	@Column(name = "billxrate")
	private java.math.BigDecimal billxrate;

	/**
	 *@generated
	 */
	@Column(name = "billamount")
	private java.math.BigDecimal billamount;

	/**
	 *@generated
	 */
	@Column(name = "invoiceid")
	private java.lang.Long invoiceid;

	/**
	 *@generated
	 */
	@Column(name = "invoicexrate")
	private java.math.BigDecimal invoicexrate;

	/**
	 *@generated
	 */
	@Column(name = "invoiceamount")
	private java.math.BigDecimal invoiceamount;

	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	@Column(name = "rptype")
	private java.lang.String rptype;

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
	@Column(name = "piece")
	private java.math.BigDecimal piece;

	/**
	 *@generated
	 */
	@Column(name = "price")
	private java.math.BigDecimal price;

	/**
	 *@generated
	 */
	@Column(name = "jobid")
	private java.lang.Long jobid;

	/**
	 *@generated
	 */
	@Column(name = "unit", length = 10)
	private java.lang.String unit;

	/**
	 *@generated
	 */
	@Column(name = "descinfo")
	private java.lang.String descinfo;
	
	/**
	 *@generated
	 */
	@Column(name = "isamend")
	private java.lang.Boolean isamend;
	
	@Column(name = "isunionfee")
	private java.lang.Boolean isunionfee;
	/**
	 *@generated
	 */
	@Column(name = "isconfirm")
	private java.lang.Boolean isconfirm;
	
	@Column(name = "ordno")
	private Integer ordno;
	
	@Column(name = "payplace", length = 2)
	private java.lang.String payplace;
	
	@Column(name = "blno", length = 30)
	private java.lang.String blno;
	
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
	
	public Long getAgenbillid() {
		return agenbillid;
	}

	public void setAgenbillid(Long agenbillid) {
		this.agenbillid = agenbillid;
	}

	/**
	 *@generated
	 */
	public java.lang.String getAraptype() {
		return this.araptype;
	}

	/**
	 *@generated
	 */
	public void setAraptype(java.lang.String value) {
		this.araptype = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getArapdate() {
		return this.arapdate;
	}

	/**
	 *@generated
	 */
	public void setArapdate(java.util.Date value) {
		this.arapdate = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getFkid() {
		return this.fkid;
	}

	/**
	 *@generated
	 */
	public void setFkid(java.lang.Long value) {
		this.fkid = value;
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
	public java.lang.Long getFeeitemid() {
		return this.feeitemid;
	}

	/**
	 *@generated
	 */
	public void setFeeitemid(java.lang.Long value) {
		this.feeitemid = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getAmount() {
		return this.amount;
	}

	/**
	 *@generated
	 */
	public void setAmount(java.math.BigDecimal value) {
		this.amount = value;
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
	public java.lang.Long getBillid() {
		return this.billid;
	}

	/**
	 *@generated
	 */
	public void setBillid(java.lang.Long value) {
		this.billid = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getBillxrate() {
		return this.billxrate;
	}

	/**
	 *@generated
	 */
	public void setBillxrate(java.math.BigDecimal value) {
		this.billxrate = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getBillamount() {
		return this.billamount;
	}

	/**
	 *@generated
	 */
	public void setBillamount(java.math.BigDecimal value) {
		this.billamount = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getInvoiceid() {
		return this.invoiceid;
	}

	/**
	 *@generated
	 */
	public void setInvoiceid(java.lang.Long value) {
		this.invoiceid = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getInvoicexrate() {
		return this.invoicexrate;
	}

	/**
	 *@generated
	 */
	public void setInvoicexrate(java.math.BigDecimal value) {
		this.invoicexrate = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getInvoiceamount() {
		return this.invoiceamount;
	}

	/**
	 *@generated
	 */
	public void setInvoiceamount(java.math.BigDecimal value) {
		this.invoiceamount = value;
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
	public java.math.BigDecimal getPrice() {
		return this.price;
	}

	/**
	 *@generated
	 */
	public void setPrice(java.math.BigDecimal value) {
		this.price = value;
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
	public java.lang.String getUnit() {
		return this.unit;
	}

	/**
	 *@generated
	 */
	public void setUnit(java.lang.String value) {
		this.unit = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getDescinfo() {
		return this.descinfo;
	}

	/**
	 *@generated
	 */
	public void setDescinfo(java.lang.String value) {
		this.descinfo = value;
	}

	public java.lang.String getCustomercode() {
		return customercode;
	}

	public void setCustomercode(java.lang.String customercode) {
		this.customercode = customercode;
	}

	public java.math.BigDecimal getPiece() {
		return piece;
	}

	public void setPiece(java.math.BigDecimal piece) {
		this.piece = piece;
	}

	public java.lang.String getRptype() {
		return rptype;
	}

	public void setRptype(java.lang.String rptype) {
		this.rptype = rptype;
	}

	public java.lang.String getFktbl() {
		return fktbl;
	}

	public void setFktbl(java.lang.String fktbl) {
		this.fktbl = fktbl;
	}

	public java.lang.String getBillxtype() {
		return billxtype;
	}

	public void setBillxtype(java.lang.String billxtype) {
		this.billxtype = billxtype;
	}

	public java.lang.String getInvoicextype() {
		return invoicextype;
	}

	public void setInvoicextype(java.lang.String invoicextype) {
		this.invoicextype = invoicextype;
	}

	public java.math.BigDecimal getPricenotax() {
		return pricenotax;
	}

	public java.math.BigDecimal getTaxrate() {
		return taxrate;
	}

	public void setPricenotax(java.math.BigDecimal pricenotax) {
		this.pricenotax = pricenotax;
	}

	public void setTaxrate(java.math.BigDecimal taxrate) {
		this.taxrate = taxrate;
	}

	public Long getCntid() {
		return cntid;
	}

	public void setCntid(Long cntid) {
		this.cntid = cntid;
	}

	public java.math.BigDecimal getAmtstl() {
		return amtstl;
	}

	public void setAmtstl(java.math.BigDecimal amtstl) {
		this.amtstl = amtstl;
	}

	public java.lang.Boolean getIsamend() {
		return isamend;
	}

	public void setIsamend(java.lang.Boolean isamend) {
		this.isamend = isamend;
	}

	public java.lang.Boolean getIsconfirm() {
		return isconfirm;
	}

	public void setIsconfirm(java.lang.Boolean isconfirm) {
		this.isconfirm = isconfirm;
	}

	public java.lang.String getSharetype() {
		return sharetype;
	}

	public void setSharetype(java.lang.String sharetype) {
		this.sharetype = sharetype;
	}

	public Long getParentid() {
		return parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	public java.math.BigDecimal getAmtstl2() {
		return amtstl2;
	}

	public void setAmtstl2(java.math.BigDecimal amtstl2) {
		this.amtstl2 = amtstl2;
	}

	public Boolean getIsfinish() {
		return isfinish;
	}

	public void setIsfinish(Boolean isfinish) {
		this.isfinish = isfinish;
	}

	public Boolean getIsfinish2() {
		return isfinish2;
	}

	public void setIsfinish2(Boolean isfinish2) {
		this.isfinish2 = isfinish2;
	}

	public Long getCorpid() {
		return corpid;
	}

	public void setCorpid(Long corpid) {
		this.corpid = corpid;
	}

	public Long getCorpid2() {
		return corpid2;
	}

	public void setCorpid2(Long corpid2) {
		this.corpid2 = corpid2;
	}

	public java.lang.String getConfirmer() {
		return confirmer;
	}

	public void setConfirmer(java.lang.String confirmer) {
		this.confirmer = confirmer;
	}

	public java.util.Date getConfirmtime() {
		return confirmtime;
	}

	public void setConfirmtime(java.util.Date confirmtime) {
		this.confirmtime = confirmtime;
	}

	public Integer getOrdno() {
		return ordno;
	}

	public void setOrdno(Integer ordno) {
		this.ordno = ordno;
	}

	public java.lang.Boolean getIsunionfee() {
		return isunionfee;
	}

	public void setIsunionfee(java.lang.Boolean isunionfee) {
		this.isunionfee = isunionfee;
	}

	public java.lang.String getPayplace() {
		return payplace;
	}

	public void setPayplace(java.lang.String payplace) {
		this.payplace = payplace;
	}

	public java.lang.String getBlno() {
		return blno;
	}

	public void setBlno(java.lang.String blno) {
		this.blno = blno;
	}

	public java.math.BigDecimal getAmtcost() {
		return amtcost;
	}

	public void setAmtcost(java.math.BigDecimal amtcost) {
		this.amtcost = amtcost;
	}

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
	}
	
}