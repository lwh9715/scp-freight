package com.scp.model.ship;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "bus_booking_train")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class MgrTrainBooking implements java.io.Serializable{
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;
	
	@Column(name = "trainno")
	private java.lang.String trainno;
	
	@Column(name = "traindate")
	private Date traindate;
	
	@Column(name = "polid")
	private Long polid;
	
	@Column(name = "podid")
	private Long podid;
	
	@Column(name = "cbmtotle")
	private java.math.BigDecimal cbmtotle;
	
	@Column(name = "wgttotle")
	private java.math.BigDecimal wgttotle;
	
	@Column(name = "piecehq")
	private Integer piecehq;
	
	@Column(name = "bktype")
	private java.lang.String bktype;
	
	@Column(name = "isdelete")
	private boolean isdelete;
	
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

	public java.lang.String getTrainno() {
		return trainno;
	}

	public void setTrainno(java.lang.String trainno) {
		this.trainno = trainno;
	}

	public Date getTraindate() {
		return traindate;
	}

	public void setTraindate(Date traindate) {
		this.traindate = traindate;
	}

	public Long getPolid() {
		return polid;
	}

	public void setPolid(Long polid) {
		this.polid = polid;
	}

	public Long getPodid() {
		return podid;
	}

	public void setPodid(Long podid) {
		this.podid = podid;
	}

	public java.math.BigDecimal getCbmtotle() {
		return cbmtotle;
	}

	public void setCbmtotle(java.math.BigDecimal cbmtotle) {
		this.cbmtotle = cbmtotle;
	}

	public java.math.BigDecimal getWgttotle() {
		return wgttotle;
	}

	public void setWgttotle(java.math.BigDecimal wgttotle) {
		this.wgttotle = wgttotle;
	}

	public Integer getPiecehq() {
		return piecehq;
	}

	public void setPiecehq(Integer piecehq) {
		this.piecehq = piecehq;
	}

	public java.lang.String getBktype() {
		return bktype;
	}

	public void setBktype(java.lang.String bktype) {
		this.bktype = bktype;
	}

	public boolean isIsdelete() {
		return isdelete;
	}

	public void setIsdelete(boolean isdelete) {
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

	@Override
	public String toString() {
		return "MgrTrainBooking [bktype=" + bktype + ", cbmtotle=" + cbmtotle
				+ ", id=" + id + ", inputer=" + inputer + ", inputtime="
				+ inputtime + ", isdelete=" + isdelete + ", piecehq=" + piecehq
				+ ", podid=" + podid + ", polid=" + polid + ", traindate="
				+ traindate + ", trainno=" + trainno + ", updater=" + updater
				+ ", updatetime=" + updatetime + ", wgttotle=" + wgttotle + "]";
	}
}
