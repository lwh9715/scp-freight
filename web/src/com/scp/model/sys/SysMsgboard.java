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
@Table(name = "sys_msgboard")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysMsgboard implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	
	@Column(name="linkid")
	private Long linkid;

	/**
	 *@generated
	 */
	@Column(name = "subject", length = 40)
	private java.lang.String subject;

	/**
	 *@generated
	 */
	@Column(name = "msgtype", length = 20)
	private java.lang.String msgtype;

	/**
	 *@generated
	 */
	@Column(name = "keywords", length = 20)
	private java.lang.String keywords;

	/**
	 *@generated
	 */
	@Column(name = "msgcontent")
	private java.lang.String msgcontent;

	/**
	 *@generated
	 */
	@Column(name = "parentid")
	private java.lang.Long parentid;

	/**
	 *@generated
	 */
	@Column(name = "satisfying")
	private java.lang.Short satisfying;

	/**
	 *@generated
	 */
	@Column(name = "state", length = 1)
	private java.lang.String state;

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
	public java.lang.String getSubject() {
		return this.subject;
	}

	/**
	 *@generated
	 */
	public void setSubject(java.lang.String value) {
		this.subject = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getMsgtype() {
		return this.msgtype;
	}

	/**
	 *@generated
	 */
	public void setMsgtype(java.lang.String value) {
		this.msgtype = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getKeywords() {
		return this.keywords;
	}

	/**
	 *@generated
	 */
	public void setKeywords(java.lang.String value) {
		this.keywords = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getMsgcontent() {
		return this.msgcontent;
	}

	/**
	 *@generated
	 */
	public void setMsgcontent(java.lang.String value) {
		this.msgcontent = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getParentid() {
		return this.parentid;
	}

	/**
	 *@generated
	 */
	public void setParentid(java.lang.Long value) {
		this.parentid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getSatisfying() {
		return this.satisfying;
	}

	/**
	 *@generated
	 */
	public void setSatisfying(java.lang.Short value) {
		this.satisfying = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getState() {
		return this.state;
	}

	/**
	 *@generated
	 */
	public void setState(java.lang.String value) {
		this.state = value;
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

	public Long getLinkid() {
		return linkid;
	}

	public void setLinkid(Long linkid) {
		this.linkid = linkid;
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
}