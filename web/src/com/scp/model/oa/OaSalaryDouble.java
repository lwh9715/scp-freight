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
@Table(name = "oa_salary_double")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class OaSalaryDouble implements java.io.Serializable {

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
	@Column(name = "sno")
	private java.lang.String sno;

	@Column(name = "userinfoid")
	private java.lang.Long userinfoid;


	@Column(name = "salaryplace")
	private java.lang.String salaryplace;
	
	@Column(name = "yearno")
	private java.lang.String yearno;
	
	@Column(name = "monthno")
	private java.lang.String monthno;
	
	@Column(name = "costcenter", length = 30)
	private java.lang.String costcenter;
	
	@Column(name = "jobs")
	private java.lang.String jobs;
	
	@Column(name = "placejob")
	private java.lang.String placejob;
	
	@Column(name = "namec", length = 30)
	private java.lang.String namec;
	
	@Column(name = "datein")
	private java.util.Date datein;
	
	@Column(name = "validate")
	private java.lang.Long validate;
	
	@Column(name = "doublbase")
	private java.lang.Double doublbase;
	
	@Column(name = "doubleout")
	private java.lang.Double doubleout;
	
	@Column(name = "currency")
	private java.lang.String currency;
	
	@Column(name = "remarks")
	private java.lang.String remarks;
	
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

	public java.lang.String getSno() {
		return sno;
	}

	public void setSno(java.lang.String sno) {
		this.sno = sno;
	}

	public java.lang.Long getUserinfoid() {
		return userinfoid;
	}

	public void setUserinfoid(java.lang.Long userinfoid) {
		this.userinfoid = userinfoid;
	}
	

	public java.lang.String getYearno() {
		return yearno;
	}

	public void setYearno(java.lang.String yearno) {
		this.yearno = yearno;
	}

	public java.lang.String getMonthno() {
		return monthno;
	}

	public void setMonthno(java.lang.String monthno) {
		this.monthno = monthno;
	}

	public java.lang.String getSalaryplace() {
		return salaryplace;
	}

	public void setSalaryplace(java.lang.String salaryplace) {
		this.salaryplace = salaryplace;
	}

	public java.lang.String getCostcenter() {
		return costcenter;
	}

	public void setCostcenter(java.lang.String costcenter) {
		this.costcenter = costcenter;
	}

	public java.lang.String getJobs() {
		return jobs;
	}

	public void setJobs(java.lang.String jobs) {
		this.jobs = jobs;
	}

	public java.lang.String getPlacejob() {
		return placejob;
	}

	public void setPlacejob(java.lang.String placejob) {
		this.placejob = placejob;
	}

	public java.lang.String getNamec() {
		return namec;
	}

	public void setNamec(java.lang.String namec) {
		this.namec = namec;
	}

	public java.util.Date getDatein() {
		return datein;
	}

	public void setDatein(java.util.Date datein) {
		this.datein = datein;
	}

	public java.lang.Long getValidate() {
		return validate;
	}

	public void setValidate(java.lang.Long validate) {
		this.validate = validate;
	}

	public java.lang.Double getDoublbase() {
		return doublbase;
	}

	public void setDoublbase(java.lang.Double doublbase) {
		this.doublbase = doublbase;
	}

	public java.lang.Double getDoubleout() {
		return doubleout;
	}

	public void setDoubleout(java.lang.Double doubleout) {
		this.doubleout = doubleout;
	}

	public java.lang.String getCurrency() {
		return currency;
	}

	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
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
