package com.scp.model.data;

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
@Table(name = "dat_driver")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class DatDriver implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy")
	private long id;
	
	@Column(name = "corpid")
	private Long corpid;

	public Long getCorpid() {
		return corpid;
	}

	public void setCorpid(Long corpid) {
		this.corpid = corpid;
	}

	/**
	 *@generated
	 */
	@Column(name = "code", nullable = false, length = 20)
	private java.lang.String code;

	/**
	 *@generated
	 */
	@Column(name = "namec", length = 50)
	private java.lang.String namec;

	/**
	 *@generated
	 */
	@Column(name = "namee", length = 50)
	private java.lang.String namee;

	/**
	 *@generated
	 */
	@Column(name = "tel", length = 20)
	private java.lang.String tel;

	/**
	 *@generated
	 */
	@Column(name = "mobile", length = 20)
	private java.lang.String mobile;

	/**
	 *@generated
	 */
	@Column(name = "nationality", length = 20)
	private java.lang.String nationality;

	/**
	 *@generated
	 */
	@Column(name = "sex", length = 1)
	private java.lang.String sex;

	/**
	 *@generated
	 */
	@Column(name = "ismarried")
	private java.lang.Boolean ismarried;

	/**
	 *@generated
	 */
	@Column(name = "indate", length = 13)
	private java.util.Date indate;

	/**
	 *@generated
	 */
	@Column(name = "address")
	private java.lang.String address;

	/**
	 *@generated
	 */
	@Column(name = "idcard", length = 20)
	private java.lang.String idcard;

	/**
	 *@generated
	 */
	@Column(name = "idenddate", length = 13)
	private java.util.Date idenddate;

	/**
	 *@generated
	 */
	@Column(name = "birthday", length = 13)
	private java.util.Date birthday;

	/**
	 *@generated
	 */
	@Column(name = "drvage")
	private java.lang.Short drvage;

	/**
	 *@generated
	 */
	@Column(name = "licno", length = 20)
	private java.lang.String licno;

	/**
	 *@generated
	 */
	@Column(name = "licgetdate", length = 13)
	private java.util.Date licgetdate;

	/**
	 *@generated
	 */
	@Column(name = "licenddate", length = 13)
	private java.util.Date licenddate;

	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String remarks;

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
	public java.lang.String getNamee() {
		return this.namee;
	}

	/**
	 *@generated
	 */
	public void setNamee(java.lang.String value) {
		this.namee = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getTel() {
		return this.tel;
	}

	/**
	 *@generated
	 */
	public void setTel(java.lang.String value) {
		this.tel = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getMobile() {
		return this.mobile;
	}

	/**
	 *@generated
	 */
	public void setMobile(java.lang.String value) {
		this.mobile = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getNationality() {
		return this.nationality;
	}

	/**
	 *@generated
	 */
	public void setNationality(java.lang.String value) {
		this.nationality = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getSex() {
		return this.sex;
	}

	/**
	 *@generated
	 */
	public void setSex(java.lang.String value) {
		this.sex = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIsmarried() {
		return this.ismarried;
	}

	/**
	 *@generated
	 */
	public void setIsmarried(java.lang.Boolean value) {
		this.ismarried = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getIndate() {
		return this.indate;
	}

	/**
	 *@generated
	 */
	public void setIndate(java.util.Date value) {
		this.indate = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getAddress() {
		return this.address;
	}

	/**
	 *@generated
	 */
	public void setAddress(java.lang.String value) {
		this.address = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getIdcard() {
		return this.idcard;
	}

	/**
	 *@generated
	 */
	public void setIdcard(java.lang.String value) {
		this.idcard = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getIdenddate() {
		return this.idenddate;
	}

	/**
	 *@generated
	 */
	public void setIdenddate(java.util.Date value) {
		this.idenddate = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getBirthday() {
		return this.birthday;
	}

	/**
	 *@generated
	 */
	public void setBirthday(java.util.Date value) {
		this.birthday = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getDrvage() {
		return this.drvage;
	}

	/**
	 *@generated
	 */
	public void setDrvage(java.lang.Short value) {
		this.drvage = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getLicno() {
		return this.licno;
	}

	/**
	 *@generated
	 */
	public void setLicno(java.lang.String value) {
		this.licno = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getLicgetdate() {
		return this.licgetdate;
	}

	/**
	 *@generated
	 */
	public void setLicgetdate(java.util.Date value) {
		this.licgetdate = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getLicenddate() {
		return this.licenddate;
	}

	/**
	 *@generated
	 */
	public void setLicenddate(java.util.Date value) {
		this.licenddate = value;
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
}