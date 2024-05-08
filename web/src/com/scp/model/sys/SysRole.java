package com.scp.model.sys;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.Parameter;

/**
 *@generated
 */
@Table(name = "sys_role")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysRole implements java.io.Serializable {

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
	@Column(name = "code",  length = 20)
	private java.lang.String code;

	/**
	 *@generated
	 */
	@Column(name = "name",  length = 50)
	private java.lang.String name;

	/**
	 *@generated
	 */
	@Column(name = "roletype",  length = 1)
	private java.lang.String roletype;

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
	
	@Column(name = "linkid")
	private java.lang.Long linkid;

	/**
	 *@generated
	 */
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "sysRole")
	@IndexColumn(name="id")
	public java.util.List<SysUserinrole> sysUserinroles = new java.util.ArrayList<SysUserinrole>();

	/**
	 *@generated
	 */
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "sysRole")
	@IndexColumn(name="id")
	public java.util.List<SysModinrole> sysModinroles = new java.util.ArrayList<SysModinrole>();
	
	/**
	 *@generated
	 */
	@Column(name = "groupname",  length = 30)
	private java.lang.String groupname;
	
	/**
	 *@generated
	 */
	@Column(name = "isystem")
	private java.lang.Boolean isystem;

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
	public java.lang.String getRoletype() {
		return this.roletype;
	}

	/**
	 *@generated
	 */
	public void setRoletype(java.lang.String value) {
		this.roletype = value;
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
	public java.util.List<SysUserinrole> getSysUserinroles() {
		return this.sysUserinroles;
	}

	/**
	 *@generated
	 */
	public void setSysUserinroles(
			java.util.List<SysUserinrole> value) {
		this.sysUserinroles = value;
	}

	/**
	 *@generated
	 */
	public java.util.List<SysModinrole> getSysModinroles() {
		return this.sysModinroles;
	}

	/**
	 *@generated
	 */
	public void setSysModinroles(
			java.util.List<SysModinrole> value) {
		this.sysModinroles = value;
	}

	public java.lang.Long getLinkid() {
		return linkid;
	}

	public void setLinkid(java.lang.Long linkid) {
		this.linkid = linkid;
	}

	public java.lang.String getGroupname() {
		return groupname;
	}

	public void setGroupname(java.lang.String groupname) {
		this.groupname = groupname;
	}

	public java.lang.Boolean getIsystem() {
		return isystem;
	}

	public void setIsystem(java.lang.Boolean isystem) {
		this.isystem = isystem;
	}
	
}