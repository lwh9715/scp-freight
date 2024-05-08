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
@Table(name = "oa_user_log_change")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class UserChange implements java.io.Serializable {

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
	@Column(name = "userinfoid")
	private java.lang.Long userinfoid;

	@Column(name = "logdate")
	private java.util.Date logdate;

	@Column(name = "place")
	private java.lang.String place;
	
	@Column(name = "changetype")
	private java.lang.String changetype;
	
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	@Column(name = "transpalce")
	private java.lang.String transpalce;
	
	@Column(name = "transduty")
	private java.lang.String transduty;

	@Column(name = "detail")
	private java.lang.String detail;

	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

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

	public java.lang.Long getUserinfoid() {
		return userinfoid;
	}

	public void setUserinfoid(java.lang.Long userinfoid) {
		this.userinfoid = userinfoid;
	}

	public java.util.Date getLogdate() {
		return logdate;
	}

	public void setLogdate(java.util.Date logdate) {
		this.logdate = logdate;
	}

	public java.lang.String getPlace() {
		return place;
	}

	public void setPlace(java.lang.String place) {
		this.place = place;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
	}

	public java.lang.String getDetail() {
		return detail;
	}

	public void setDetail(java.lang.String detail) {
		this.detail = detail;
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

	public java.lang.String getChangetype() {
		return changetype;
	}

	public void setChangetype(java.lang.String changetype) {
		this.changetype = changetype;
	}

	public java.lang.String getTranspalce() {
		return transpalce;
	}

	public void setTranspalce(java.lang.String transpalce) {
		this.transpalce = transpalce;
	}

	public java.lang.String getTransduty() {
		return transduty;
	}

	public void setTransduty(java.lang.String transduty) {
		this.transduty = transduty;
	}

}
