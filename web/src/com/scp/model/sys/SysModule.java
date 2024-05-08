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
@Table(name = "sys_module")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysModule implements java.io.Serializable {

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
	@Column(name = "pid")
	private java.lang.Long pid;

	/**
	 *@generated
	 */
	@Column(name = "modorder")
	private java.lang.Integer modorder;

	/**
	 *@generated
	 */
	@Column(name = "code", length = 100)
	private java.lang.String code;

	/**
	 *@generated
	 */
	@Column(name = "name", length = 100)
	private java.lang.String name;

	/**
	 *@generated
	 */
	@Column(name = "ico", length = 100)
	private java.lang.String ico;

	/**
	 *@generated
	 */
	@Column(name = "url", length = 100)
	private java.lang.String url;

	/**
	 *@generated
	 */
	@Column(name = "isleaf",  length = 1)
	private java.lang.String isleaf;

	/**
	 *@generated
	 */
	@Column(name = "isctrl",  length = 1)
	private java.lang.String isctrl;

	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	/**
	 *@generated
	 */
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "sysModule")
	@IndexColumn(name="id")
	public java.util.List<SysModinrole> sysModinroles = new java.util.ArrayList<SysModinrole>();

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
	public java.lang.Long getPid() {
		return this.pid;
	}

	/**
	 *@generated
	 */
	public void setPid(java.lang.Long value) {
		this.pid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Integer getModorder() {
		return this.modorder;
	}

	/**
	 *@generated
	 */
	public void setModorder(java.lang.Integer value) {
		this.modorder = value;
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
	public java.lang.String getIco() {
		return this.ico;
	}

	/**
	 *@generated
	 */
	public void setIco(java.lang.String value) {
		this.ico = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getUrl() {
		return this.url;
	}

	/**
	 *@generated
	 */
	public void setUrl(java.lang.String value) {
		this.url = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getIsleaf() {
		return this.isleaf;
	}

	/**
	 *@generated
	 */
	public void setIsleaf(java.lang.String value) {
		this.isleaf = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getIsctrl() {
		return this.isctrl;
	}

	/**
	 *@generated
	 */
	public void setIsctrl(java.lang.String value) {
		this.isctrl = value;
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
}