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
@Table(name = "sys_useronline")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysUseronline implements java.io.Serializable {

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
	@Column(name = "sessionid")
	private java.lang.String sessionid;

	/**
	 *@generated
	 */
	@Column(name = "logintime", length = 29)
	private java.util.Date logintime;

	/**
	 *@generated
	 */
	@Column(name = "ipaddr", length = 100)
	private java.lang.String ipaddr;
	
	/**
	 *@generated
	 */
	@Column(name = "isvalid")
	private java.lang.Boolean isvalid;
	
	/**
	 *@generated
	 */
	@Column(name = "logouttime", length = 29)
	private java.util.Date logouttime;

	/**
	 *@generated
	 */
	@Column(name = "ip", length = 16)
	private java.lang.String ip;

	/**
	 *@generated
	 */
	@Column(name = "isonline", nullable = false, length = 1)
	private java.lang.String isonline;

	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	@Column(name = "loginuuid", length = 50)
	private java.lang.String loginuuid;
	
	@Column(name = "openid", length = 50)
	private java.lang.String openid;

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
	public java.lang.String getSessionid() {
		return this.sessionid;
	}

	/**
	 *@generated
	 */
	public void setSessionid(java.lang.String value) {
		this.sessionid = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getLogintime() {
		return this.logintime;
	}

	/**
	 *@generated
	 */
	public void setLogintime(java.util.Date value) {
		this.logintime = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getLogouttime() {
		return this.logouttime;
	}

	/**
	 *@generated
	 */
	public void setLogouttime(java.util.Date value) {
		this.logouttime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getIp() {
		return this.ip;
	}

	/**
	 *@generated
	 */
	public void setIp(java.lang.String value) {
		this.ip = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getIsonline() {
		return this.isonline;
	}

	/**
	 *@generated
	 */
	public void setIsonline(java.lang.String value) {
		this.isonline = value;
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

	public java.lang.String getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(java.lang.String ipaddr) {
		this.ipaddr = ipaddr;
	}

	public java.lang.Boolean getIsvalid() {
		return isvalid;
	}

	public void setIsvalid(java.lang.Boolean isvalid) {
		this.isvalid = isvalid;
	}

	public java.lang.String getLoginuuid() {
		return loginuuid;
	}

	public void setLoginuuid(java.lang.String loginuuid) {
		this.loginuuid = loginuuid;
	}

	public java.lang.String getOpenid() {
		return openid;
	}

	public void setOpenid(java.lang.String openid) {
		this.openid = openid;
	}
	
}