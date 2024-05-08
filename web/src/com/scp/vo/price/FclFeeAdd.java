package com.scp.vo.price;


/**
 */
public class FclFeeAdd implements java.io.Serializable {
	
	private long id;

	private java.lang.Long fclid;

	private java.lang.Long feeitemid;
	
	private java.lang.String feeitemcode;
	
	private java.lang.String feeitemname;

	private java.lang.String ppcc;

	private java.lang.String currency;

	private java.lang.String unit;

	private java.math.BigDecimal amt20;

	private java.math.BigDecimal amt40gp;

	private java.math.BigDecimal amt40hq;
	
	private java.math.BigDecimal amt45hq;
	
	private java.math.BigDecimal amtother;

	private java.math.BigDecimal amt;

	private java.math.BigDecimal amt20_ar;

	private java.math.BigDecimal amt40gp_ar;

	private java.math.BigDecimal amt40hq_ar;

	private java.math.BigDecimal amt_ar;
	
	private java.lang.Long cntypeid;
	
	private String cntypecode;
	
	private int piece20;
	
	private int piece40gp;
	
	private int piece40hq;
	
	public java.math.BigDecimal getAmt20_ar() {
		return amt20_ar;
	}

	public void setAmt20_ar(java.math.BigDecimal amt20Ar) {
		amt20_ar = amt20Ar;
	}

	public java.math.BigDecimal getAmt40gp_ar() {
		return amt40gp_ar;
	}

	public void setAmt40gp_ar(java.math.BigDecimal amt40gpAr) {
		amt40gp_ar = amt40gpAr;
	}

	public java.math.BigDecimal getAmt40hq_ar() {
		return amt40hq_ar;
	}

	public void setAmt40hq_ar(java.math.BigDecimal amt40hqAr) {
		amt40hq_ar = amt40hqAr;
	}

	public java.math.BigDecimal getAmt_ar() {
		return amt_ar;
	}

	public void setAmt_ar(java.math.BigDecimal amtAr) {
		amt_ar = amtAr;
	}

	private java.lang.Boolean istemplate;

	private java.lang.String templatename;

	private java.lang.String inputer;

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

	public int getPiece20() {
		return piece20;
	}

	public void setPiece20(int piece20) {
		this.piece20 = piece20;
	}

	public int getPiece40gp() {
		return piece40gp;
	}

	public void setPiece40gp(int piece40gp) {
		this.piece40gp = piece40gp;
	}

	public int getPiece40hq() {
		return piece40hq;
	}

	public void setPiece40hq(int piece40hq) {
		this.piece40hq = piece40hq;
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

	public String getCntypecode() {
		return cntypecode;
	}

	public void setCntypecode(String cntypecode) {
		this.cntypecode = cntypecode;
	}

	public java.math.BigDecimal getAmt45hq() {
		return amt45hq;
	}

	public void setAmt45hq(java.math.BigDecimal amt45hq) {
		this.amt45hq = amt45hq;
	}
	
}
