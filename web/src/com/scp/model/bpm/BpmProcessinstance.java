package com.scp.model.bpm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "bpm_processinstance")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BpmProcessinstance implements java.io.Serializable {
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "nos")
	private java.lang.String nos;

	@Column(name = "processid")
	private java.lang.Long processid;

	@Column(name = "displayname", length = 128)
	private java.lang.String displayname;
	
	@Column(name = "state")
	private java.lang.Integer state;
	
	@Column(name = "suspended")
	private java.lang.Boolean suspended;
	
	@Column(name = "version")
	private java.lang.Integer version;
	
	@Column(name = "createduser",length = 50)
	private java.lang.String createduser;
	
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
	
	@Column(name = "refno")
	private java.lang.String refno;
	
	@Column(name = "refid")
	private java.lang.String refid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

	public java.lang.String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(java.lang.String displayname) {
		this.displayname = displayname;
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

	public java.lang.Integer getVersion() {
		return version;
	}

	public void setVersion(java.lang.Integer version) {
		this.version = version;
	}

	public java.lang.String getCreateduser() {
		return createduser;
	}

	public void setCreateduser(java.lang.String createduser) {
		this.createduser = createduser;
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

	public java.lang.String getRefno() {
		return refno;
	}

	public void setRefno(java.lang.String refno) {
		this.refno = refno;
	}

	public java.lang.String getRefid() {
		return refid;
	}

	public void setRefid(java.lang.String refid) {
		this.refid = refid;
	}

	public java.lang.Long getProcessid() {
		return processid;
	}

	public void setProcessid(java.lang.Long processid) {
		this.processid = processid;
	}

	public java.lang.String getNos() {
		return nos;
	}

	public void setNos(java.lang.String nos) {
		this.nos = nos;
	}


}
