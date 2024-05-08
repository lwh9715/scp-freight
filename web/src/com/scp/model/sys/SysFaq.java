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
@Table(name = "sys_faq")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysFaq implements java.io.Serializable {

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
	@Column(name = "subject", length = 40)
	private java.lang.String subject;

	/**
	 *@generated
	 */
	@Column(name = "classify", length = 20)
	private java.lang.String classify;

	/**
	 *@generated
	 */
	@Column(name = "keywords", length = 20)
	private java.lang.String keywords;

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
	
	/**
	 *@generated
	 */
	@Column(name = "moduleid")
	private java.lang.Long moduleid;

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
	public java.lang.String getClassify() {
		return this.classify;
	}

	/**
	 *@generated
	 */
	public void setClassify(java.lang.String value) {
		this.classify = value;
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

	public java.lang.Long getModuleid() {
		return moduleid;
	}

	public void setModuleid(java.lang.Long moduleid) {
		this.moduleid = moduleid;
	}

}