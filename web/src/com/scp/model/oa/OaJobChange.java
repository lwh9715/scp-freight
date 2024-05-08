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
@Table(name = "oa_job_change")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class OaJobChange implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	@Column(name = "userinfoid")
	private java.lang.Long userinfoid;

	@Column(name = "name", length = 30)
	private java.lang.String name;

	@Column(name = "dept", length = 30)
	private java.lang.String dept;
	
	@Column(name = "changetype", length = 1)
	private java.lang.String changetype;

	@Column(name = "datein")
	private java.util.Date datein;

	@Column(name = "dateconfirm")
	private java.util.Date dateconfirm;
	
	@Column(name = "deptfm",length=30)
	private java.lang.String deptfm;
	
	@Column(name = "deptto",length=30)
	private java.lang.String deptto;
	
	@Column(name = "jobfm",length=30)
	private java.lang.String jobfm;
	
	@Column(name = "jobto",length=30)
	private java.lang.String jobto;
	
	@Column(name = "salaryfm")
	private java.lang.Double salaryfm;

	@Column(name = "salaryto")
	private java.lang.Double salaryto;

	@Column(name = "remarks")
	private java.lang.String remarks;

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

	public java.lang.Long getUserinfoid() {
		return userinfoid;
	}

	public void setUserinfoid(java.lang.Long userinfoid) {
		this.userinfoid = userinfoid;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getDept() {
		return dept;
	}

	public void setDept(java.lang.String dept) {
		this.dept = dept;
	}

	public java.lang.String getChangetype() {
		return changetype;
	}

	public void setChangetype(java.lang.String changetype) {
		this.changetype = changetype;
	}

	public java.util.Date getDatein() {
		return datein;
	}

	public void setDatein(java.util.Date datein) {
		this.datein = datein;
	}

	public java.util.Date getDateconfirm() {
		return dateconfirm;
	}

	public void setDateconfirm(java.util.Date dateconfirm) {
		this.dateconfirm = dateconfirm;
	}

	public java.lang.String getDeptfm() {
		return deptfm;
	}

	public void setDeptfm(java.lang.String deptfm) {
		this.deptfm = deptfm;
	}

	public java.lang.String getDeptto() {
		return deptto;
	}

	public void setDeptto(java.lang.String deptto) {
		this.deptto = deptto;
	}

	public java.lang.String getJobfm() {
		return jobfm;
	}

	public void setJobfm(java.lang.String jobfm) {
		this.jobfm = jobfm;
	}

	public java.lang.String getJobto() {
		return jobto;
	}

	public void setJobto(java.lang.String jobto) {
		this.jobto = jobto;
	}

	
	
	public java.lang.Double getSalaryfm() {
		return salaryfm;
	}

	public void setSalaryfm(java.lang.Double salaryfm) {
		this.salaryfm = salaryfm;
	}

	public java.lang.Double getSalaryto() {
		return salaryto;
	}

	public void setSalaryto(java.lang.Double salaryto) {
		this.salaryto = salaryto;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
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
