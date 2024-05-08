package com.scp.model.ship;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "bus_goodstrack_temp")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusGoodstrackTemp implements java.io.Serializable {

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
	
	/**
	 *@generated
	 */
	@Column(name = "tmptype")
	private java.lang.String tmptype;
	
	@Column(name = "issys")
	private java.lang.Boolean issys;
	
	@Column(name = "isauto")
	private java.lang.Boolean isauto;
	

	/**
	 *@generated
	 */
	@Column(name = "orderno")
	private Integer orderno;
	
	@Column(name = "assigntype")
	private java.lang.String assigntype;
	
	/**
	 *@generated
	 */
	@Column(name = "assignuserid")
	private java.lang.Long assignuserid;
	
	/**
	 *@generated
	 */
	@Column(name = "iscs")
	private java.lang.Boolean iscs;
	
	@Column(name = "assignuserids")
	private java.lang.String assignuserids;
	
	@Column(name = "isinner")
	private java.lang.Boolean isinner;
	
	@Column(name = "weixintemp")
	private java.lang.String weixintemp;
	
	public Integer getOrderno() {
		return orderno;
	}

	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public java.lang.String getTmptype() {
		return tmptype;
	}

	public void setTmptype(java.lang.String tmptype) {
		this.tmptype = tmptype;
	}

	public java.lang.Boolean getIssys() {
		return issys;
	}

	public void setIssys(java.lang.Boolean issys) {
		this.issys = issys;
	}

	public java.lang.Boolean getIsauto() {
		return isauto;
	}

	public void setIsauto(java.lang.Boolean isauto) {
		this.isauto = isauto;
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

	public java.lang.Boolean getIscs() {
		return iscs;
	}

	public void setIscs(java.lang.Boolean iscs) {
		this.iscs = iscs;
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

	public java.lang.String getWeixintemp() {
		return weixintemp;
	}

	public void setWeixintemp(java.lang.String weixintemp) {
		this.weixintemp = weixintemp;
	}

}