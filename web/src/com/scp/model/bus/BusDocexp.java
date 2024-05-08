package com.scp.model.bus;

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
@Table(name = "bus_docexp")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusDocexp implements java.io.Serializable {

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
	@Column(name = "jobid")
	private java.lang.Long jobid;

	/**
	 *@generated
	 */
	@Column(name = "expno", length = 30)
	private java.lang.String expno;
	
	/**
	 *@generated
	 */
	@Column(name = "expno2", length = 30)
	private java.lang.String expno2;

	/**
	 *@generated
	 */
	@Column(name = "sendate", length = 13)
	private java.util.Date sendate;
	
	/**
	 *@generated
	 */
	@Column(name = "sendate2", length = 13)
	private java.util.Date sendate2;

	/**
	 *@generated
	 */
	@Column(name = "sender", length = 100)
	private java.lang.String sender;
	
	/**
	 *@generated
	 */
	@Column(name = "sender2", length = 100)
	private java.lang.String sender2;

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
	
	@Column(name = "remarks", length = 29)
	private String remarks;

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
	public java.lang.Long getJobid() {
		return this.jobid;
	}

	/**
	 *@generated
	 */
	public void setJobid(java.lang.Long value) {
		this.jobid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getExpno() {
		return this.expno;
	}

	/**
	 *@generated
	 */
	public void setExpno(java.lang.String value) {
		this.expno = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getSendate() {
		return this.sendate;
	}

	/**
	 *@generated
	 */
	public void setSendate(java.util.Date value) {
		this.sendate = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getSender() {
		return this.sender;
	}

	/**
	 *@generated
	 */
	public void setSender(java.lang.String value) {
		this.sender = value;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public java.lang.String getExpno2() {
		return expno2;
	}

	public void setExpno2(java.lang.String expno2) {
		this.expno2 = expno2;
	}

	public java.util.Date getSendate2() {
		return sendate2;
	}

	public void setSendate2(java.util.Date sendate2) {
		this.sendate2 = sendate2;
	}

	public java.lang.String getSender2() {
		return sender2;
	}

	public void setSender2(java.lang.String sender2) {
		this.sender2 = sender2;
	}
}