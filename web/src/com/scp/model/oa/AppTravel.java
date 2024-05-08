package com.scp.model.oa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "oa_apply_travel")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public  class AppTravel implements java.io.Serializable {

	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	@Column(name = "userinfoid")
	private long userinfoid;
	
	@Column(name = "applydate")
	private java.util.Date applydate;
	
	@Column(name = "name",length=30)
	private java.lang.String name;
	
	@Column(name = "dept",length=30)
	private java.lang.String dept;
	
	@Column(name = "toplace")
	private java.lang.String toplace;
	
	@Column(name = "traffictools",length=30)
	private java.lang.String traffictools;
	
	@Column(name = "dateconfirm")
	private java.lang.String dateconfirm;
	
	@Column(name = "deptfm")
	private java.util.Date deptfm;
	
	@Column(name = "deptto")
	private java.util.Date deptto;
	
	@Column(name = "daycount")
	private double daycount;
	
	@Column(name = "purpose")
	private java.lang.String purpose;
	
	@Column(name = "salaryfm")
	private java.lang.String salaryfm;

	@Column(name = "isborrow")
	private boolean isborrow;
	
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	@Column(name = "isdelete")
	private boolean isdelete;
	
	@Column(name = "inputer")
	private java.lang.String inputer;
	
	@Column(name = "inputtime")
	private java.util.Date inputtime;
	
	@Column(name = "updater")
	private java.lang.String updater;
	
	@Column(name = "updatetime")
	private java.util.Date updatetime;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserinfoid() {
		return this.userinfoid;
	}

	public void setUserinfoid(long userinfoid) {
		this.userinfoid = userinfoid;
	}

	public java.util.Date getApplydate() {
		return this.applydate;
	}

	public void setApplydate(Date applydate) {
		this.applydate = applydate;
	}

	public java.lang.String getName() {
		return this.name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getDept() {
		return this.dept;
	}

	public void setDept(java.lang.String dept) {
		this.dept = dept;
	}

	public java.lang.String getToplace() {
		return this.toplace;
	}

	public void setToplace(java.lang.String toplace) {
		this.toplace = toplace;
	}

	public java.lang.String getTraffictools() {
		return this.traffictools;
	}

	public void setTraffictools(java.lang.String traffictools) {
		this.traffictools = traffictools;
	}

	public java.lang.String getDateconfirm() {
		return this.dateconfirm;
	}

	public void setDateconfirm(java.lang.String dateconfirm) {
		this.dateconfirm = dateconfirm;
	}

	public java.util.Date getDeptfm() {
		return this.deptfm;
	}

	public void setDeptfm(Date deptfm) {
		this.deptfm = deptfm;
	}

	public java.util.Date getDeptto() {
		return this.deptto;
	}

	public void setDeptto(Date deptto) {
		this.deptto = deptto;
	}

	public double getDaycount() {
		return this.daycount;
	}

	public void setDaycount(double daycount) {
		this.daycount = daycount;
	}

	public java.lang.String getPurpose() {
		return this.purpose;
	}

	public void setPurpose(java.lang.String purpose) {
		this.purpose = purpose;
	}

	public java.lang.String getSalaryfm() {
		return this.salaryfm;
	}

	public void setSalaryfm(java.lang.String salaryfm) {
		this.salaryfm = salaryfm;
	}

	public boolean getIsborrow() {
		return this.isborrow;
	}

	public void setIsborrow(boolean isborrow) {
		this.isborrow = isborrow;
	}

	public java.lang.String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
	}

	public boolean getIsdelete() {
		return this.isdelete;
	}

	public void setIsdelete(boolean isdelete) {
		this.isdelete = isdelete;
	}

	public java.lang.String getInputer() {
		return this.inputer;
	}

	public void setInputer(java.lang.String inputer) {
		this.inputer = inputer;
	}

	public java.util.Date getInputtime() {
		return this.inputtime;
	}

	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}

	public java.lang.String getUpdater() {
		return this.updater;
	}

	public void setUpdater(java.lang.String updater) {
		this.updater = updater;
	}

	public java.util.Date getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

}