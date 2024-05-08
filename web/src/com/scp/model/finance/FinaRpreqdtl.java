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
@Table(name = "fina_rpreqdtl")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class FinaRpreqdtl implements java.io.Serializable {

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
	@Column(name = "rpreqid")
	private java.lang.Long rpreqid;

	/**
	 *@generated
	 */
	@Column(name = "arapid")
	private java.lang.Long arapid;

	/**
	 *@generated
	 */
	@Column(name = "amtreq")
	private java.math.BigDecimal amtreq;

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
	public java.lang.Long getRpreqid() {
		return this.rpreqid;
	}

	/**
	 *@generated
	 */
	public void setRpreqid(java.lang.Long value) {
		this.rpreqid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getArapid() {
		return this.arapid;
	}

	/**
	 *@generated
	 */
	public void setArapid(java.lang.Long value) {
		this.arapid = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getAmtreq() {
		return this.amtreq;
	}

	/**
	 *@generated
	 */
	public void setAmtreq(java.math.BigDecimal value) {
		this.amtreq = value;
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
}