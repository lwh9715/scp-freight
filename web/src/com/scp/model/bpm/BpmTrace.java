package com.scp.model.bpm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "bpm_trace")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BpmTrace implements java.io.Serializable {
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;

	@Column(name = "processid")
	private java.lang.Long jobid;

	@Column(name = "processinstanceid")
	private java.lang.Long processinstanceid;
	
	@Column(name = "stepnumber")
	private java.lang.Integer stepnumber;
	
	@Column(name = "fromnode", length = 50)
	private java.lang.String fromnode;
	
	@Column(name = "tonode", length = 50)
	private java.lang.String tonode;
	
	@Column(name = "totype", length = 10)
	private java.lang.String totype;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.Long getJobid() {
		return jobid;
	}

	public void setJobid(java.lang.Long jobid) {
		this.jobid = jobid;
	}

	public java.lang.Long getProcessinstanceid() {
		return processinstanceid;
	}

	public void setProcessinstanceid(java.lang.Long processinstanceid) {
		this.processinstanceid = processinstanceid;
	}

	public java.lang.Integer getStepnumber() {
		return stepnumber;
	}

	public void setStepnumber(java.lang.Integer stepnumber) {
		this.stepnumber = stepnumber;
	}

	public java.lang.String getFromnode() {
		return fromnode;
	}

	public void setFromnode(java.lang.String fromnode) {
		this.fromnode = fromnode;
	}

	public java.lang.String getTonode() {
		return tonode;
	}

	public void setTonode(java.lang.String tonode) {
		this.tonode = tonode;
	}

	public java.lang.String getTotype() {
		return totype;
	}

	public void setTotype(java.lang.String totype) {
		this.totype = totype;
	}
	
	

}
