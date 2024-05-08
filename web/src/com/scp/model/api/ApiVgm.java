package com.scp.model.api;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "api_vgm")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class ApiVgm implements java.io.Serializable{
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private Long id;
	
	
	@Column(name = "carrierCode", length = 10, nullable = false)
	private String carrierCode;
	
	@Column(name = "vesselName", length = 35)
	private String vesselName;
	
	@Column(name = "voyNo", length = 10)
	private String voyNo;
	
	@Column(name = "portCode", length = 5 , nullable = false)
	private String portCode;
	
	@Column(name = "masterBillNo", length = 30)
	private String masterBillNo;
	
	@Column(name = "customerCode", length = 80)
	private String customerCode;
	
	@Column(name = "pier", length = 60)
	private String pier;
	
	@Column(name = "soNumber", length = 30)
	private String soNumber;
	
	@Column(name = "isSoc")
	private Boolean isSoc;
	
	@Column(name = "isdelete")
	private Boolean isdelete;
	
	@Column(name = "boxcheckstate")
	private Boolean boxcheckstate;
	
	@Column(name = "checkstate")
	private Boolean checkstate;
	
	@Column(name = "inputer")
	private String inputer;
	
	@Column(name = "updater")
	private String updater;
	
	@Column(name = "inputtime")
	private Date inputtime;
	
	@Column(name = "updatetime")
	private Date updatetime;
	
	@Column(name = "jobid")
	private Long jobid;

	@Column(name = "status")
	private String status;

	@Column(name = "message")
	private String message;

	@Column(name = "nos")
	private String nos;


	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public String getVoyNo() {
		return voyNo;
	}

	public void setVoyNo(String voyNo) {
		this.voyNo = voyNo;
	}

	public String getPortCode() {
		return portCode;
	}

	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}

	public String getMasterBillNo() {
		return masterBillNo;
	}

	public void setMasterBillNo(String masterBillNo) {
		this.masterBillNo = masterBillNo;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getPier() {
		return pier;
	}

	public void setPier(String pier) {
		this.pier = pier;
	}

	public String getSoNumber() {
		return soNumber;
	}

	public void setSoNumber(String soNumber) {
		this.soNumber = soNumber;
	}

	public Boolean getIsSoc() {
		return isSoc;
	}

	public void setIsSoc(Boolean isSoc) {
		this.isSoc = isSoc;
	}


	public Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(Boolean isdelete) {
		this.isdelete = isdelete;
	}

	public Boolean getBoxcheckstate() {
		return boxcheckstate;
	}

	public void setBoxcheckstate(Boolean boxcheckstate) {
		this.boxcheckstate = boxcheckstate;
	}

	public Boolean getCheckstate() {
		return checkstate;
	}

	public void setCheckstate(Boolean checkstate) {
		this.checkstate = checkstate;
	}

	public String getInputer() {
		return inputer;
	}

	public void setInputer(String inputer) {
		this.inputer = inputer;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public Long getJobid() {
		return jobid;
	}

	public void setJobid(Long jobid) {
		this.jobid = jobid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNos() {
		return nos;
	}

	public void setNos(String nos) {
		this.nos = nos;
	}

}
