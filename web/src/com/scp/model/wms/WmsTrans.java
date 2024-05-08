package com.scp.model.wms;

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
@Table(name = "wms_trans")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class WmsTrans implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "nos", length = 30)
	private java.lang.String nos;
	
	@Column(name = "singtime", length = 29)
	private java.util.Date singtime;
	
	@Column(name = "warehouseid_out")
	private java.lang.Long warehouseid_out;
	
	@Column(name = "warehouseid_in")
	private java.lang.Long warehouseid_in;
	
	@Column(name = "isubmit")
	private java.lang.Boolean isubmit;

	@Column(name = "submitime", length = 29)
	private java.util.Date submitime;

	@Column(name = "submiter", length = 30)
	private java.lang.String submiter;

	@Column(name = "ischeck")
	private java.lang.Boolean ischeck;

	@Column(name = "checktime", length = 29)
	private java.util.Date checktime;

	@Column(name = "checkter", length = 30)
	private java.lang.String checkter;
	
	@Column(name = "wmsoutid")
	private java.lang.Long wmsoutid;
	
	@Column(name = "wmsinid")
	private java.lang.Long wmsinid;
	
	@Column(name = "wmsoutnos", length = 30)
	private java.lang.String wmsoutnos;
	
	@Column(name = "wmsinnos", length = 30)
	private java.lang.String wmsinnos;
	
	@Column(name = "remark")
	private java.lang.String remark;
	
	@Column(name = "corpid")
	private java.lang.Long corpid;
	
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

	public java.lang.Long getWarehouseid_out() {
		return warehouseid_out;
	}

	public void setWarehouseid_out(java.lang.Long warehouseidOut) {
		warehouseid_out = warehouseidOut;
	}

	public java.lang.Long getWarehouseid_in() {
		return warehouseid_in;
	}

	public void setWarehouseid_in(java.lang.Long warehouseidIn) {
		warehouseid_in = warehouseidIn;
	}

	public java.lang.Boolean getIsubmit() {
		return isubmit;
	}

	public void setIsubmit(java.lang.Boolean isubmit) {
		this.isubmit = isubmit;
	}

	public java.util.Date getSubmitime() {
		return submitime;
	}

	public void setSubmitime(java.util.Date submitime) {
		this.submitime = submitime;
	}

	public java.lang.String getSubmiter() {
		return submiter;
	}

	public void setSubmiter(java.lang.String submiter) {
		this.submiter = submiter;
	}

	public java.lang.Boolean getIscheck() {
		return ischeck;
	}

	public void setIscheck(java.lang.Boolean ischeck) {
		this.ischeck = ischeck;
	}

	public java.util.Date getChecktime() {
		return checktime;
	}

	public void setChecktime(java.util.Date checktime) {
		this.checktime = checktime;
	}

	public java.lang.String getCheckter() {
		return checkter;
	}

	public void setCheckter(java.lang.String checkter) {
		this.checkter = checkter;
	}

	public java.lang.Long getWmsoutid() {
		return wmsoutid;
	}

	public void setWmsoutid(java.lang.Long wmsoutid) {
		this.wmsoutid = wmsoutid;
	}

	public java.lang.Long getWmsinid() {
		return wmsinid;
	}

	public void setWmsinid(java.lang.Long wmsinid) {
		this.wmsinid = wmsinid;
	}

	public java.lang.String getWmsoutnos() {
		return wmsoutnos;
	}

	public void setWmsoutnos(java.lang.String wmsoutnos) {
		this.wmsoutnos = wmsoutnos;
	}

	public java.lang.String getWmsinnos() {
		return wmsinnos;
	}

	public void setWmsinnos(java.lang.String wmsinnos) {
		this.wmsinnos = wmsinnos;
	}

	public java.lang.String getRemark() {
		return remark;
	}

	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}

	public java.lang.Long getCorpid() {
		return corpid;
	}

	public void setCorpid(java.lang.Long corpid) {
		this.corpid = corpid;
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