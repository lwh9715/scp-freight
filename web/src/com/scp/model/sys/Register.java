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
@Table(name = "cs_register")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class Register implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	/**
	 *@generated
	 */
	@Column(name = "name")
	private java.lang.String name;

	/**
	 * 
	 * 
	 * @generated
	 */
	@Column(name = "tel", length = 100)
	private java.lang.String tel;
	/**
	 *@generated
	 */
	@Column(name = "email")
	private java.lang.String email;

	/**
	 *@generated
	 */
	@Column(name = "qq")
	private java.lang.String qq;

	/**
	 *@generated
	 */
	@Column(name = "mobile")
	private java.lang.String mobile;


	/**
	 *@generated
	 */
	@Column(name = "compname")
	private java.lang.String compname;

	/**
	 *@generated
	 */
	@Column(name = "address")
	private java.lang.String address;
	
	@Column(name = "ischeck")
	private java.lang.Boolean ischeck;
	
	
	@Column(name = "checktime", length = 29)
	private java.util.Date checktime;
	
	@Column(name = "checkter", length = 100)
	private java.lang.String checkter;
	
	
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

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getTel() {
		return tel;
	}

	public void setTel(java.lang.String tel) {
		this.tel = tel;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.lang.String getQq() {
		return qq;
	}

	public void setQq(java.lang.String qq) {
		this.qq = qq;
	}

	public java.lang.String getMobile() {
		return mobile;
	}

	public void setMobile(java.lang.String mobile) {
		this.mobile = mobile;
	}

	public java.lang.String getCompname() {
		return compname;
	}

	public void setCompname(java.lang.String compname) {
		this.compname = compname;
	}

	public java.lang.String getAddress() {
		return address;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	public java.lang.Boolean getIscheck() {
		return ischeck;
	}

	public void setIscheck(java.lang.Boolean ischeck) {
		this.ischeck = ischeck;
	}

	public java.util.Date getChecktime() {
		return checktime;
	}

	public void setChecktime(java.util.Date checktime) {
		this.checktime = checktime;
	}

	public java.lang.String getCheckter() {
		return checkter;
	}

	public void setCheckter(java.lang.String checkter) {
		this.checkter = checkter;
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
