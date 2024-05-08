package com.scp.model.sys;

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
@Table(name = "sys_custlib")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class SysCustlib implements java.io.Serializable {

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
	@Column(name = "code", length = 20)
	private java.lang.String code;

	/**
	 *@generated
	 */
	@Column(name = "namec", length = 30)
	private java.lang.String namec;

	/**
	 *@generated
	 */
	@Column(name = "rowno")
	private java.lang.Short rowno;

	/**
	 *@generated
	 */
	@Column(name = "colno")
	private java.lang.Short colno;
	
	@Column(name = "corper", length = 30)
	private java.lang.String corper;
	
	@Column(name = "depter", length = 30)
	private java.lang.String depter;
	
	@Column(name = "jobdesc", length = 30)
	private java.lang.String jobdesc;
	
	@Column(name = "libtype", length = 1)
	private java.lang.String libtype;

	@Column(name = "userid")
	private java.lang.Long userid;
	
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	@Column(name = "isys")
	private java.lang.Boolean isys;
	
	
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
	public java.lang.String getCode() {
		return this.code;
	}

	/**
	 *@generated
	 */
	public void setCode(java.lang.String value) {
		this.code = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getNamec() {
		return this.namec;
	}

	/**
	 *@generated
	 */
	public void setNamec(java.lang.String value) {
		this.namec = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getRowno() {
		return this.rowno;
	}

	/**
	 *@generated
	 */
	public void setRowno(java.lang.Short value) {
		this.rowno = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getColno() {
		return this.colno;
	}

	/**
	 *@generated
	 */
	public void setColno(java.lang.Short value) {
		this.colno = value;
	}

	public java.lang.String getCorper() {
		return corper;
	}

	public void setCorper(java.lang.String corper) {
		this.corper = corper;
	}

	public java.lang.String getDepter() {
		return depter;
	}

	public void setDepter(java.lang.String depter) {
		this.depter = depter;
	}

	public java.lang.String getJobdesc() {
		return jobdesc;
	}

	public void setJobdesc(java.lang.String jobdesc) {
		this.jobdesc = jobdesc;
	}

	public java.lang.String getLibtype() {
		return libtype;
	}

	public void setLibtype(java.lang.String libtype) {
		this.libtype = libtype;
	}

	public java.lang.Long getUserid() {
		return userid;
	}

	public void setUserid(java.lang.Long userid) {
		this.userid = userid;
	}

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
	}

	public java.lang.Boolean getIsys() {
		return isys;
	}

	public void setIsys(java.lang.Boolean isys) {
		this.isys = isys;
	}
	
}