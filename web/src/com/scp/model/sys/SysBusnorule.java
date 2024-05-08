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
@Table(name = "sys_busnorule")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class SysBusnorule implements java.io.Serializable {

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
	@Column(name = "busnotypeid")
	private java.lang.Long busnotypeid;
	
	@Column(name = "isyear")
	private java.lang.Boolean isyear;
	
	@Column(name = "ismonth")
	private java.lang.Boolean ismonth;
	
	

	/**
	 *@generated
	 */
	@Column(name = "datefm", nullable = false, length = 13)
	private java.util.Date datefm;

	/**
	 *@generated
	 */
	@Column(name = "dateto", nullable = false, length = 13)
	private java.util.Date dateto;

	/**
	 *@generated
	 */
	@Column(name = "prefix", nullable = false, length = 10)
	private java.lang.String prefix;

	/**
	 *@generated
	 */
	@Column(name = "seriallen", nullable = false)
	private short seriallen;

	/**
	 *@generated
	 */
	@Column(name = "yearlen", nullable = false)
	private short yearlen;

	/**
	 *@generated
	 */
	@Column(name = "resettype", nullable = false, length = 1)
	private java.lang.String resettype;

	/**
	 *@generated
	 */
	@Column(name = "serialno")
	private java.lang.Integer serialno;

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
	
	
	@Column(name = "isday")
	private java.lang.Boolean isday;
	

	/**
	 *@generated
	 */
	@Column(name = "remark")
	private java.lang.String remark;

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
	
	
	@Column(name = "impexp")
	private java.lang.String impexp;

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
	public java.lang.Long getBusnotypeid() {
		return this.busnotypeid;
	}

	/**
	 *@generated
	 */
	public void setBusnotypeid(java.lang.Long value) {
		this.busnotypeid = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getDatefm() {
		return this.datefm;
	}

	/**
	 *@generated
	 */
	public void setDatefm(java.util.Date value) {
		this.datefm = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getDateto() {
		return this.dateto;
	}

	/**
	 *@generated
	 */
	public void setDateto(java.util.Date value) {
		this.dateto = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getPrefix() {
		return this.prefix;
	}

	/**
	 *@generated
	 */
	public void setPrefix(java.lang.String value) {
		this.prefix = value;
	}

	/**
	 *@generated
	 */
	public short getSeriallen() {
		return this.seriallen;
	}

	/**
	 *@generated
	 */
	public void setSeriallen(short value) {
		this.seriallen = value;
	}

	/**
	 *@generated
	 */
	public short getYearlen() {
		return this.yearlen;
	}

	/**
	 *@generated
	 */
	public void setYearlen(short value) {
		this.yearlen = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getResettype() {
		return this.resettype;
	}

	/**
	 *@generated
	 */
	public void setResettype(java.lang.String value) {
		this.resettype = value;
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
	public java.lang.String getRemark() {
		return this.remark;
	}

	/**
	 *@generated
	 */
	public void setRemark(java.lang.String value) {
		this.remark = value;
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

	public java.lang.Boolean getIsyear() {
		return isyear;
	}

	public void setIsyear(java.lang.Boolean isyear) {
		this.isyear = isyear;
	}

	public java.lang.Boolean getIsmonth() {
		return ismonth;
	}

	public void setIsmonth(java.lang.Boolean ismonth) {
		this.ismonth = ismonth;
	}

	public java.lang.Integer getSerialno() {
		return serialno;
	}

	public void setSerialno(java.lang.Integer serialno) {
		this.serialno = serialno;
	}

	public java.lang.Boolean getIsday() {
		return isday;
	}

	public void setIsday(java.lang.Boolean isday) {
		this.isday = isday;
	}

	public java.lang.String getImpexp() {
		return impexp;
	}

	public void setImpexp(java.lang.String impexp) {
		this.impexp = impexp;
	}
	
	
}