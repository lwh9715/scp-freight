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
@Table(name = "fina_statement")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class FinaStatement implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	
	@Column(name = "reportid")
	private Long reportid;
	

	/**
	 *@generated
	 */
	@Column(name = "nos", length = 30)
	private java.lang.String nos;

	/**
	 *@generated
	 */
	@Column(name = "singtime", length = 29)
	private java.util.Date singtime;

	/**
	 *@generated
	 */
	@Column(name = "customerid")
	private java.lang.Long customerid;

	/**
	 *@generated
	 */
	@Column(name = "customerabbr", length = 50)
	private java.lang.String customerabbr;

	/**
	 *@generated
	 */
	@Column(name = "customerdesc")
	private java.lang.String customerdesc;

	/**
	 *@generated
	 */
	@Column(name = "accountid")
	private java.lang.Long accountid;

	/**
	 *@generated
	 */
	@Column(name = "accountcont")
	private java.lang.String accountcont;

	/**
	 *@generated
	 */
	@Column(name = "currency", length = 3)
	private java.lang.String currency;

	/**
	 *@generated
	 */
	@Column(name = "notesdesc")
	private java.lang.String notesdesc;

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
	@Column(name = "actpayrecid")
	private java.lang.Long actpayrecid;

	/**
	 *@generated
	 */
	@Column(name = "isprint")
	private java.lang.Boolean isprint;

	/**
	 *@generated
	 */
	@Column(name = "printer", length = 30)
	private java.lang.String printer;

	/**
	 *@generated
	 */
	@Column(name = "printime", length = 29)
	private java.util.Date printime;

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
	public java.lang.Long getCustomerid() {
		return this.customerid;
	}

	/**
	 *@generated
	 */
	public void setCustomerid(java.lang.Long value) {
		this.customerid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCustomerabbr() {
		return this.customerabbr;
	}

	/**
	 *@generated
	 */
	public void setCustomerabbr(java.lang.String value) {
		this.customerabbr = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCustomerdesc() {
		return this.customerdesc;
	}

	/**
	 *@generated
	 */
	public void setCustomerdesc(java.lang.String value) {
		this.customerdesc = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getAccountid() {
		return this.accountid;
	}

	/**
	 *@generated
	 */
	public void setAccountid(java.lang.Long value) {
		this.accountid = value;
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
	public java.lang.String getNotesdesc() {
		return this.notesdesc;
	}

	/**
	 *@generated
	 */
	public void setNotesdesc(java.lang.String value) {
		this.notesdesc = value;
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
	public java.lang.Long getActpayrecid() {
		return this.actpayrecid;
	}

	/**
	 *@generated
	 */
	public void setActpayrecid(java.lang.Long value) {
		this.actpayrecid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIsprint() {
		return this.isprint;
	}

	/**
	 *@generated
	 */
	public void setIsprint(java.lang.Boolean value) {
		this.isprint = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getPrinter() {
		return this.printer;
	}

	/**
	 *@generated
	 */
	public void setPrinter(java.lang.String value) {
		this.printer = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getPrintime() {
		return this.printime;
	}

	/**
	 *@generated
	 */
	public void setPrintime(java.util.Date value) {
		this.printime = value;
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

	public Long getReportid() {
		return reportid;
	}

	public void setReportid(Long reportid) {
		this.reportid = reportid;
	}
	
	
}