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
@Table(name = "bus_optrack")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusOptrack implements java.io.Serializable {

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
	@Column(name = "optype", length = 1)
	private java.lang.String optype;

	/**
	 *@generated
	 */
	@Column(name = "opdesc")
	private java.lang.String opdesc;

	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String remarks;

	/**
	 *@generated
	 */
	@Column(name = "opusr")
	private java.lang.String opusr;
	
	
	
	@Column(name = "linkid")
	private java.lang.Long linkid;
	
	
	@Column(name = "linktbl")
	private java.lang.String linktbl;

	/**
	 *@generated
	 */
	@Column(name = "optime", length = 29)
	private java.util.Date optime;

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
	public java.lang.String getOptype() {
		return this.optype;
	}

	/**
	 *@generated
	 */
	public void setOptype(java.lang.String value) {
		this.optype = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getOpdesc() {
		return this.opdesc;
	}

	/**
	 *@generated
	 */
	public void setOpdesc(java.lang.String value) {
		this.opdesc = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getRemarks() {
		return this.remarks;
	}

	/**
	 *@generated
	 */
	public void setRemarks(java.lang.String value) {
		this.remarks = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getOptime() {
		return this.optime;
	}

	/**
	 *@generated
	 */
	public void setOptime(java.util.Date value) {
		this.optime = value;
	}

	public java.lang.String getOpusr() {
		return opusr;
	}

	public void setOpusr(java.lang.String opusr) {
		this.opusr = opusr;
	}

	public java.lang.Long getLinkid() {
		return linkid;
	}

	public void setLinkid(java.lang.Long linkid) {
		this.linkid = linkid;
	}

	public java.lang.String getLinktbl() {
		return linktbl;
	}

	public void setLinktbl(java.lang.String linktbl) {
		this.linktbl = linktbl;
	}

	
}