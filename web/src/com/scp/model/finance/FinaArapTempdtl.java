package com.scp.model.finance;

import java.math.BigDecimal;

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
@Table(name = "fina_arap_tempdtl")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class FinaArapTempdtl implements java.io.Serializable {

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
	@Column(name = "templetid")
	private java.lang.Long templetid;

	/**
	 *@generated
	 */
	@Column(name = "araptype", nullable = false, length = 2)
	private java.lang.String araptype;
	
	@Column(name = "custype", length = 1)
	private java.lang.String custype;
	

	/**
	 *@generated
	 */
	@Column(name = "customerid")
	private java.lang.Long customerid;
	
	
	@Column(name = "cntypeid")
	private java.lang.Long cntypeid;

	@Column(name = "cntnumber")
	private java.lang.Boolean cntnumber;
	
	/**
	 *@generated
	 */
	@Column(name = "feeitemid")
	private java.lang.Long feeitemid;

	/**
	 *@generated
	 */
	@Column(name = "currency", length = 10)
	private java.lang.String currency;

	/**
	 *@generated
	 */
	@Column(name = "amount")
	private java.math.BigDecimal amount;
	
	/**
	 *@generated
	 */
	@Column(name = "taxrate")
	private java.math.BigDecimal taxrate = new BigDecimal(1);

	/**
	 *@generated
	 */
	@Column(name = "ppcc", length = 2)
	private java.lang.String ppcc;
	
	
	@Column(name = "customercode", length = 100)
	private java.lang.String customercode;

	/**
	 *@generated
	 */
	@Column(name = "calctype", length = 50)
	private java.lang.String calctype;

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
	
	@Column(name = "unit", length = 10)
	private java.lang.String unit;
	
	@Column(name = "payplace", length = 2)
	private java.lang.String payplace;

	public java.lang.String getUnit() {
		return unit;
	}

	public void setUnit(java.lang.String unit) {
		this.unit = unit;
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
	public java.lang.Long getTempletid() {
		return this.templetid;
	}

	/**
	 *@generated
	 */
	public void setTempletid(java.lang.Long value) {
		this.templetid = value;
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
	public java.lang.String getPpcc() {
		return this.ppcc;
	}

	/**
	 *@generated
	 */
	public void setPpcc(java.lang.String value) {
		this.ppcc = value;
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

	public java.lang.String getCustomercode() {
		return customercode;
	}

	public void setCustomercode(java.lang.String customercode) {
		this.customercode = customercode;
	}

	public java.lang.String getCustype() {
		return custype;
	}

	public void setCustype(java.lang.String custype) {
		this.custype = custype;
	}

	public java.lang.Long getCntypeid() {
		return cntypeid;
	}

	public void setCntypeid(java.lang.Long cntypeid) {
		this.cntypeid = cntypeid;
	}

	public java.math.BigDecimal getTaxrate() {
		return taxrate;
	}

	public void setTaxrate(java.math.BigDecimal taxrate) {
		this.taxrate = taxrate;
	}

	public java.lang.Boolean getCntnumber() {
		return cntnumber;
	}

	public void setCntnumber(java.lang.Boolean cntnumber) {
		this.cntnumber = cntnumber;
	}

	public java.lang.String getPayplace() {
		return payplace;
	}

	public void setPayplace(java.lang.String payplace) {
		this.payplace = payplace;
	}
	
}