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
@Table(name = "bus_ship_bookdtl")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusShipBookdtl implements java.io.Serializable {

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
	@Column(name = "bookingid")
	private java.lang.Long bookingid;

	/**
	 *@generated
	 */
	@Column(name = "bookno", length = 30)
	private java.lang.String bookno;

	/**
	 *@generated
	 */
	@Column(name = "cntypeid")
	private java.lang.Long cntypeid;

	/**
	 *@generated
	 */
	@Column(name = "bookstate", nullable = false, length = 1)
	private java.lang.String bookstate;

	/**
	 *@generated
	 */
	@Column(name = "userid_assign")
	private java.lang.Long useridAssign;

	/**
	 *@generated
	 */
	@Column(name = "userid_use")
	private java.lang.Long useridUse;

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
	
	@Column(name = "nextshipcount")
	private java.lang.Integer nextshipcount;
	
	/**
	 *@generated
	 */
	@Column(name = "nextshipremarks")
	private java.lang.String nextshipremarks;
	
	/**
	 *@generated
	 */
	@Column(name = "dropcntcount")
	private java.lang.Integer dropcntcount;
	
	/**
	 *@generated
	 */
	@Column(name = "dropcntremarks")
	private java.lang.String dropcntremarks;


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
	public java.lang.Long getBookingid() {
		return this.bookingid;
	}

	/**
	 *@generated
	 */
	public void setBookingid(java.lang.Long value) {
		this.bookingid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getBookno() {
		return this.bookno;
	}

	/**
	 *@generated
	 */
	public void setBookno(java.lang.String value) {
		this.bookno = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getCntypeid() {
		return this.cntypeid;
	}

	/**
	 *@generated
	 */
	public void setCntypeid(java.lang.Long value) {
		this.cntypeid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getBookstate() {
		return this.bookstate;
	}

	/**
	 *@generated
	 */
	public void setBookstate(java.lang.String value) {
		this.bookstate = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getUseridAssign() {
		return this.useridAssign;
	}

	/**
	 *@generated
	 */
	public void setUseridAssign(java.lang.Long value) {
		this.useridAssign = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getUseridUse() {
		return this.useridUse;
	}

	/**
	 *@generated
	 */
	public void setUseridUse(java.lang.Long value) {
		this.useridUse = value;
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
	
	public java.lang.Integer getNextshipcount() {
		return nextshipcount;
	}

	public java.lang.String getNextshipremarks() {
		return nextshipremarks;
	}

	public java.lang.Integer getDropcntcount() {
		return dropcntcount;
	}

	public java.lang.String getDropcntremarks() {
		return dropcntremarks;
	}

	public void setNextshipcount(java.lang.Integer nextshipcount) {
		this.nextshipcount = nextshipcount;
	}

	public void setNextshipremarks(java.lang.String nextshipremarks) {
		this.nextshipremarks = nextshipremarks;
	}

	public void setDropcntcount(java.lang.Integer dropcntcount) {
		this.dropcntcount = dropcntcount;
	}

	public void setDropcntremarks(java.lang.String dropcntremarks) {
		this.dropcntremarks = dropcntremarks;
	}
	
}