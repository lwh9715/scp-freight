package com.scp.model.bpm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "bpm_task")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BpmTask implements java.io.Serializable {
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;

	@Column(name = "processid")
	private java.lang.Long processid;

	@Column(name = "processinstanceid")
	private java.lang.Long processinstanceid;
	
	@Column(name = "tasktype", length = 10)
	private java.lang.String tasktype;
	
	@Column(name = "taskname")
	private java.lang.String taskname;
	
	@Column(name = "tourl")
	private java.lang.String tourl;
	
	@Column(name = "state")
	private java.lang.Integer state;
	
	@Column(name = "suspended")
	private java.lang.Boolean suspended;
	
	@Column(name = "actorid", length = 50)
	private java.lang.String actorid;
	
	@Column(name = "createdtime", length = 29)
	private java.util.Date createdtime;
	
	@Column(name = "startedtime", length = 29)
	private java.util.Date startedtime;
	
	@Column(name = "expiredtime", length = 29)
	private java.util.Date expiredtime;
	
	@Column(name = "endtime", length = 29)
	private java.util.Date endtime;
	
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	@Column(name = "isback")
	private java.lang.Boolean isback;
	
	@Column(name = "comments")
	private java.lang.String comments;
	
	@Column(name = "commentime", length = 29)
	private java.util.Date commentime;
	
	@Column(name = "commentuserid")
	private java.lang.Long commentuserid;
	
	@Column(name = "status", length = 1)
	private java.lang.String status;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public java.lang.Long getProcessinstanceid() {
		return processinstanceid;
	}

	public void setProcessinstanceid(java.lang.Long processinstanceid) {
		this.processinstanceid = processinstanceid;
	}

	public java.lang.String getTasktype() {
		return tasktype;
	}

	public void setTasktype(java.lang.String tasktype) {
		this.tasktype = tasktype;
	}

	public java.lang.String getTourl() {
		return tourl;
	}

	public void setTourl(java.lang.String tourl) {
		this.tourl = tourl;
	}

	public java.lang.Integer getState() {
		return state;
	}

	public void setState(java.lang.Integer state) {
		this.state = state;
	}

	public java.lang.Boolean getSuspended() {
		return suspended;
	}

	public void setSuspended(java.lang.Boolean suspended) {
		this.suspended = suspended;
	}

	public java.lang.String getActorid() {
		return actorid;
	}

	public void setActorid(java.lang.String actorid) {
		this.actorid = actorid;
	}


	public java.util.Date getCreatedtime() {
		return createdtime;
	}

	public void setCreatedtime(java.util.Date createdtime) {
		this.createdtime = createdtime;
	}

	public java.util.Date getStartedtime() {
		return startedtime;
	}

	public void setStartedtime(java.util.Date startedtime) {
		this.startedtime = startedtime;
	}

	public java.util.Date getExpiredtime() {
		return expiredtime;
	}

	public void setExpiredtime(java.util.Date expiredtime) {
		this.expiredtime = expiredtime;
	}

	public java.util.Date getEndtime() {
		return endtime;
	}

	public void setEndtime(java.util.Date endtime) {
		this.endtime = endtime;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
	}

	public java.lang.Long getProcessid() {
		return processid;
	}

	public void setProcessid(java.lang.Long processid) {
		this.processid = processid;
	}

	public java.lang.String getTaskname() {
		return taskname;
	}

	public void setTaskname(java.lang.String taskname) {
		this.taskname = taskname;
	}

	public java.lang.Boolean getIsback() {
		return isback;
	}

	public void setIsback(java.lang.Boolean isback) {
		this.isback = isback;
	}

	public java.lang.String getComments() {
		return comments;
	}

	public void setComments(java.lang.String comments) {
		this.comments = comments;
	}

	public java.util.Date getCommentime() {
		return commentime;
	}

	public void setCommentime(java.util.Date commentime) {
		this.commentime = commentime;
	}

	public java.lang.Long getCommentuserid() {
		return commentuserid;
	}

	public void setCommentuserid(java.lang.Long commentuserid) {
		this.commentuserid = commentuserid;
	}

	public java.lang.String getStatus() {
		return status;
	}

	public void setStatus(java.lang.String status) {
		this.status = status;
	}
}
