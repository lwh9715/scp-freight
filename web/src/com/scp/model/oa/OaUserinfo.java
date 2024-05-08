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
@Table(name = "oa_userinfo")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class OaUserinfo implements java.io.Serializable {

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

	@Column(name = "uerid")
	private java.lang.Long uerid;

	@Column(name = "jobno", length = 30)
	private java.lang.String jobno;
	
	@Column(name = "departmentid")
	private java.lang.Long departmentid;
	
	public java.lang.Long getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(java.lang.Long departmentid) {
		this.departmentid = departmentid;
	}

	@Column(name = "corpid")
	private java.lang.Long corpid;
	
	@Column(name = "placejob")
	private java.lang.String placejob;
	
	@Column(name = "place")
	private java.lang.String place;
	
	@Column(name = "costcenter", length = 30)
	private java.lang.String costcenter;
	
	@Column(name = "jobs")
	private java.lang.String jobs;
	
	@Column(name = "namee", length = 50)
	private java.lang.String namee;
	
	@Column(name = "namec", length = 30)
	private java.lang.String namec;
	
	@Column(name = "sex", length = 1)
	private java.lang.String sex;
	

	@Column(name = "datein")
	private java.util.Date datein;
	
	@Column(name = "dateout")
	private java.util.Date dateout;
	
	@Column(name = "isemployed")
	private java.lang.Boolean isemployed;

	@Column(name = "isregular")
	private java.lang.Boolean isregular;
	
	@Column(name = "scorerecent")
	private java.lang.String scorerecent;
	
	@Column(name = "salaryplace")
	private java.lang.String salaryplace;
	
	@Column(name = "idno", length = 30)
	private java.lang.String idno;
	
	@Column(name = "idplace")
	private java.lang.String idplace;
	
	@Column(name = "domicileplace")
	private java.lang.String domicileplace;
	
	@Column(name = "birthdate")
	private java.util.Date birthdate;
	
	@Column(name = "regulardate")
	private java.util.Date regulardate;
	
	
	@Column(name = "originplace", length = 30)
	private java.lang.String originplace;
	
	@Column(name = "regplace")
	private java.lang.String regplace;
	
	@Column(name = "regtype")
	private java.lang.String regtype;
	
	
	@Column(name = "email")
	private java.lang.String email;
	
	
	@Column(name = "age", length = 30)
	private java.lang.Long age;
	
	@Column(name = "ismarried")
	private java.lang.Boolean ismarried;
	
	@Column(name = "cetlevel", length = 30)
	private java.lang.String cetlevel;
	
	
	@Column(name = "education", length = 30)
	private java.lang.String education;
	
	
	@Column(name = "graduationdate")
	private java.util.Date graduationdate;
	
	
	@Column(name = "school", length = 30)
	private java.lang.String school;
	
	@Column(name = "major", length = 30)
	private java.lang.String major;
	
	@Column(name = "titletime")
	private java.lang.String titletime;
	
	@Column(name = "mobilephone")
	private java.lang.String mobilephone;
	
	@Column(name = "secureprotocol")
	private java.lang.String secureprotocol;
	
	@Column(name = "securityaccount")
	private java.lang.String securityaccount;
	
	@Column(name = "fundaccount")
	private java.lang.String fundaccount;
	
	@Column(name = "accountbank")
	private java.lang.String accountbank;
	
	@Column(name = "bankno1")
	private java.lang.String bankno1;
	
	@Column(name = "bankno2")
	private java.lang.String bankno2;
	
	@Column(name = "emergencycontact")
	private java.lang.String emergencycontact;
	
	@Column(name = "emergencyphone")
	private java.lang.String emergencyphone;
	
	@Column(name = "serviceage",length=10)
	private java.lang.String serviceage;
	
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
	
	@Column(name = "am1")
	private java.util.Date am1;
	
	@Column(name = "am2")
	private java.util.Date am2;
	
	@Column(name = "pm1")
	private java.util.Date pm1;
	
	@Column(name = "pm2")
	private java.util.Date pm2;

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


	public java.lang.Long getUerid() {
		return uerid;
	}

	public void setUerid(java.lang.Long uerid) {
		this.uerid = uerid;
	}

	public java.lang.String getJobno() {
		return jobno;
	}

	public void setJobno(java.lang.String jobno) {
		this.jobno = jobno;
	}

	public java.lang.Long getCorpid() {
		return corpid;
	}

	public void setCorpid(java.lang.Long corpid) {
		this.corpid = corpid;
	}

	public java.lang.String getPlacejob() {
		return placejob;
	}

	public void setPlacejob(java.lang.String placejob) {
		this.placejob = placejob;
	}

	public java.lang.String getPlace() {
		return place;
	}

	public void setPlace(java.lang.String place) {
		this.place = place;
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

	public java.lang.String getNamee() {
		return namee;
	}

	public void setNamee(java.lang.String namee) {
		this.namee = namee;
	}

	public java.lang.String getNamec() {
		return namec;
	}

	public void setNamec(java.lang.String namec) {
		this.namec = namec;
	}

	public java.lang.String getSex() {
		return sex;
	}

	public void setSex(java.lang.String sex) {
		this.sex = sex;
	}

	public java.util.Date getDatein() {
		return datein;
	}

	public void setDatein(java.util.Date datein) {
		this.datein = datein;
	}

	public java.util.Date getDateout() {
		return dateout;
	}

	public void setDateout(java.util.Date dateout) {
		this.dateout = dateout;
	}

	public java.lang.Boolean getIsemployed() {
		return isemployed;
	}

	public void setIsemployed(java.lang.Boolean isemployed) {
		this.isemployed = isemployed;
	}

	public java.lang.Boolean getIsregular() {
		return isregular;
	}

	public void setIsregular(java.lang.Boolean isregular) {
		this.isregular = isregular;
	}

	public java.lang.String getScorerecent() {
		return scorerecent;
	}

	public void setScorerecent(java.lang.String scorerecent) {
		this.scorerecent = scorerecent;
	}


	public java.lang.String getSalaryplace() {
		return salaryplace;
	}

	public void setSalaryplace(java.lang.String salaryplace) {
		this.salaryplace = salaryplace;
	}

	public java.lang.String getIdno() {
		return idno;
	}

	public void setIdno(java.lang.String idno) {
		this.idno = idno;
	}

	public java.lang.String getIdplace() {
		return idplace;
	}

	public void setIdplace(java.lang.String idplace) {
		this.idplace = idplace;
	}

	public java.lang.String getDomicileplace() {
		return domicileplace;
	}

	public void setDomicileplace(java.lang.String domicileplace) {
		this.domicileplace = domicileplace;
	}

	public java.util.Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(java.util.Date birthdate) {
		this.birthdate = birthdate;
	}

	public java.lang.String getOriginplace() {
		return originplace;
	}

	public void setOriginplace(java.lang.String originplace) {
		this.originplace = originplace;
	}

	public java.lang.String getRegplace() {
		return regplace;
	}

	public void setRegplace(java.lang.String regplace) {
		this.regplace = regplace;
	}

	public java.lang.String getRegtype() {
		return regtype;
	}

	public void setRegtype(java.lang.String regtype) {
		this.regtype = regtype;
	}

	public java.lang.Long getAge() {
		return age;
	}

	public void setAge(java.lang.Long age) {
		this.age = age;
	}

	public java.lang.Boolean getIsmarried() {
		return ismarried;
	}

	public void setIsmarried(java.lang.Boolean ismarried) {
		this.ismarried = ismarried;
	}

	public java.lang.String getCetlevel() {
		return cetlevel;
	}

	public void setCetlevel(java.lang.String cetlevel) {
		this.cetlevel = cetlevel;
	}

	public java.lang.String getEducation() {
		return education;
	}

	public void setEducation(java.lang.String education) {
		this.education = education;
	}

	public java.util.Date getGraduationdate() {
		return graduationdate;
	}

	public void setGraduationdate(java.util.Date graduationdate) {
		this.graduationdate = graduationdate;
	}

	public java.lang.String getSchool() {
		return school;
	}

	public void setSchool(java.lang.String school) {
		this.school = school;
	}

	public java.lang.String getMajor() {
		return major;
	}

	public void setMajor(java.lang.String major) {
		this.major = major;
	}

	public java.lang.String getTitletime() {
		return titletime;
	}

	public void setTitletime(java.lang.String titletime) {
		this.titletime = titletime;
	}

	public java.lang.String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(java.lang.String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public java.lang.String getSecureprotocol() {
		return secureprotocol;
	}

	public void setSecureprotocol(java.lang.String secureprotocol) {
		this.secureprotocol = secureprotocol;
	}

	public java.lang.String getSecurityaccount() {
		return securityaccount;
	}

	public void setSecurityaccount(java.lang.String securityaccount) {
		this.securityaccount = securityaccount;
	}

	public java.lang.String getFundaccount() {
		return fundaccount;
	}

	public void setFundaccount(java.lang.String fundaccount) {
		this.fundaccount = fundaccount;
	}

	public java.lang.String getAccountbank() {
		return accountbank;
	}

	public void setAccountbank(java.lang.String accountbank) {
		this.accountbank = accountbank;
	}

	public java.lang.String getBankno1() {
		return bankno1;
	}

	public void setBankno1(java.lang.String bankno1) {
		this.bankno1 = bankno1;
	}

	public java.lang.String getBankno2() {
		return bankno2;
	}

	public void setBankno2(java.lang.String bankno2) {
		this.bankno2 = bankno2;
	}

	public java.lang.String getEmergencycontact() {
		return emergencycontact;
	}

	public void setEmergencycontact(java.lang.String emergencycontact) {
		this.emergencycontact = emergencycontact;
	}

	public java.lang.String getEmergencyphone() {
		return emergencyphone;
	}

	public void setEmergencyphone(java.lang.String emergencyphone) {
		this.emergencyphone = emergencyphone;
	}

	public java.lang.String getServiceage() {
		return serviceage;
	}

	public void setServiceage(java.lang.String serviceage) {
		this.serviceage = serviceage;
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

	public java.util.Date getRegulardate() {
		return regulardate;
	}

	public void setRegulardate(java.util.Date regulardate) {
		this.regulardate = regulardate;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.util.Date getAm1() {
		return am1;
	}

	public java.util.Date getAm2() {
		return am2;
	}

	public void setAm2(java.util.Date am2) {
		this.am2 = am2;
	}

	public java.util.Date getPm1() {
		return pm1;
	}

	public void setPm1(java.util.Date pm1) {
		this.pm1 = pm1;
	}

	public java.util.Date getPm2() {
		return pm2;
	}

	public void setPm2(java.util.Date pm2) {
		this.pm2 = pm2;
	}

	public void setAm1(java.util.Date am1) {
		this.am1 = am1;
	}

}
