package com.scp.model.ship;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "bus_goodstrack")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusGoodstrack implements java.io.Serializable {

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
	@Column(name = "fkid")
	private java.lang.Long fkid;

	/**
	 *@generated
	 */
	@Column(name = "linkid")
	private java.lang.Long linkid;

	/**
	 *@generated
	 */
	@Column(name = "dealdate")
	private java.lang.String dealdate;

	/**
	 *@generated
	 */
	@Column(name = "context")
	private java.lang.String context;

	/**
	 *@generated
	 */
	@Column(name = "srouce")
	private java.lang.String srouce;

	/**
	 *@generated
	 */
	@Column(name = "inputer")
	private java.lang.String inputer;

	/**
	 *@generated
	 */
	@Column(name = "inputtime")
	private java.util.Date inputtime;

	/**
	 *@generated
	 */
	@Column(name = "iscs")
	private java.lang.Boolean iscs;
	
	
	@Column(name = "isfinish")
	private java.lang.Boolean isfinish;
	
	/**
	 *@generated
	 */
	@Column(name = "locationc")
	private java.lang.String locationc;
	
	/**
	 *@generated
	 */
	@Column(name = "locatione")
	private java.lang.String locatione;
	
	/**
	 *@generated
	 */
	@Column(name = "statusc")
	private java.lang.String statusc;
	
	/**
	 *@generated
	 */
	@Column(name = "statuse")
	private java.lang.String statuse;
	
	
	@Column(name = "updater")
	private java.lang.String updater;
	
	@Column(name = "updatetime")
	private java.util.Date updatetime;
	
	@Column(name = "orderno")
	private java.lang.Integer orderno;
	
	@Column(name = "assigntype")
	private java.lang.String assigntype;
	
	/**
	 *@generated
	 */
	@Column(name = "assignuserid")
	private java.lang.Long assignuserid;

	@Column(name = "assignuserids")
	private java.lang.String assignuserids;
	
	@Column(name = "isinner")
	private java.lang.Boolean isinner;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.Long getFkid() {
		return fkid;
	}

	public void setFkid(java.lang.Long fkid) {
		this.fkid = fkid;
	}

	public java.lang.Long getLinkid() {
		return linkid;
	}

	public void setLinkid(java.lang.Long linkid) {
		this.linkid = linkid;
	}


	public java.lang.String getContext() {
		return context;
	}

	public void setContext(java.lang.String context) {
		this.context = context;
	}

	public java.lang.String getSrouce() {
		return srouce;
	}

	public void setSrouce(java.lang.String srouce) {
		this.srouce = srouce;
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

	public java.lang.Boolean getIscs() {
		return iscs;
	}

	public void setIscs(java.lang.Boolean iscs) {
		this.iscs = iscs;
	}

	public java.lang.String getLocationc() {
		return locationc;
	}

	public void setLocationc(java.lang.String locationc) {
		this.locationc = locationc;
	}

	public java.lang.String getLocatione() {
		return locatione;
	}

	public void setLocatione(java.lang.String locatione) {
		this.locatione = locatione;
	}

	public java.lang.String getStatusc() {
		return statusc;
	}

	public void setStatusc(java.lang.String statusc) {
		this.statusc = statusc;
	}

	public java.lang.String getStatuse() {
		return statuse;
	}

	public void setStatuse(java.lang.String statuse) {
		this.statuse = statuse;
	}

	public java.lang.String getDealdate() {
		return dealdate;
	}

	public void setDealdate(java.lang.String dealdate) {
		this.dealdate = dealdate;
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

	public java.lang.Boolean getIsfinish() {
		return isfinish;
	}

	public void setIsfinish(java.lang.Boolean isfinish) {
		this.isfinish = isfinish;
	}

	public java.lang.Integer getOrderno() {
		return orderno;
	}

	public void setOrderno(java.lang.Integer orderno) {
		this.orderno = orderno;
	}

	public java.lang.String getAssigntype() {
		return assigntype;
	}

	public void setAssigntype(java.lang.String assigntype) {
		this.assigntype = assigntype;
	}

	public java.lang.Long getAssignuserid() {
		return assignuserid;
	}

	public void setAssignuserid(java.lang.Long assignuserid) {
		this.assignuserid = assignuserid;
	}

	public java.lang.String getAssignuserids() {
		return assignuserids;
	}

	public void setAssignuserids(java.lang.String assignuserids) {
		this.assignuserids = assignuserids;
	}

	public java.lang.Boolean getIsinner() {
		return isinner;
	}

	public void setIsinner(java.lang.Boolean isinner) {
		this.isinner = isinner;
	}
	
}