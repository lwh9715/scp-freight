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
@Table(name = "dat_exchangerate")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class DatExchangerate implements java.io.Serializable {

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
	@Column(name = "currencyfm", nullable = false, length = 3)
	private java.lang.String currencyfm;

	/**
	 *@generated
	 */
	@Column(name = "currencyto", nullable = false, length = 3)
	private java.lang.String currencyto;

	/**
	 *@generated
	 */
	@Column(name = "rate")
	private java.math.BigDecimal rate;

	/**
	 *@generated
	 */
	@Column(name = "datafm", length = 13)
	private java.util.Date datafm;

	/**
	 *@generated
	 */
	@Column(name = "datato", length = 13)
	private java.util.Date datato;

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

	/**
	 *@generated
	 */
	@Column(name = "xtype", nullable = false, length = 1)
	private java.lang.String xtype;
	
	
	@Column(name = "ratetype")
	private java.lang.String ratetype;
	
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
	public java.lang.String getCurrencyfm() {
		return this.currencyfm;
	}

	/**
	 *@generated
	 */
	public void setCurrencyfm(java.lang.String value) {
		this.currencyfm = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCurrencyto() {
		return this.currencyto;
	}

	/**
	 *@generated
	 */
	public void setCurrencyto(java.lang.String value) {
		this.currencyto = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getRate() {
		return this.rate;
	}

	/**
	 *@generated
	 */
	public void setRate(java.math.BigDecimal value) {
		this.rate = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getDatafm() {
		return this.datafm;
	}

	/**
	 *@generated
	 */
	public void setDatafm(java.util.Date value) {
		this.datafm = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getDatato() {
		return this.datato;
	}

	/**
	 *@generated
	 */
	public void setDatato(java.util.Date value) {
		this.datato = value;
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
	public java.lang.String getXtype() {
		return this.xtype;
	}

	/**
	 *@generated
	 */
	public void setXtype(java.lang.String value) {
		this.xtype = value;
	}

	public java.lang.String getRatetype() {
		return ratetype;
	}

	public void setRatetype(java.lang.String ratetype) {
		this.ratetype = ratetype;
	}

	public long getCorpid() {
		return corpid;
	}

	public void setCorpid(long corpid) {
		this.corpid = corpid;
	}
}