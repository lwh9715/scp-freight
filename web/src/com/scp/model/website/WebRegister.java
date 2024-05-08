package com.scp.model.website;

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
@Table(name = "web_register")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class WebRegister implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;
	
	@Column(name = "corpname", length = 100)
	private java.lang.String corpname;
	
	@Column(name = "username", length = 100)
	private java.lang.String username;
	
	@Column(name = "contacts")
	private java.lang.String contacts;
	
	@Column(name = "email", length = 100)
	private java.lang.String email;
	
	@Column(name = "passwords", length = 100)
	private java.lang.String passwords;
	
	@Column(name = "tel", length = 100)
	private java.lang.String tel;
	
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	@Column(name = "inputtime", length = 35)
	private java.util.Date inputtime;
	
	@Column(name = "ischeck")
	private java.lang.Boolean ischeck;
	
	@Column(name = "checker", length = 100)
	private java.lang.String checker;
	
	@Column(name = "checktime", length = 35)
	private java.util.Date checktime;
	
	@Column(name = "customerid")
	private java.lang.Long customerid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getCorpname() {
		return corpname;
	}

	public void setCorpname(java.lang.String corpname) {
		this.corpname = corpname;
	}

	public java.lang.String getUsername() {
		return username;
	}

	public void setUsername(java.lang.String username) {
		this.username = username;
	}

	public java.lang.String getContacts() {
		return contacts;
	}

	public void setContacts(java.lang.String contacts) {
		this.contacts = contacts;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.lang.String getPasswords() {
		return passwords;
	}

	public void setPasswords(java.lang.String passwords) {
		this.passwords = passwords;
	}

	public java.lang.String getTel() {
		return tel;
	}

	public void setTel(java.lang.String tel) {
		this.tel = tel;
	}

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
	}

	public java.util.Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(java.util.Date inputtime) {
		this.inputtime = inputtime;
	}

	public java.lang.Boolean getIscheck() {
		return ischeck;
	}

	public void setIscheck(java.lang.Boolean ischeck) {
		this.ischeck = ischeck;
	}

	public java.lang.String getChecker() {
		return checker;
	}

	public void setChecker(java.lang.String checker) {
		this.checker = checker;
	}

	public java.util.Date getChecktime() {
		return checktime;
	}

	public void setChecktime(java.util.Date checktime) {
		this.checktime = checktime;
	}

	public java.lang.Long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(java.lang.Long customerid) {
		this.customerid = customerid;
	}
	

}