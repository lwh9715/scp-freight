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
@Table(name = "sys_userinrole")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysUserinrole implements java.io.Serializable {

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
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	/**
	 *@generated
	 */
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "roleid")
	@ForeignKey(name = "SYS_ROLE_SYS_USERINROLE")
	public SysRole sysRole;

	/**
	 *@generated
	 */
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "userid")
	@ForeignKey(name = "SYS_USER_SYS_USERINROLE")
	public SysUser sysUser;
	
	
	@Column(name = "orgid")
	private java.lang.Long orgid;
	

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
	public SysRole getSysRole() {
		return this.sysRole;
	}

	/**
	 *@generated
	 */
	public void setSysRole(SysRole value) {
		this.sysRole = value;
	}

	/**
	 *@generated
	 */
	public SysUser getSysUser() {
		return this.sysUser;
	}

	/**
	 *@generated
	 */
	public void setSysUser(SysUser value) {
		this.sysUser = value;
	}

	public java.lang.Long getOrgid() {
		return orgid;
	}

	public void setOrgid(java.lang.Long orgid) {
		this.orgid = orgid;
	}
}