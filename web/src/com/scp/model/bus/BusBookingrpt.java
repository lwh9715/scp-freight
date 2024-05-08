package com.scp.model.bus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "bus_bookingrpt")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusBookingrpt implements java.io.Serializable{
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "saleid")
	private java.lang.Long saleid;
	
	@Column(name = "deptid")
	private java.lang.Long deptid;
	
	@Column(name = "polid")
	private java.lang.Long polid;
	
	@Column(name = "podid")
	private java.lang.Long podid;
	
	@Column(name = "sodate")
	private java.util.Date sodate;
	
	@Column(name = "carrierid")
	private java.lang.Long carrierid;
	
	@Column(name = "line", length = 30)
	private java.lang.String line;
	
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	@Column(name = "isSubmit")
	private Boolean isSubmit;
	
	@Column(name = "unit20gp")
	private int unit20gp;
	
	@Column(name = "unit40gp")
	private int unit40gp;
	
	@Column(name = "unit40hq")
	private int unit40hq;
	
	@Column(name = "unit45hq")
	private int unit45hq;
	
	@Column(name = "unitother")
	private int unitother;
	
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

	public java.lang.Long getSaleid() {
		return saleid;
	}

	public void setSaleid(java.lang.Long saleid) {
		this.saleid = saleid;
	}

	public java.lang.Long getDeptid() {
		return deptid;
	}

	public void setDeptid(java.lang.Long deptid) {
		this.deptid = deptid;
	}

	public java.lang.Long getPolid() {
		return polid;
	}

	public void setPolid(java.lang.Long polid) {
		this.polid = polid;
	}

	public java.lang.Long getPodid() {
		return podid;
	}

	public void setPodid(java.lang.Long podid) {
		this.podid = podid;
	}

	public java.util.Date getSodate() {
		return sodate;
	}

	public void setSodate(java.util.Date sodate) {
		this.sodate = sodate;
	}

	public java.lang.Long getCarrierid() {
		return carrierid;
	}

	public void setCarrierid(java.lang.Long carrierid) {
		this.carrierid = carrierid;
	}

	public java.lang.String getLine() {
		return line;
	}

	public void setLine(java.lang.String line) {
		this.line = line;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
	}

	public Boolean getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(Boolean isSubmit) {
		this.isSubmit = isSubmit;
	}

	public int getUnit20gp() {
		return unit20gp;
	}

	public void setUnit20gp(int unit20gp) {
		this.unit20gp = unit20gp;
	}

	public int getUnit40gp() {
		return unit40gp;
	}

	public void setUnit40gp(int unit40gp) {
		this.unit40gp = unit40gp;
	}

	public int getUnit40hq() {
		return unit40hq;
	}

	public void setUnit40hq(int unit40hq) {
		this.unit40hq = unit40hq;
	}

	public int getUnit45hq() {
		return unit45hq;
	}

	public void setUnit45hq(int unit45hq) {
		this.unit45hq = unit45hq;
	}

	public int getUnitother() {
		return unitother;
	}

	public void setUnitother(int unitother) {
		this.unitother = unitother;
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

	@Override
	public String toString() {
		return "BusBookingrpt [carrierid=" + carrierid + ", deptid=" + deptid
				+ ", id=" + id + ", inputer=" + inputer + ", inputtime="
				+ inputtime + ", isSubmit=" + isSubmit + ", isdelete="
				+ isdelete + ", line=" + line + ", podid=" + podid + ", polid="
				+ polid + ", remarks=" + remarks + ", saleid=" + saleid
				+ ", sodate=" + sodate + ", unit20gp=" + unit20gp
				+ ", unit40gp=" + unit40gp + ", unit40hq=" + unit40hq
				+ ", unit45hq=" + unit45hq + ", unitother=" + unitother
				+ ", updater=" + updater + ", updatetime=" + updatetime + "]";
	}
	
	
	
}
