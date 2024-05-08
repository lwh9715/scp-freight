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
@Table(name = "sys_report")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysReport implements java.io.Serializable {

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
	@Column(name = "code", length = 20)
	private java.lang.String code;
	
	
	@Column(name = "modcode", length = 100)
	private java.lang.String modcode;

	/**
	 *@generated
	 */
	@Column(name = "namec", length = 20)
	private java.lang.String namec;

	/**
	 *@generated
	 */
	@Column(name = "namee", length = 20)
	private java.lang.String namee;

	/**
	 *@generated
	 */
	@Column(name = "info", length = 20)
	private java.lang.String info;

	/**
	 *@generated
	 */
	@Column(name = "parentid")
	private java.lang.Long parentid;

	/**
	 *@generated
	 */
	@Column(name = "modid")
	private java.lang.Long modid;

	/**
	 *@generated
	 */
	@Column(name = "filename", length = 100)
	private java.lang.String filename;

	/**
	 *@generated
	 */
	@Column(name = "isleaf", nullable = false, length = 1)
	private java.lang.String isleaf;
	
	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	@Column(name = "templete")
	private java.lang.String templete;
	
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	@Column(name = "isreadonly")
	private java.lang.Boolean isreadonly;
	
	@Column(name = "userid")
	private java.lang.Long userid;
	
	@Column(name = "corpid")
	private java.lang.Long corpid;
	
	@Column(name = "filepath")
	private java.lang.String filepath;
	
	@Column(name = "ispublic")
	private java.lang.Boolean ispublic;
	
	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;

	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;

	@Column(name = "updater", length = 100)
	private java.lang.String updater;

	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;
	
	@Column(name = "isinuse")
	private java.lang.Boolean isinuse;
	
	@Column(name = "islock")
	private java.lang.Boolean islock;
	
	@Column(name = "filebyte")
	private byte[] filebyte;
	
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
	public java.lang.String getInfo() {
		return this.info;
	}

	/**
	 *@generated
	 */
	public void setInfo(java.lang.String value) {
		this.info = value;
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
	public java.lang.Long getModid() {
		return this.modid;
	}

	/**
	 *@generated
	 */
	public void setModid(java.lang.Long value) {
		this.modid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getFilename() {
		return this.filename;
	}

	/**
	 *@generated
	 */
	public void setFilename(java.lang.String value) {
		this.filename = value;
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

	public java.lang.String getModcode() {
		return modcode;
	}

	public void setModcode(java.lang.String modcode) {
		this.modcode = modcode;
	}

	public java.lang.String getTemplete() {
		return templete;
	}

	public void setTemplete(java.lang.String templete) {
		this.templete = templete;
	}

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
	}

	public java.lang.Boolean getIsreadonly() {
		return isreadonly;
	}

	public void setIsreadonly(java.lang.Boolean isreadonly) {
		this.isreadonly = isreadonly;
	}

	public java.lang.Long getUserid() {
		return userid;
	}

	public void setUserid(java.lang.Long userid) {
		this.userid = userid;
	}

	public java.lang.String getFilepath() {
		return filepath;
	}

	public void setFilepath(java.lang.String filepath) {
		this.filepath = filepath;
	}

	public java.lang.Boolean getIspublic() {
		return ispublic;
	}

	public void setIspublic(java.lang.Boolean ispublic) {
		this.ispublic = ispublic;
	}

	public java.lang.String getInputer() {
		return inputer;
	}

	public void setInputer(java.lang.String inputer) {
		this.inputer = inputer;
	}

	public java.util.Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(java.util.Date inputtime) {
		this.inputtime = inputtime;
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

	public java.lang.Boolean getIsinuse() {
		return isinuse;
	}

	public void setIsinuse(java.lang.Boolean isinuse) {
		this.isinuse = isinuse;
	}

	public byte[] getFilebyte() {
		return filebyte;
	}

	public void setFilebyte(byte[] filebyte) {
		this.filebyte = filebyte;
	}

	public java.lang.Long getCorpid() {
		return corpid;
	}

	public void setCorpid(java.lang.Long corpid) {
		this.corpid = corpid;
	}

	public java.lang.Boolean getIslock() {
		return islock;
	}

	public void setIslock(java.lang.Boolean islock) {
		this.islock = islock;
	}

	
}