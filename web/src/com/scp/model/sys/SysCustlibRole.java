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
@Table(name = "sys_custlib_role")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class SysCustlibRole implements java.io.Serializable {

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
	@Column(name = "custlibid")
	private java.lang.Long custlibid;

	/**
	 *@generated
	 */
	@Column(name = "roleid")
	private java.lang.Long roleid;
	
	
	@Column(name = "orgid")
	private java.lang.Long orgid;
	
	@Column(name = "parentid")
	private java.lang.Long parentid;

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
	public java.lang.Long getCustlibid() {
		return this.custlibid;
	}

	/**
	 *@generated
	 */
	public void setCustlibid(java.lang.Long value) {
		this.custlibid = value;
	}

	public java.lang.Long getRoleid() {
		return roleid;
	}

	public void setRoleid(java.lang.Long roleid) {
		this.roleid = roleid;
	}

	public java.lang.Long getOrgid() {
		return orgid;
	}

	public void setOrgid(java.lang.Long orgid) {
		this.orgid = orgid;
	}

	public java.lang.Long getParentid() {
		return parentid;
	}

	public void setParentid(java.lang.Long parentid) {
		this.parentid = parentid;
	}

	
}