package com.scp.model.sys;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *@generated
 */
@Table(name = "sys_modinrole")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysModinrole implements java.io.Serializable {

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
	@Column(name = "rightnew",  length = 1)
	private java.lang.String rightnew;

	/**
	 *@generated
	 */
	@Column(name = "rightedit",  length = 1)
	private java.lang.String rightedit;

	/**
	 *@generated
	 */
	@Column(name = "rightdel",  length = 1)
	private java.lang.String rightdel;

	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	/**
	 *@generated
	 */
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "moduleid")
	@ForeignKey(name = "SYS_MODULE_SYS_MODINROLE")
	public SysModule sysModule;

	/**
	 *@generated
	 */
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "roleid")
	@ForeignKey(name = "SYS_ROLE_SYS_MODINROLE")
	public SysRole sysRole;

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
	public java.lang.String getRightnew() {
		return this.rightnew;
	}

	/**
	 *@generated
	 */
	public void setRightnew(java.lang.String value) {
		this.rightnew = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getRightedit() {
		return this.rightedit;
	}

	/**
	 *@generated
	 */
	public void setRightedit(java.lang.String value) {
		this.rightedit = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getRightdel() {
		return this.rightdel;
	}

	/**
	 *@generated
	 */
	public void setRightdel(java.lang.String value) {
		this.rightdel = value;
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
	public SysModule getSysModule() {
		return this.sysModule;
	}

	/**
	 *@generated
	 */
	public void setSysModule(SysModule value) {
		this.sysModule = value;
	}

	/**
	 *@generated
	 */
	public SysRole getSysRole() {
		return this.sysRole;
	}

	/**
	 *@generated
	 */
	public void setSysRole(SysRole value) {
		this.sysRole = value;
	}
}