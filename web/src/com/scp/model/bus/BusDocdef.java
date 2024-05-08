package com.scp.model.bus;

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
@Table(name = "bus_docdef")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusDocdef implements java.io.Serializable {

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
	@Column(name = "linkid")
	private java.lang.Long linkid;

	/**
	 *@generated
	 */
	@Column(name = "jobid")
	private java.lang.Long jobid;

	/**
	 *@generated
	 */
	@Column(name = "docexpid")
	private java.lang.Long docexpid;

	/**
	 *@generated
	 */
	@Column(name = "code", length = 1)
	private java.lang.String code;

	/**
	 *@generated
	 */
	@Column(name = "namec", length = 20)
	private java.lang.String namec;

	/**
	 *@generated
	 */
	@Column(name = "namee", length = 40)
	private java.lang.String namee;

	/**
	 *@generated
	 */
	@Column(name = "corpid")
	private java.lang.Long corpid;

	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

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
	@Column(name = "ischoose", length = 29)
	private Boolean ischoose;
	
	@Column(name = "expno")
	private java.lang.String expno;
	

	@Column(name = "sender")
	private java.lang.String sender;
	
	
	@Column(name = "sendate")
	private java.util.Date sendate;
	
	@Column(name = "expcorp")
	private java.lang.String expcorp;
	
	@Column(name = "mark")
	private java.lang.String mark;
	
	@Column(name = "getdate")
	private java.util.Date getdate;
	
	@Column(name = "filename")
	private java.lang.String filename;
	
	
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


	public java.lang.Long getLinkid() {
		return linkid;
	}

	public void setLinkid(java.lang.Long linkid) {
		this.linkid = linkid;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getJobid() {
		return this.jobid;
	}

	/**
	 *@generated
	 */
	public void setJobid(java.lang.Long value) {
		this.jobid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getDocexpid() {
		return this.docexpid;
	}

	/**
	 *@generated
	 */
	public void setDocexpid(java.lang.Long value) {
		this.docexpid = value;
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
	public java.lang.String getNamec() {
		return this.namec;
	}

	/**
	 *@generated
	 */
	public void setNamec(java.lang.String value) {
		this.namec = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getNamee() {
		return this.namee;
	}

	/**
	 *@generated
	 */
	public void setNamee(java.lang.String value) {
		this.namee = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getCorpid() {
		return this.corpid;
	}

	/**
	 *@generated
	 */
	public void setCorpid(java.lang.Long value) {
		this.corpid = value;
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

	public Boolean getIschoose() {
		return ischoose;
	}

	public void setIschoose(Boolean ischoose) {
		this.ischoose = ischoose;
	}

	public java.lang.String getExpno() {
		return expno;
	}

	public void setExpno(java.lang.String expno) {
		this.expno = expno;
	}

	public java.lang.String getSender() {
		return sender;
	}

	public void setSender(java.lang.String sender) {
		this.sender = sender;
	}

	public java.util.Date getSendate() {
		return sendate;
	}

	public void setSendate(java.util.Date sendate) {
		this.sendate = sendate;
	}

	public java.lang.String getExpcorp() {
		return expcorp;
	}

	public void setExpcorp(java.lang.String expcorp) {
		this.expcorp = expcorp;
	}

	public java.lang.String getMark() {
		return mark;
	}

	public void setMark(java.lang.String mark) {
		this.mark = mark;
	}

	public java.util.Date getGetdate() {
		return getdate;
	}

	public void setGetdate(java.util.Date getdate) {
		this.getdate = getdate;
	}

	public java.lang.String getFilename() {
		return filename;
	}

	public void setFilename(java.lang.String filename) {
		this.filename = filename;
	}
	
}