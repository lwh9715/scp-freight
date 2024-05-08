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
@Table(name = "fina_prepaid")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class FinaPrepaid implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "nos", length = 30)
	private java.lang.String nos;
	
	@Column(name = "singtime")
	private java.util.Date singtime;
	
	@Column(name = "rpdate")
	private java.util.Date rpdate;
	
	@Column(name = "clientid")
	private Long clientid;
	
	@Column(name = "billtype", length = 1)
	private java.lang.String billtype;
	
	@Column(name = "corpid")
	private Long corpid;
	
	@Column(name = "corpid_account")
	private Long corpid_account;
	
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;

	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;

	@Column(name = "updater", length = 100)
	private java.lang.String updater;

	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getNos() {
		return nos;
	}

	public void setNos(java.lang.String nos) {
		this.nos = nos;
	}

	public java.util.Date getSingtime() {
		return singtime;
	}

	public void setSingtime(java.util.Date singtime) {
		this.singtime = singtime;
	}

	public java.util.Date getRpdate() {
		return rpdate;
	}

	public void setRpdate(java.util.Date rpdate) {
		this.rpdate = rpdate;
	}

	public Long getClientid() {
		return clientid;
	}

	public void setClientid(Long clientid) {
		this.clientid = clientid;
	}

	public java.lang.String getBilltype() {
		return billtype;
	}

	public void setBilltype(java.lang.String billtype) {
		this.billtype = billtype;
	}

	public Long getCorpid() {
		return corpid;
	}

	public void setCorpid(Long corpid) {
		this.corpid = corpid;
	}

	public Long getCorpid_account() {
		return corpid_account;
	}

	public void setCorpid_account(Long corpidAccount) {
		corpid_account = corpidAccount;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
	}

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
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

	
	
}