package com.scp.model.del;

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
@Table(name = "del_delivery")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class DelDelivery implements java.io.Serializable {

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
	@Column(name = "driver", length = 20)
	private java.lang.String driver;

	/**
	 *@generated
	 */
	@Column(name = "drivertel", length = 20)
	private java.lang.String drivertel;

	/**
	 *@generated
	 */
	@Column(name = "driverno", length = 20)
	private java.lang.String driverno;

	/**
	 *@generated
	 */
	@Column(name = "jobid")
	private java.lang.Long jobid;

	/**
	 *@generated
	 */
	@Column(name = "refid")
	private java.lang.Long refid;

	/**
	 *@generated
	 */
	@Column(name = "trucktype", length = 1)
	private java.lang.String trucktype;

	/**
	 *@generated
	 */
	@Column(name = "carid")
	private java.lang.Long carid;

	/**
	 *@generated
	 */
	@Column(name = "isubmit")
	private java.lang.Boolean isubmit;

	/**
	 *@generated
	 */
	@Column(name = "submitime", length = 29)
	private java.util.Date submitime;

	/**
	 *@generated
	 */
	@Column(name = "submiter", length = 30)
	private java.lang.String submiter;

	/**
	 *@generated
	 */
	@Column(name = "ischeck")
	private java.lang.Boolean ischeck;

	/**
	 *@generated
	 */
	@Column(name = "checktime", length = 29)
	private java.util.Date checktime;

	/**
	 *@generated
	 */
	@Column(name = "checkter", length = 30)
	private java.lang.String checkter;

	/**
	 *@generated
	 */
	@Column(name = "issend")
	private java.lang.Boolean issend;

	/**
	 *@generated
	 */
	@Column(name = "sendtime", length = 29)
	private java.util.Date sendtime;

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
	public java.lang.String getDriver() {
		return this.driver;
	}

	/**
	 *@generated
	 */
	public void setDriver(java.lang.String value) {
		this.driver = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getDrivertel() {
		return this.drivertel;
	}

	/**
	 *@generated
	 */
	public void setDrivertel(java.lang.String value) {
		this.drivertel = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getDriverno() {
		return this.driverno;
	}

	/**
	 *@generated
	 */
	public void setDriverno(java.lang.String value) {
		this.driverno = value;
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
	public java.lang.Long getRefid() {
		return this.refid;
	}

	/**
	 *@generated
	 */
	public void setRefid(java.lang.Long value) {
		this.refid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getTrucktype() {
		return this.trucktype;
	}

	/**
	 *@generated
	 */
	public void setTrucktype(java.lang.String value) {
		this.trucktype = value;
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
	public java.lang.Boolean getIsubmit() {
		return this.isubmit;
	}

	/**
	 *@generated
	 */
	public void setIsubmit(java.lang.Boolean value) {
		this.isubmit = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getSubmitime() {
		return this.submitime;
	}

	/**
	 *@generated
	 */
	public void setSubmitime(java.util.Date value) {
		this.submitime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getSubmiter() {
		return this.submiter;
	}

	/**
	 *@generated
	 */
	public void setSubmiter(java.lang.String value) {
		this.submiter = value;
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
	public java.lang.Boolean getIssend() {
		return this.issend;
	}

	/**
	 *@generated
	 */
	public void setIssend(java.lang.Boolean value) {
		this.issend = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getSendtime() {
		return this.sendtime;
	}

	/**
	 *@generated
	 */
	public void setSendtime(java.util.Date value) {
		this.sendtime = value;
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