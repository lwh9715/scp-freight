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
@Table(name = "sys_addresslist")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysAddresslist implements java.io.Serializable {

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
	@Column(name = "userid")
	private java.lang.Long userid;

	/**
	 *@generated
	 */
	@Column(name = "ispublic")
	private java.lang.Boolean ispublic;

	/**
	 *@generated
	 */
	@Column(name = "code", length = 20)
	private java.lang.String code;

	/**
	 *@generated
	 */
	@Column(name = "namec", length = 100)
	private java.lang.String namec;

	/**
	 *@generated
	 */
	@Column(name = "namee", length = 100)
	private java.lang.String namee;

	/**
	 *@generated
	 */
	@Column(name = "tel1", length = 20)
	private java.lang.String tel1;

	/**
	 *@generated
	 */
	@Column(name = "tel2", length = 20)
	private java.lang.String tel2;

	/**
	 *@generated
	 */
	@Column(name = "fax1", length = 20)
	private java.lang.String fax1;

	/**
	 *@generated
	 */
	@Column(name = "fax2", length = 20)
	private java.lang.String fax2;

	/**
	 *@generated
	 */
	@Column(name = "email1", length = 30)
	private java.lang.String email1;

	/**
	 *@generated
	 */
	@Column(name = "email2", length = 30)
	private java.lang.String email2;

	/**
	 *@generated
	 */
	@Column(name = "qq", length = 30)
	private java.lang.String qq;

	/**
	 *@generated
	 */
	@Column(name = "msn", length = 30)
	private java.lang.String msn;

	/**
	 *@generated
	 */
	@Column(name = "mobilephone", length = 30)
	private java.lang.String mobilephone;

	/**
	 *@generated
	 */
	@Column(name = "skype", length = 30)
	private java.lang.String skype;

	/**
	 *@generated
	 */
	@Column(name = "fax", length = 30)
	private java.lang.String fax;

	/**
	 *@generated
	 */
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

	
	@Column(name = "customerabbr")
	private java.lang.String customerabbr;
	
	
	@Column(name = "jobtitle")
	private java.lang.String jobtitle;
	
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
	public java.lang.Long getUserid() {
		return this.userid;
	}

	/**
	 *@generated
	 */
	public void setUserid(java.lang.Long value) {
		this.userid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIspublic() {
		return this.ispublic;
	}

	/**
	 *@generated
	 */
	public void setIspublic(java.lang.Boolean value) {
		this.ispublic = value;
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
	public java.lang.String getTel1() {
		return this.tel1;
	}

	/**
	 *@generated
	 */
	public void setTel1(java.lang.String value) {
		this.tel1 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getTel2() {
		return this.tel2;
	}

	/**
	 *@generated
	 */
	public void setTel2(java.lang.String value) {
		this.tel2 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getFax1() {
		return this.fax1;
	}

	/**
	 *@generated
	 */
	public void setFax1(java.lang.String value) {
		this.fax1 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getFax2() {
		return this.fax2;
	}

	/**
	 *@generated
	 */
	public void setFax2(java.lang.String value) {
		this.fax2 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getEmail1() {
		return this.email1;
	}

	/**
	 *@generated
	 */
	public void setEmail1(java.lang.String value) {
		this.email1 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getEmail2() {
		return this.email2;
	}

	/**
	 *@generated
	 */
	public void setEmail2(java.lang.String value) {
		this.email2 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getQq() {
		return this.qq;
	}

	/**
	 *@generated
	 */
	public void setQq(java.lang.String value) {
		this.qq = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getMsn() {
		return this.msn;
	}

	/**
	 *@generated
	 */
	public void setMsn(java.lang.String value) {
		this.msn = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getMobilephone() {
		return this.mobilephone;
	}

	/**
	 *@generated
	 */
	public void setMobilephone(java.lang.String value) {
		this.mobilephone = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getSkype() {
		return this.skype;
	}

	/**
	 *@generated
	 */
	public void setSkype(java.lang.String value) {
		this.skype = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getFax() {
		return this.fax;
	}

	/**
	 *@generated
	 */
	public void setFax(java.lang.String value) {
		this.fax = value;
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

	public java.lang.String getCustomerabbr() {
		return customerabbr;
	}

	public void setCustomerabbr(java.lang.String customerabbr) {
		this.customerabbr = customerabbr;
	}

	public java.lang.String getJobtitle() {
		return jobtitle;
	}

	public void setJobtitle(java.lang.String jobtitle) {
		this.jobtitle = jobtitle;
	}
}