package com.scp.model.ship;

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
@Table(name = "bus_ship_cost")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusShipCost implements java.io.Serializable {

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
	@Column(name = "scheduleid")
	private java.lang.Long scheduleid;

	/**
	 *@generated
	 */
	@Column(name = "sur", length = 10)
	private java.lang.String sur;

	/**
	 *@generated
	 */
	@Column(name = "via", length = 20)
	private java.lang.String via;

	/**
	 *@generated
	 */
	@Column(name = "tt", length = 10)
	private java.lang.String tt;

	/**
	 *@generated
	 */
	@Column(name = "freetime", length = 10)
	private java.lang.String freetime;

	/**
	 *@generated
	 */
	@Column(name = "pricer", length = 50)
	private java.lang.String pricer;

	/**
	 *@generated
	 */
	@Column(name = "pricefrom", length = 20)
	private java.lang.String pricefrom;

	/**
	 *@generated
	 */
	@Column(name = "pricetype", length = 10)
	private java.lang.String pricetype;

	/**
	 *@generated
	 */
	@Column(name = "corpid")
	private java.lang.Long corpid;

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
	@Column(name = "inputtime", length = 35)
	private java.util.Date inputtime;

	/**
	 *@generated
	 */
	@Column(name = "updater", length = 100)
	private java.lang.String updater;

	/**
	 *@generated
	 */
	@Column(name = "updatetime", length = 35)
	private java.util.Date updatetime;

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
	public java.lang.Long getScheduleid() {
		return this.scheduleid;
	}

	/**
	 *@generated
	 */
	public void setScheduleid(java.lang.Long value) {
		this.scheduleid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getSur() {
		return this.sur;
	}

	/**
	 *@generated
	 */
	public void setSur(java.lang.String value) {
		this.sur = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getVia() {
		return this.via;
	}

	/**
	 *@generated
	 */
	public void setVia(java.lang.String value) {
		this.via = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getTt() {
		return this.tt;
	}

	/**
	 *@generated
	 */
	public void setTt(java.lang.String value) {
		this.tt = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getFreetime() {
		return this.freetime;
	}

	/**
	 *@generated
	 */
	public void setFreetime(java.lang.String value) {
		this.freetime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getPricer() {
		return this.pricer;
	}

	/**
	 *@generated
	 */
	public void setPricer(java.lang.String value) {
		this.pricer = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getPricefrom() {
		return this.pricefrom;
	}

	/**
	 *@generated
	 */
	public void setPricefrom(java.lang.String value) {
		this.pricefrom = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getPricetype() {
		return this.pricetype;
	}

	/**
	 *@generated
	 */
	public void setPricetype(java.lang.String value) {
		this.pricetype = value;
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
}