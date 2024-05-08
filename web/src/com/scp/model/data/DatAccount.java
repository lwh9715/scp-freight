package com.scp.model.data;

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
@Table(name = "dat_account")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class DatAccount implements java.io.Serializable {

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
	@Column(name = "code", nullable = false, length = 20)
	private java.lang.String code;

	/**
	 *@generated
	 */
	@Column(name = "accountno", nullable = false, length = 30)
	private java.lang.String accountno;

	/**
	 *@generated
	 */
	@Column(name = "accountnoe", length = 30)
	private java.lang.String accountnoe;
	
	/**
	 *@generated
	 */
	@Column(name = "bankid")
	private Long bankid;
	
	@Column(name = "corpid")
	private Long corpid;
	
	/**
	 *@generated
	 */
	@Column(name = "accountcont", nullable = false, length = 254)
	private java.lang.String accountcont;
	
	
	@Column(name = "accountconte")
	private java.lang.String accountconte;
	

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

	@Column(name = "isinuse")
	private java.lang.Boolean isinuse;
	
	@Column(name = "orderno")
	private java.lang.Integer orderno;
	
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
	public java.lang.String getAccountno() {
		return this.accountno;
	}

	/**
	 *@generated
	 */
	public void setAccountno(java.lang.String value) {
		this.accountno = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getAccountcont() {
		return this.accountcont;
	}

	/**
	 *@generated
	 */
	public void setAccountcont(java.lang.String value) {
		this.accountcont = value;
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

	
	public java.lang.String getAccountnoe() {
		return accountnoe;
	}

	public void setAccountnoe(java.lang.String accountnoe) {
		this.accountnoe = accountnoe;
	}

	public Long getBankid() {
		return bankid;
	}

	public void setBankid(Long bankid) {
		this.bankid = bankid;
	}

	public java.lang.Boolean getIsinuse() {
		return isinuse;
	}

	public void setIsinuse(java.lang.Boolean isinuse) {
		this.isinuse = isinuse;
	}

	public java.lang.Integer getOrderno() {
		return orderno;
	}

	public void setOrderno(java.lang.Integer orderno) {
		this.orderno = orderno;
	}

	public Long getCorpid() {
		return corpid;
	}

	public void setCorpid(Long corpid) {
		this.corpid = corpid;
	}

	public java.lang.String getAccountconte() {
		return accountconte;
	}

	public void setAccountconte(java.lang.String accountconte) {
		this.accountconte = accountconte;
	}
	
}