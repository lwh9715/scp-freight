package com.scp.model.data;

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
@Table(name = "dat_cntype")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class DatCntype implements java.io.Serializable {

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
	@Column(name = "code", nullable = false, length = 20)
	private java.lang.String code;

	/**
	 *@generated
	 */
	@Column(name = "l")
	private java.math.BigDecimal l;

	/**
	 *@generated
	 */
	@Column(name = "w")
	private java.math.BigDecimal w;

	/**
	 *@generated
	 */
	@Column(name = "h")
	private java.math.BigDecimal h;

	/**
	 *@generated
	 */
	@Column(name = "cbm")
	private java.math.BigDecimal cbm;

	/**
	 *@generated
	 */
	@Column(name = "ton")
	private java.math.BigDecimal ton;

	/**
	 *@generated
	 */
	@Column(name = "qty")
	private java.lang.Short qty;

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
	
	@Column(name = "istop")
	private java.lang.Boolean istop;
	
	@Column(name = "corpid")
	private long corpid;

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
	public java.lang.String getCode() {
		return this.code;
	}

	/**
	 *@generated
	 */
	public void setCode(java.lang.String value) {
		this.code = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getL() {
		return this.l;
	}

	/**
	 *@generated
	 */
	public void setL(java.math.BigDecimal value) {
		this.l = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getW() {
		return this.w;
	}

	/**
	 *@generated
	 */
	public void setW(java.math.BigDecimal value) {
		this.w = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getH() {
		return this.h;
	}

	/**
	 *@generated
	 */
	public void setH(java.math.BigDecimal value) {
		this.h = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getCbm() {
		return this.cbm;
	}

	/**
	 *@generated
	 */
	public void setCbm(java.math.BigDecimal value) {
		this.cbm = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getTon() {
		return this.ton;
	}

	/**
	 *@generated
	 */
	public void setTon(java.math.BigDecimal value) {
		this.ton = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getQty() {
		return this.qty;
	}

	/**
	 *@generated
	 */
	public void setQty(java.lang.Short value) {
		this.qty = value;
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

	public java.lang.Boolean getIstop() {
		return istop;
	}

	public void setIstop(java.lang.Boolean istop) {
		this.istop = istop;
	}

	public long getCorpid() {
		return corpid;
	}

	public void setCorpid(long corpid) {
		this.corpid = corpid;
	}
}