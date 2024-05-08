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
 *@generated
 */
@Table(name = "bus_pricedtl")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusPriceDtl implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	/**
	 *@generated
	 */
	@Column(name = "priceid")
	private java.lang.Long priceid;

	/**
	 *@generated
	 */
	@Column(name = "feeitemid")
	private java.lang.Long feeitemid;

	/**
	 *@generated
	 */
	@Column(name = "feeitemcode", length = 30)
	private java.lang.String feeitemcode;

	/**
	 *@generated
	 */
	@Column(name = "feeitemname", length = 100)
	private java.lang.String feeitemname;

	/**
	 *@generated
	 */
	@Column(name = "ppcc", length = 2)
	private java.lang.String ppcc;

	/**
	 *@generated
	 */
	@Column(name = "currency", length = 3)
	private java.lang.String currency;

	/**
	 *@generated
	 */
	@Column(name = "unit", length = 10)
	private java.lang.String unit;

	@Column(name = "amt20")
	private java.math.BigDecimal amt20;

	@Column(name = "amt40gp")
	private java.math.BigDecimal amt40gp;

	@Column(name = "amt40hq")
	private java.math.BigDecimal amt40hq;
	
	@Column(name = "amtother")
	private java.math.BigDecimal amtother;

	@Column(name = "amt")
	private java.math.BigDecimal amt;
	
	@Column(name = "amt20_ar")
	private java.math.BigDecimal amt20ar;

	@Column(name = "amt40gp_ar")
	private java.math.BigDecimal amt40gpar;

	@Column(name = "amt40hq_ar")
	private java.math.BigDecimal amt40hqar;
	
	@Column(name = "amtother_ar")
	private java.math.BigDecimal amtother_ar;

	@Column(name = "amt_ar")
	private java.math.BigDecimal amtar;

	@Column(name = "cbm")
	private java.math.BigDecimal cbm;
	/**
	 *@generated
	 */
	@Column(name = "piece20")
	private java.lang.Short piece20;

	/**
	 *@generated
	 */
	@Column(name = "piece40gp")
	private java.lang.Short piece40gp;

	/**
	 *@generated
	 */
	@Column(name = "piece40hq")
	private java.lang.Short piece40hq;

	/**
	 *@generated
	 */
	@Column(name = "piece")
	private java.lang.Short piece;

	@Column(name = "pieceother")
	private java.lang.Short pieceother;
	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String remarks;

	/**
	 *@generated
	 */
	@Column(name = "inputer", length = 30)
	private java.lang.String inputer;

	/**
	 *@generated
	 */
	@Column(name = "inputtime", length = 35)
	private java.util.Date inputtime;

	/**
	 *@generated
	 */
	@Column(name = "updater", length = 30)
	private java.lang.String updater;

	/**
	 *@generated
	 */
	@Column(name = "updatetime", length = 35)
	private java.util.Date updatetime;
	
	@Column(name = "cntypeothercode")
	private java.lang.String cntypeothercode;
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
	public java.lang.String getFeeitemcode() {
		return this.feeitemcode;
	}

	/**
	 *@generated
	 */
	public void setFeeitemcode(java.lang.String value) {
		this.feeitemcode = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getFeeitemname() {
		return this.feeitemname;
	}

	/**
	 *@generated
	 */
	public void setFeeitemname(java.lang.String value) {
		this.feeitemname = value;
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
	public java.math.BigDecimal getAmt20() {
		return this.amt20;
	}

	/**
	 *@generated
	 */
	public void setAmt20(java.math.BigDecimal value) {
		this.amt20 = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getAmt40gp() {
		return this.amt40gp;
	}

	/**
	 *@generated
	 */
	public void setAmt40gp(java.math.BigDecimal value) {
		this.amt40gp = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getAmt40hq() {
		return this.amt40hq;
	}

	/**
	 *@generated
	 */
	public void setAmt40hq(java.math.BigDecimal value) {
		this.amt40hq = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getAmt() {
		return this.amt;
	}

	/**
	 *@generated
	 */
	public void setAmt(java.math.BigDecimal value) {
		this.amt = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece20() {
		return this.piece20;
	}

	/**
	 *@generated
	 */
	public void setPiece20(java.lang.Short value) {
		this.piece20 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece40gp() {
		return this.piece40gp;
	}

	/**
	 *@generated
	 */
	public void setPiece40gp(java.lang.Short value) {
		this.piece40gp = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece40hq() {
		return this.piece40hq;
	}

	/**
	 *@generated
	 */
	public void setPiece40hq(java.lang.Short value) {
		this.piece40hq = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece() {
		return this.piece;
	}

	/**
	 *@generated
	 */
	public void setPiece(java.lang.Short value) {
		this.piece = value;
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

	public java.math.BigDecimal getAmt20ar() {
		return amt20ar;
	}

	public void setAmt20ar(java.math.BigDecimal amt20ar) {
		this.amt20ar = amt20ar;
	}

	public java.math.BigDecimal getAmt40gpar() {
		return amt40gpar;
	}

	public void setAmt40gpar(java.math.BigDecimal amt40gpar) {
		this.amt40gpar = amt40gpar;
	}

	public java.math.BigDecimal getAmt40hqar() {
		return amt40hqar;
	}

	public void setAmt40hqar(java.math.BigDecimal amt40hqar) {
		this.amt40hqar = amt40hqar;
	}

	public java.math.BigDecimal getAmtar() {
		return amtar;
	}

	public void setAmtar(java.math.BigDecimal amtar) {
		this.amtar = amtar;
	}

	public java.math.BigDecimal getAmtother() {
		return amtother;
	}

	public void setAmtother(java.math.BigDecimal amtother) {
		this.amtother = amtother;
	}

	public java.math.BigDecimal getAmtother_ar() {
		return amtother_ar;
	}

	public void setAmtother_ar(java.math.BigDecimal amtotherAr) {
		amtother_ar = amtotherAr;
	}

	public java.lang.Short getPieceother() {
		return pieceother;
	}

	public void setPieceother(java.lang.Short pieceother) {
		this.pieceother = pieceother;
	}

	public java.lang.String getCntypeothercode() {
		return cntypeothercode;
	}

	public void setCntypeothercode(java.lang.String cntypeothercode) {
		this.cntypeothercode = cntypeothercode;
	}

	public java.math.BigDecimal getCbm() {
		return cbm;
	}

	public void setCbm(java.math.BigDecimal cbm) {
		this.cbm = cbm;
	}

	public java.lang.Long getPriceid() {
		return priceid;
	}

	public void setPriceid(java.lang.Long priceid) {
		this.priceid = priceid;
	}
	
}