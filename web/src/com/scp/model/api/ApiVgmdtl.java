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

@Table(name = "api_vgmdtl")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class ApiVgmdtl implements java.io.Serializable{
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	
	@Column(name = "vgmid")
	private long vgmid;
	
	@Column(name = "containerNo", length = 20)
	private String containerNo;
	
	@Column(name = "vgmWeight", length = 10)
	private java.math.BigDecimal vgmWeight;
	
	@Column(name = "weighingParty", length = 70)
	private String weighingParty;
	
	@Column(name = "signature", length = 20)
	private String signature;
	
	@Column(name = "weighingMode", length = 10)
	private String weighingMode;
	
	@Column(name = "weighingAddress", length = 140)
	private String weighingAddress;
	
	@Column(name = "weighingTime")
	private Date weighingTime;
	
	@Column(name = "inputer")
	private String inputer;
	
	@Column(name = "updater")
	private String updater;
	
	@Column(name = "inputtime")
	private Date inputtime;
	
	@Column(name = "updatetime")
	private Date updatetime;
	
	@Column(name = "isdelete")
	private Boolean isdelete;
	
	@Column(name = "status")
	private String status;

	@Column(name = "message")
	private String message;

	@Column(name = "sono")
	private String sono;
	
	@Column(name = "issubmit")
	private Boolean issubmit;

	public Boolean getIssubmit() {
		return issubmit;
	}

	public void setIssubmit(Boolean issubmit) {
		this.issubmit = issubmit;
	}

	@Column(name = "resptime")
	private Date  resptime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}



	public java.math.BigDecimal getVgmWeight() {
		return vgmWeight;
	}

	public void setVgmWeight(java.math.BigDecimal vgmWeight) {
		this.vgmWeight = vgmWeight;
	}

	public String getWeighingParty() {
		return weighingParty;
	}

	public void setWeighingParty(String weighingParty) {
		this.weighingParty = weighingParty;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getWeighingMode() {
		return weighingMode;
	}

	public void setWeighingMode(String weighingMode) {
		this.weighingMode = weighingMode;
	}

	public String getWeighingAddress() {
		return weighingAddress;
	}

	public void setWeighingAddress(String weighingAddress) {
		this.weighingAddress = weighingAddress;
	}

	public Date getWeighingTime() {
		return weighingTime;
	}

	public void setWeighingTime(Date weighingTime) {
		this.weighingTime = weighingTime;
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

	public Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(Boolean isdelete) {
		this.isdelete = isdelete;
	}

	public long getVgmid() {
		return vgmid;
	}

	public void setVgmid(long vgmid) {
		this.vgmid = vgmid;
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

	public String getSono() {
		return sono;
	}

	public void setSono(String sono) {
		this.sono = sono;
	}

	public Date getResptime() {
		return resptime;
	}

	public void setResptime(Date resptime) {
		this.resptime = resptime;
	}
}
