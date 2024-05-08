package com.scp.model.car;

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
@Table(name = "car_oli_record")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class CarOliRecord implements java.io.Serializable {

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
	@Column(name = "nos", length = 20)
	private java.lang.String nos;

	/**
	 *@generated
	 */
	@Column(name = "singtime", length = 29)
	private java.util.Date singtime;

	/**
	 *@generated
	 */
	@Column(name = "jobid")
	private java.lang.Long jobid;

	/**
	 *@generated
	 */
	@Column(name = "carid")
	private java.lang.Long carid;

	/**
	 *@generated
	 */
	@Column(name = "piece")
	private java.math.BigDecimal piece;

	/**
	 *@generated
	 */
	@Column(name = "price")
	private java.math.BigDecimal price;

	/**
	 *@generated
	 */
	@Column(name = "amount")
	private java.math.BigDecimal amount;

	/**
	 *@generated
	 */
	@Column(name = "currency", length = 3)
	private java.lang.String currency;

	/**
	 *@generated
	 */
	@Column(name = "ischeck")
	private java.lang.Boolean ischeck;

	/**
	 *@generated
	 */
	@Column(name = "checkter", length = 30)
	private java.lang.String checkter;

	/**
	 *@generated
	 */
	@Column(name = "checktime", length = 29)
	private java.util.Date checktime;

	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String remarks;

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
	public java.lang.String getNos() {
		return this.nos;
	}

	/**
	 *@generated
	 */
	public void setNos(java.lang.String value) {
		this.nos = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getSingtime() {
		return this.singtime;
	}

	/**
	 *@generated
	 */
	public void setSingtime(java.util.Date value) {
		this.singtime = value;
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
	public java.lang.Long getCarid() {
		return this.carid;
	}

	/**
	 *@generated
	 */
	public void setCarid(java.lang.Long value) {
		this.carid = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getPiece() {
		return this.piece;
	}

	/**
	 *@generated
	 */
	public void setPiece(java.math.BigDecimal value) {
		this.piece = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getPrice() {
		return this.price;
	}

	/**
	 *@generated
	 */
	public void setPrice(java.math.BigDecimal value) {
		this.price = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getAmount() {
		return this.amount;
	}

	/**
	 *@generated
	 */
	public void setAmount(java.math.BigDecimal value) {
		this.amount = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCurrency() {
		return this.currency;
	}

	/**
	 *@generated
	 */
	public void setCurrency(java.lang.String value) {
		this.currency = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIscheck() {
		return this.ischeck;
	}

	/**
	 *@generated
	 */
	public void setIscheck(java.lang.Boolean value) {
		this.ischeck = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCheckter() {
		return this.checkter;
	}

	/**
	 *@generated
	 */
	public void setCheckter(java.lang.String value) {
		this.checkter = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getChecktime() {
		return this.checktime;
	}

	/**
	 *@generated
	 */
	public void setChecktime(java.util.Date value) {
		this.checktime = value;
	}

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