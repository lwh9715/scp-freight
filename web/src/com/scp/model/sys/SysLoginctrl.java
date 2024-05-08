package com.scp.model.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 *@generated
 */
@Table(name = "sys_loginctrl")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysLoginctrl implements java.io.Serializable {

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
	
	@Transient
	private java.lang.String username;
	
	@Transient
	private java.lang.String password;

	/**
	 *@generated
	 */
	@Column(name = "pubip", length = 20)
	private java.lang.String pubip;

	/**
	 *@generated
	 */
	@Column(name = "innerip", length = 20)
	private java.lang.String innerip;

	/**
	 *@generated
	 */
	@Column(name = "mac", length = 50)
	private java.lang.String mac;

	/**
	 *@generated
	 */
	@Column(name = "cpuid", length = 100)
	private java.lang.String cpuid;

	/**
	 *@generated
	 */
	@Column(name = "mainboardid", length = 100)
	private java.lang.String mainboardid;

	/**
	 *@generated
	 */
	@Column(name = "diskid", length = 100)
	private java.lang.String diskid;

	/**
	 *@generated
	 */
	@Column(name = "clitype", length = 20)
	private java.lang.String clitype;

	/**
	 *@generated
	 */
	@Column(name = "ostype", length = 20)
	private java.lang.String ostype;

	/**
	 *@generated
	 */
	@Column(name = "pcname", length = 100)
	private java.lang.String pcname;
	
	@Column(name = "pcloginusr", length = 100)
	private java.lang.String pcloginusr;
	

	/**
	 *@generated
	 */
	@Column(name = "isallow")
	private java.lang.Boolean isallow;


	/**
	 *@generated
	 */
	@Column(name = "applyuser", length = 30)
	private java.lang.String applyuser;

	/**
	 *@generated
	 */
	@Column(name = "applytime", length = 29)
	private java.util.Date applytime;

	/**
	 *@generated
	 */
	@Column(name = "lastloginusr", length = 30)
	private java.lang.String lastloginusr;

	/**
	 *@generated
	 */
	@Column(name = "lastlogintime", length = 29)
	private java.util.Date lastlogintime;

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
	public java.lang.String getPubip() {
		return this.pubip;
	}

	/**
	 *@generated
	 */
	public void setPubip(java.lang.String value) {
		this.pubip = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getInnerip() {
		return this.innerip;
	}

	/**
	 *@generated
	 */
	public void setInnerip(java.lang.String value) {
		this.innerip = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getMac() {
		return this.mac;
	}

	/**
	 *@generated
	 */
	public void setMac(java.lang.String value) {
		this.mac = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCpuid() {
		return this.cpuid;
	}

	/**
	 *@generated
	 */
	public void setCpuid(java.lang.String value) {
		this.cpuid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getMainboardid() {
		return this.mainboardid;
	}

	/**
	 *@generated
	 */
	public void setMainboardid(java.lang.String value) {
		this.mainboardid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getDiskid() {
		return this.diskid;
	}

	/**
	 *@generated
	 */
	public void setDiskid(java.lang.String value) {
		this.diskid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getClitype() {
		return this.clitype;
	}

	/**
	 *@generated
	 */
	public void setClitype(java.lang.String value) {
		this.clitype = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getOstype() {
		return this.ostype;
	}

	/**
	 *@generated
	 */
	public void setOstype(java.lang.String value) {
		this.ostype = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getPcname() {
		return this.pcname;
	}

	/**
	 *@generated
	 */
	public void setPcname(java.lang.String value) {
		this.pcname = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIsallow() {
		return this.isallow;
	}

	/**
	 *@generated
	 */
	public void setIsallow(java.lang.Boolean value) {
		this.isallow = value;
	}


	public java.lang.String getApplyuser() {
		return applyuser;
	}

	public void setApplyuser(java.lang.String applyuser) {
		this.applyuser = applyuser;
	}

	public java.util.Date getApplytime() {
		return applytime;
	}

	public void setApplytime(java.util.Date applytime) {
		this.applytime = applytime;
	}

	public java.lang.String getLastloginusr() {
		return lastloginusr;
	}

	public void setLastloginusr(java.lang.String lastloginusr) {
		this.lastloginusr = lastloginusr;
	}


	public java.util.Date getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(java.util.Date lastlogintime) {
		this.lastlogintime = lastlogintime;
	}

	public java.lang.String getUsername() {
		return username;
	}

	public void setUsername(java.lang.String username) {
		this.username = username;
	}

	public java.lang.String getPassword() {
		return password;
	}

	public void setPassword(java.lang.String password) {
		this.password = password;
	}

	public java.lang.String getPcloginusr() {
		return pcloginusr;
	}

	public void setPcloginusr(java.lang.String pcloginusr) {
		this.pcloginusr = pcloginusr;
	}
	
}