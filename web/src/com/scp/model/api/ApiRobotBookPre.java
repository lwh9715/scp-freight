package com.scp.model.api;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "api_robot_book_pre")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class ApiRobotBookPre implements java.io.Serializable {
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	@Column(name = "nos", length = 30)
	private java.lang.String nos;
	
	@Column(name = "pol")
	private java.lang.String pol;
	
	@Column(name = "pod" )
	private java.lang.String pod;
	
	@Column(name = "etd")
	private java.util.Date etd;
	
	@Column(name = "num20gp" )
	private long num20gp;
	

	@Column(name = "num40gp" )
	private long num40gp;
	
	@Column(name = "num40hq" )
	private long num40hq;
	
	@Column(name = "num45hq" )
	private long num45hq;
	
	@Column(name = "carriercode",length = 10 )
	private java.lang.String carriercode;
	
	@Column(name="startime")
	private java.util.Date startime;
	@Column(name="trycount")
	private int trycount;
	@Column(name="nmax")
	private int nmax;
	@Column(name = "inputer" , length = 100)
	private java.lang.String inputer;
	
	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;
	
	@Column(name = "updater" , length = 100)
	private java.lang.String updater;
	
	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;
	
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
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

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
	}

	public int getNmax() {
		return nmax;
	}

	public void setNmax(int nmax) {
		this.nmax = nmax;
	}

	public java.lang.String getCarriercode() {
		return carriercode;
	}

	public void setCarriercode(java.lang.String carriercode) {
		this.carriercode = carriercode;
	}

	public java.util.Date getStartime() {
		return startime;
	}

	public void setStartime(java.util.Date startime) {
		this.startime = startime;
	}

	public int getTrycount() {
		return trycount;
	}

	public void setTrycount(int trycount) {
		this.trycount = trycount;
	}

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

	public java.lang.String getPol() {
		return pol;
	}

	public void setPol(java.lang.String pol) {
		this.pol = pol;
	}

	public java.lang.String getPod() {
		return pod;
	}

	public void setPod(java.lang.String pod) {
		this.pod = pod;
	}

	public java.util.Date getEtd() {
		return etd;
	}

	public void setEtd(java.util.Date etd) {
		this.etd = etd;
	}

	public long getNum20gp() {
		return num20gp;
	}

	public void setNum20gp(long num20gp) {
		this.num20gp = num20gp;
	}

	public long getNum40gp() {
		return num40gp;
	}

	public void setNum40gp(long num40gp) {
		this.num40gp = num40gp;
	}

	public long getNum40hq() {
		return num40hq;
	}

	public void setNum40hq(long num40hq) {
		this.num40hq = num40hq;
	}

	public long getNum45hq() {
		return num45hq;
	}

	public void setNum45hq(long num45hq) {
		this.num45hq = num45hq;
	}
	

	
	
}
