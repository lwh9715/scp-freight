package com.scp.model.finance;

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
@Table(name = "fina_doc")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class FinaDoc implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "corpid")
	private Long corpid;

	public Long getCorpid() {
		return corpid;
	}

	public void setCorpid(Long corpid) {
		this.corpid = corpid;
	}

	/**
	 *@generated
	 */
	@Column(name = "docdate", length = 13)
	private java.util.Date docdate;

	/**
	 *@generated
	 */
	@Column(name = "docnumid")
	private java.lang.Long docnumid;

	/**
	 *@generated
	 */
	@Column(name = "num")
	private java.lang.Integer num;

	/**
	 *@generated
	 */
	@Column(name = "attcount", nullable = false)
	private int attcount;

	/**
	 *@generated
	 */
	@Column(name = "ischeck", nullable = false, length = 1)
	private java.lang.String ischeck;

	/**
	 *@generated
	 */
	@Column(name = "checkdate", length = 13)
	private java.util.Date checkdate;

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
	public java.util.Date getDocdate() {
		return this.docdate;
	}

	/**
	 *@generated
	 */
	public void setDocdate(java.util.Date value) {
		this.docdate = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getDocnumid() {
		return this.docnumid;
	}

	/**
	 *@generated
	 */
	public void setDocnumid(java.lang.Long value) {
		this.docnumid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Integer getNum() {
		return this.num;
	}

	/**
	 *@generated
	 */
	public void setNum(java.lang.Integer value) {
		this.num = value;
	}

	/**
	 *@generated
	 */
	public int getAttcount() {
		return this.attcount;
	}

	/**
	 *@generated
	 */
	public void setAttcount(int value) {
		this.attcount = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getIscheck() {
		return this.ischeck;
	}

	/**
	 *@generated
	 */
	public void setIscheck(java.lang.String value) {
		this.ischeck = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getCheckdate() {
		return this.checkdate;
	}

	/**
	 *@generated
	 */
	public void setCheckdate(java.util.Date value) {
		this.checkdate = value;
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
}