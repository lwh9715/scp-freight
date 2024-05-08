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
@Table(name = "sys_message") 
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysMessage implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;

	@Column(name = "msgurl")
	private java.lang.String msgurl;
	
	
	@Column(name = "msgtype", length = 1)
	private java.lang.String msgtype;
	
	@Column(name = "issystem")
	private java.lang.Boolean issystem;
	
	
	/**
	 *@generated
	 */
	@Column(name = "title", length = 200)
	private java.lang.String title;

	/**
	 *@generated
	 */
	@Column(name = "content")
	private java.lang.String content;

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
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	@Column(name = "modulecode")
	private java.lang.String modulecode;
	
	@Column(name = "updater", length = 100)
	private java.lang.String updater;

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
	public java.lang.String getTitle() {
		return this.title;
	}

	/**
	 *@generated
	 */
	public void setTitle(java.lang.String value) {
		this.title = value;
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
	public java.lang.Boolean getIsdelete() {
		return this.isdelete;
	}

	/**
	 *@generated
	 */
	public void setIsdelete(java.lang.Boolean value) {
		this.isdelete = value;
	}

	public java.lang.String getMsgurl() {
		return msgurl;
	}

	public void setMsgurl(java.lang.String msgurl) {
		this.msgurl = msgurl;
	}

	public java.lang.String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(java.lang.String msgtype) {
		this.msgtype = msgtype;
	}

	public java.lang.Boolean getIssystem() {
		return issystem;
	}

	public void setIssystem(java.lang.Boolean issystem) {
		this.issystem = issystem;
	}

	public java.lang.String getModulecode() {
		return modulecode;
	}

	public void setModulecode(java.lang.String modulecode) {
		this.modulecode = modulecode;
	}

	public java.lang.String getUpdater() {
		return updater;
	}

	public void setUpdater(java.lang.String updater) {
		this.updater = updater;
	}

	public java.util.Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(java.util.Date updatetime) {
		this.updatetime = updatetime;
	}
	
}