package com.scp.model.oa;

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
@Table(name = "oa_leaveapplication")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class OaLeaveapplication implements java.io.Serializable {

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
	@Column(name = "nos")
	private java.lang.String nos;

	/**
	 *@generated
	 */
	@Column(name = "singtime")
	private java.util.Date singtime;

	@Column(name = "userid")
	private java.lang.Long userid;

	@Column(name = "dept", length = 20)
	private java.lang.String dept;

	@Column(name = "jobdesc", length = 20)
	private java.lang.String jobdesc;

	@Column(name = "timestart")
	private java.util.Date timestart;

	@Column(name = "timend")
	private java.util.Date timend;

	@Column(name = "days")
	private java.math.BigDecimal days;

	@Column(name = "hours")
	private java.math.BigDecimal hours;

	@Column(name = "reason")
	private java.lang.String reason;

	@Column(name = "leavetype", length = 1)
	private java.lang.String leavetype;

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getNos() {
		return nos;
	}

	public void setNos(java.lang.String nos) {
		this.nos = nos;
	}

	public java.util.Date getSingtime() {
		return singtime;
	}

	public void setSingtime(java.util.Date singtime) {
		this.singtime = singtime;
	}

	public java.lang.Long getUserid() {
		return userid;
	}

	public void setUserid(java.lang.Long userid) {
		this.userid = userid;
	}

	public java.lang.String getDept() {
		return dept;
	}

	public void setDept(java.lang.String dept) {
		this.dept = dept;
	}

	public java.lang.String getJobdesc() {
		return jobdesc;
	}

	public void setJobdesc(java.lang.String jobdesc) {
		this.jobdesc = jobdesc;
	}

	public java.util.Date getTimestart() {
		return timestart;
	}

	public void setTimestart(java.util.Date timestart) {
		this.timestart = timestart;
	}

	public java.util.Date getTimend() {
		return timend;
	}

	public void setTimend(java.util.Date timend) {
		this.timend = timend;
	}

	public java.math.BigDecimal getDays() {
		return days;
	}

	public void setDays(java.math.BigDecimal days) {
		this.days = days;
	}

	public java.math.BigDecimal getHours() {
		return hours;
	}

	public void setHours(java.math.BigDecimal hours) {
		this.hours = hours;
	}

	public java.lang.String getReason() {
		return reason;
	}

	public void setReason(java.lang.String reason) {
		this.reason = reason;
	}

	public java.lang.String getLeavetype() {
		return leavetype;
	}

	public void setLeavetype(java.lang.String leavetype) {
		this.leavetype = leavetype;
	}

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
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

}
