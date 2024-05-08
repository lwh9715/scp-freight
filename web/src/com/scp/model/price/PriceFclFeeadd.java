package com.scp.model.price;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Date:2016-6-22 FCL运价主附加费：price_fcl_feeadd
 * 
 * @author bruce
 */
@Table(name = "price_fcl_feeadd")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class PriceFclFeeadd implements java.io.Serializable {
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	@Column(name = "fclid")
	private java.lang.Long fclid;

	@Column(name = "feeitemid")
	private java.lang.Long feeitemid;
	
	@Column(name = "feeitemcode", length = 30)
	private java.lang.String feeitemcode;
	
	@Column(name = "feeitemname", length = 100)
	private java.lang.String feeitemname;

	@Column(name = "ppcc", length = 2)
	private java.lang.String ppcc;

	@Column(name = "currency", length = 3)
	private java.lang.String currency;

	@Column(name = "unit", length = 10)
	private java.lang.String unit;

	@Column(name = "amt20")
	private java.math.BigDecimal amt20;

	@Column(name = "amt40gp")
	private java.math.BigDecimal amt40gp;

	@Column(name = "amt40hq")
	private java.math.BigDecimal amt40hq;
	
	@Column(name = "amt45hq")
	private java.math.BigDecimal amt45hq;

	@Column(name = "amt")
	private java.math.BigDecimal amt;

	@Column(name = "istemplate")
	private java.lang.Boolean istemplate;

	@Column(name = "templatename")
	private java.lang.String templatename;

	@Column(name = "inputer", length = 30)
	private java.lang.String inputer;

	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;

	@Column(name = "updater", length = 30)
	private java.lang.String updater;

	@Column(name = "updatetime")
	private java.util.Date updatetime;
	
	@Column(name = "amtother")
	private java.math.BigDecimal amtother;
	
	@Column(name = "cntypeid")
	private java.lang.Long cntypeid;
	
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	@Column(name = "conditiontype", length = 10)
	private java.lang.String conditiontype;
	
	@Column(name = "condition", length = 10)
	private java.lang.String condition;
	
	@Column(name = "conditionvalue")
	private java.lang.String conditionvalue;
	
	@Column(name = "corpid")
	private java.lang.Long corpid;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.Long getFclid() {
		return fclid;
	}

	public void setFclid(java.lang.Long fclid) {
		this.fclid = fclid;
	}

	public java.lang.Long getFeeitemid() {
		return feeitemid;
	}

	public void setFeeitemid(java.lang.Long feeitemid) {
		this.feeitemid = feeitemid;
	}

	public java.lang.String getPpcc() {
		return ppcc;
	}

	public void setPpcc(java.lang.String ppcc) {
		this.ppcc = ppcc;
	}

	public java.lang.String getCurrency() {
		return currency;
	}

	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}

	public java.lang.String getUnit() {
		return unit;
	}

	public void setUnit(java.lang.String unit) {
		this.unit = unit;
	}

	public java.math.BigDecimal getAmt20() {
		return amt20;
	}

	public void setAmt20(java.math.BigDecimal amt20) {
		this.amt20 = amt20;
	}

	public java.math.BigDecimal getAmt40gp() {
		return amt40gp;
	}

	public void setAmt40gp(java.math.BigDecimal amt40gp) {
		this.amt40gp = amt40gp;
	}

	public java.math.BigDecimal getAmt40hq() {
		return amt40hq;
	}

	public void setAmt40hq(java.math.BigDecimal amt40hq) {
		this.amt40hq = amt40hq;
	}

	public java.math.BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(java.math.BigDecimal amt) {
		this.amt = amt;
	}

	public java.lang.Boolean getIstemplate() {
		return istemplate;
	}

	public void setIstemplate(java.lang.Boolean istemplate) {
		this.istemplate = istemplate;
	}

	public java.lang.String getTemplatename() {
		return templatename;
	}

	public void setTemplatename(java.lang.String templatename) {
		this.templatename = templatename;
	}

	public java.lang.String getInputer() {
		return inputer;
	}

	public void setInputer(java.lang.String inputer) {
		this.inputer = inputer;
	}

	public java.util.Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(java.util.Date inputtime) {
		this.inputtime = inputtime;
	}

	public java.lang.String getUpdater() {
		return updater;
	}

	public void setUpdater(java.lang.String updater) {
		this.updater = updater;
	}

	public java.util.Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(java.util.Date updatetime) {
		this.updatetime = updatetime;
	}

	public java.lang.String getFeeitemcode() {
		return feeitemcode;
	}

	public void setFeeitemcode(java.lang.String feeitemcode) {
		this.feeitemcode = feeitemcode;
	}

	public java.lang.String getFeeitemname() {
		return feeitemname;
	}

	public void setFeeitemname(java.lang.String feeitemname) {
		this.feeitemname = feeitemname;
	}

	public java.math.BigDecimal getAmtother() {
		return amtother;
	}

	public void setAmtother(java.math.BigDecimal amtother) {
		this.amtother = amtother;
	}

	public java.lang.Long getCntypeid() {
		return cntypeid;
	}

	public void setCntypeid(java.lang.Long cntypeid) {
		this.cntypeid = cntypeid;
	}

	public java.math.BigDecimal getAmt45hq() {
		return amt45hq;
	}

	public void setAmt45hq(java.math.BigDecimal amt45hq) {
		this.amt45hq = amt45hq;
	}

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
	}

	public java.lang.String getConditiontype() {
		return conditiontype;
	}

	public void setConditiontype(java.lang.String conditiontype) {
		this.conditiontype = conditiontype;
	}

	public java.lang.String getCondition() {
		return condition;
	}

	public void setCondition(java.lang.String condition) {
		this.condition = condition;
	}

	public java.lang.String getConditionvalue() {
		return conditionvalue;
	}

	public void setConditionvalue(java.lang.String conditionvalue) {
		this.conditionvalue = conditionvalue;
	}

	public java.lang.Long getCorpid() {
		return corpid;
	}

	public void setCorpid(java.lang.Long corpid) {
		this.corpid = corpid;
	}
	
}
