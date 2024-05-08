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
@Table(name = "sys_corpcontacts")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysCorpcontacts implements java.io.Serializable {

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
	@Column(name = "customerid")
	private java.lang.Long customerid;

	/**
	 *@generated
	 */
	@Column(name = "customerabbr", length = 100)
	private java.lang.String customerabbr;

	/**
	 *@generated
	 */
	@Column(name = "name", length = 200)
	private java.lang.String name;

	/**
	 *@generated
	 */
	@Column(name = "sex", nullable = false, length = 1)
	private java.lang.String sex;

	/**
	 *@generated
	 */
	@Column(name = "phone", length = 20)
	private java.lang.String phone;

	/**
	 *@generated
	 */
	@Column(name = "tel", length = 20)
	private java.lang.String tel;
	
	@Column(name = "fax", length = 20)
	private java.lang.String fax;

	/**
	 *@generated
	 */
	@Column(name = "address", length = 200)
	private java.lang.String address;

	/**
	 *@generated
	 */
	@Column(name = "qq", length = 20)
	private java.lang.String qq;

	/**
	 *@generated
	 */
	@Column(name = "msn", length = 20)
	private java.lang.String msn;

	/**
	 *@generated
	 */
	@Column(name = "email", length = 20)
	private java.lang.String email;

	/**
	 *@generated
	 */
	@Column(name = "habit", length = 200)
	private java.lang.String habit;

	/**
	 *@generated
	 */
	@Column(name = "duty", length = 200)
	private java.lang.String duty;

	/**
	 *@generated
	 */
	@Column(name = "jobdesc", length = 200)
	private java.lang.String jobdesc;

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
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	
	/**
	 *@generated
	 */
	@Column(name = "isdefault")
	private java.lang.Boolean isdefault;
	
	
	@Column(name = "contactype", length =1)
	private java.lang.String contactype;
	
	@Column(name = "contactype2", length = 1)
	private java.lang.String contactype2;
	
	@Column(name = "contactxt")
	private java.lang.String contactxt;
	
	@Column(name = "billtxt")
	private java.lang.String billtxt;
	
	/**
	 *@generated
	 */
	@Column(name = "iscusales")
	private java.lang.Boolean iscusales;

	/**
	 *@generated
	 */
	@Column(name = "salesid")
	private java.lang.Long salesid;

	public Long getSalesid() {
		return salesid;
	}

	public void setSalesid(Long salesid) {
		this.salesid = salesid;
	}

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
	public java.lang.Long getCustomerid() {
		return this.customerid;
	}

	/**
	 *@generated
	 */
	public void setCustomerid(java.lang.Long value) {
		this.customerid = value;
	}

	

	/**
	 *@generated
	 */
	public java.lang.String getName() {
		return this.name;
	}

	/**
	 *@generated
	 */
	public void setName(java.lang.String value) {
		this.name = value;
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
	public java.lang.String getPhone() {
		return this.phone;
	}

	/**
	 *@generated
	 */
	public void setPhone(java.lang.String value) {
		this.phone = value;
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
	public java.lang.String getEmail() {
		return this.email;
	}

	/**
	 *@generated
	 */
	public void setEmail(java.lang.String value) {
		this.email = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getHabit() {
		return this.habit;
	}

	/**
	 *@generated
	 */
	public void setHabit(java.lang.String value) {
		this.habit = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getDuty() {
		return this.duty;
	}

	/**
	 *@generated
	 */
	public void setDuty(java.lang.String value) {
		this.duty = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getJobdesc() {
		return this.jobdesc;
	}

	/**
	 *@generated
	 */
	public void setJobdesc(java.lang.String value) {
		this.jobdesc = value;
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

	public java.lang.String getCustomerabbr() {
		return customerabbr;
	}

	public void setCustomerabbr(java.lang.String customerabbr) {
		this.customerabbr = customerabbr;
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

	public java.lang.String getContactype() {
		return contactype;
	}

	public void setContactype(java.lang.String contactype) {
		this.contactype = contactype;
	}

	public java.lang.String getContactype2() {
		return contactype2;
	}

	public void setContactype2(java.lang.String contactype2) {
		this.contactype2 = contactype2;
	}

	public java.lang.String getContactxt() {
		return contactxt;
	}

	public void setContactxt(java.lang.String contactxt) {
		this.contactxt = contactxt;
	}

	public java.lang.Boolean getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(java.lang.Boolean isdefault) {
		this.isdefault = isdefault;
	}

	public java.lang.String getFax() {
		return fax;
	}

	public void setFax(java.lang.String fax) {
		this.fax = fax;
	}

	public java.lang.Boolean getIscusales() {
		return iscusales;
	}

	public void setIscusales(java.lang.Boolean iscusales) {
		this.iscusales = iscusales;
	}

	public java.lang.String getBilltxt() {
		return billtxt;
	}

	public void setBilltxt(java.lang.String billtxt) {
		this.billtxt = billtxt;
	}
	
	
}