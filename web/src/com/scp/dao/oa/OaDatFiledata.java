package com.scp.dao.oa;

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
@Table(name = "oa_dat_filedata")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class OaDatFiledata implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	/**
	 *@generated
	 */
//	@Column(name = "fkcode")
//	private java.lang.Short fkcode;

	/**
	 *@generated
	 */
	@Column(name = "code", nullable = false, length = 20)
	private java.lang.String code;

	/**
	 *@generated
	 */
	@Column(name = "namec", length = 50)
	private java.lang.String namec;

	/**
	 *@generated
	 */
	@Column(name = "namee", length = 100)
	private java.lang.String namee;

	/**
	 *@generated
	 */
	@Column(name = "orderno", nullable = false)
	private short orderno;

	/**
	 *@generated
	 */
	@Column(name = "issysdata")
	private java.lang.Boolean issysdata;

	/**
	 *@generated
	 */
	@Column(name = "corpid")
	private java.lang.Long corpid;
	
	/**
	 *@generated
	 */
	@Column(name = "picurl")
	private java.lang.String picurl;

	/**
	 *@generated
	 */
	@Column(name = "parentid")
	private java.lang.Long parentid;
	/**
	 *@generated
	 */
	@Column(name = "selfid")
	private java.lang.Long selfid;
	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	/**
	 *@generated
	 */
	@Column(name = "isleaf")
	private java.lang.Boolean isleaf;

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
	@Column(name = "gbcode", length = 20)
	private java.lang.String gbcode;

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
//	public java.lang.Short getFkcode() {
//		return this.fkcode;
//	}
//
//	/**
//	 *@generated
//	 */
//	public void setFkcode(java.lang.Short value) {
//		this.fkcode = value;
//	}

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
	public short getOrderno() {
		return this.orderno;
	}

	/**
	 *@generated
	 */
	public void setOrderno(short value) {
		this.orderno = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIssysdata() {
		return issysdata;
	}

	/**
	 *@generated
	 */
	public void setIssysdata(java.lang.Boolean issysdata) {
		this.issysdata = issysdata;
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

	public java.lang.Boolean getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(java.lang.Boolean isleaf) {
		this.isleaf = isleaf;
	}

	public java.lang.String getPicurl() {
		return picurl;
	}

	public void setPicurl(java.lang.String picurl) {
		this.picurl = picurl;
	}

	public java.lang.Long getParentid() {
		return parentid;
	}

	public void setParentid(java.lang.Long parentid) {
		this.parentid = parentid;
	}

	public java.lang.String getGbcode() {
		return gbcode;
	}

	public void setGbcode(java.lang.String gbcode) {
		this.gbcode = gbcode;
	}

	public java.lang.Long getSelfid() {
		return selfid;
	}

	public void setSelfid(java.lang.Long selfid) {
		this.selfid = selfid;
	}

	
	
	
}