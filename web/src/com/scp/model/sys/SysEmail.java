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
@Table(name = "sys_email")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysEmail implements java.io.Serializable {

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
	@Column(name = "addressee")
	private java.lang.String addressee;

	/**
	 *@generated
	 */
	@Column(name = "copys")
	private java.lang.String copys;

	/**
	 *@generated
	 */
	@Column(name = "subject")
	private java.lang.String subject;

	/**
	 *@generated
	 */
	@Column(name = "content")
	private java.lang.String content;

	/**
	 *@generated
	 */
	@Column(name = "issent")
	private java.lang.Boolean issent;
	
	@Column(name = "isautosend")
	private java.lang.Boolean isautosend;

	/**
	 *@generated
	 */
	@Column(name = "senttime", length = 29)
	private java.util.Date senttime;

	/**
	 *@generated
	 */
	@Column(name = "msgid")
	private java.lang.String msgid;

	/**
	 *@generated
	 */
	@Column(name = "sender")
	private java.lang.String sender;

	
	@Column(name = "trycount")
	private java.lang.Integer trycount;
	
	/**
	 *@generated
	 */
	@Column(name = "receivetime", length = 29)
	private java.util.Date receivetime;

	/**
	 *@generated
	 */
	@Column(name = "emailtype",  length = 1)
	private java.lang.String emailtype;

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
	@Column(name = "linkid")
	private java.lang.Long linkid;
	
	/**
	 *@generated
	 */
	@Column(name = "linktbl")
	private java.lang.String linktbl;
	
	
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
	public java.lang.String getAddressee() {
		return this.addressee;
	}

	/**
	 *@generated
	 */
	public void setAddressee(java.lang.String value) {
		this.addressee = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCopys() {
		return this.copys;
	}

	/**
	 *@generated
	 */
	public void setCopys(java.lang.String value) {
		this.copys = value;
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
	public java.lang.String getContent() {
		return this.content;
	}

	/**
	 *@generated
	 */
	public void setContent(java.lang.String value) {
		this.content = value;
	}


	public java.lang.Boolean getIssent() {
		return issent;
	}

	public void setIssent(java.lang.Boolean issent) {
		this.issent = issent;
	}

	/**
	 *@generated
	 */
	public java.util.Date getSenttime() {
		return this.senttime;
	}

	/**
	 *@generated
	 */
	public void setSenttime(java.util.Date value) {
		this.senttime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getMsgid() {
		return this.msgid;
	}

	/**
	 *@generated
	 */
	public void setMsgid(java.lang.String value) {
		this.msgid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getSender() {
		return this.sender;
	}

	/**
	 *@generated
	 */
	public void setSender(java.lang.String value) {
		this.sender = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getReceivetime() {
		return this.receivetime;
	}

	/**
	 *@generated
	 */
	public void setReceivetime(java.util.Date value) {
		this.receivetime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getEmailtype() {
		return this.emailtype;
	}

	/**
	 *@generated
	 */
	public void setEmailtype(java.lang.String value) {
		this.emailtype = value;
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

	public java.lang.Boolean getIsautosend() {
		return isautosend;
	}

	public void setIsautosend(java.lang.Boolean isautosend) {
		this.isautosend = isautosend;
	}

	public java.lang.Long getLinkid() {
		return linkid;
	}

	public void setLinkid(java.lang.Long linkid) {
		this.linkid = linkid;
	}

	public java.lang.String getLinktbl() {
		return linktbl;
	}

	public void setLinktbl(java.lang.String linktbl) {
		this.linktbl = linktbl;
	}

	public java.lang.Integer getTrycount() {
		return trycount == null ? 0 : trycount;
	}

	public void setTrycount(java.lang.Integer trycount) {
		this.trycount = trycount;
	}
	
}