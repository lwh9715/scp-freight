package com.scp.model.bus;

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
@Table(name = "bus_shipschedule")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusShipSchedule implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	/**
	 *@generated
	 */
	@Column(name = "shipline", length = 20)
	private java.lang.String shipline;
	
	@Column(name = "yearno")
	private Integer yearno;
	
	@Column(name = "monthno")
	private Integer monthno;
	
	@Column(name = "weekno")
	private Integer weekno;
	
	@Column(name = "cy")
	private java.util.Date cy;
	
	@Column(name = "cv")
	private java.util.Date cv;
	
	@Column(name = "vgm")
	private java.util.Date vgm;
	
	/**
	 *@generated
	 */
	@Column(name = "carrier", length = 20)
	private java.lang.String carrier;
	
	@Column(name = "schtype", length = 1)
	private java.lang.String schtype;
	

	/**
	 *@generated
	 */
	@Column(name = "pol", length = 20)
	private java.lang.String pol;

	/**
	 *@generated
	 */
	@Column(name = "pod", length = 20)
	private java.lang.String pod;

	/**
	 *@generated
	 */
	@Column(name = "ves", length = 30)
	private java.lang.String ves;

	/**
	 *@generated
	 */
	@Column(name = "voy", length = 30)
	private java.lang.String voy;

	/**
	 *@generated
	 */
	@Column(name = "cls")
	private java.util.Date cls;

	/**
	 *@generated
	 */
	@Column(name = "eta")
	private java.util.Date eta;
	
	/**
	 *@generated
	 */
	@Column(name = "ata")
	private java.util.Date ata;
	
	/**
	 *@generated
	 */
	@Column(name = "atd")
	private java.util.Date atd;
	
	/**
	 *@generated
	 */
	@Column(name = "etd")
	private java.util.Date etd;
	
	/**
	 *@generated
	 */
	@Column(name = "tt")
	private java.lang.String tt;

	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

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

	@Column(name = "vescode", length = 50)
	private java.lang.String vescode;
	
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
	public java.lang.String getCarrier() {
		return this.carrier;
	}

	/**
	 *@generated
	 */
	public void setCarrier(java.lang.String value) {
		this.carrier = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getPol() {
		return this.pol;
	}

	/**
	 *@generated
	 */
	public void setPol(java.lang.String value) {
		this.pol = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getPod() {
		return this.pod;
	}

	/**
	 *@generated
	 */
	public void setPod(java.lang.String value) {
		this.pod = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getVes() {
		return this.ves;
	}

	/**
	 *@generated
	 */
	public void setVes(java.lang.String value) {
		this.ves = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getVoy() {
		return this.voy;
	}

	/**
	 *@generated
	 */
	public void setVoy(java.lang.String value) {
		this.voy = value;
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

	public java.lang.String getShipline() {
		return shipline;
	}

	public void setShipline(java.lang.String shipline) {
		this.shipline = shipline;
	}

	public Integer getYearno() {
		return yearno;
	}

	public void setYearno(Integer yearno) {
		this.yearno = yearno;
	}

	public Integer getMonthno() {
		return monthno;
	}

	public void setMonthno(Integer monthno) {
		this.monthno = monthno;
	}

	public java.lang.String getSchtype() {
		return schtype;
	}

	public void setSchtype(java.lang.String schtype) {
		this.schtype = schtype;
	}

	public Integer getWeekno() {
		return weekno;
	}

	public void setWeekno(Integer weekno) {
		this.weekno = weekno;
	}


	public java.lang.String getTt() {
		return tt;
	}

	public void setTt(java.lang.String tt) {
		this.tt = tt;
	}
	
	public java.util.Date getCy() {
		return cy;
	}

	public void setCy(java.util.Date cy) {
		this.cy = cy;
	}

	public java.util.Date getCv() {
		return cv;
	}

	public void setCv(java.util.Date cv) {
		this.cv = cv;
	}

	public java.util.Date getVgm() {
		return vgm;
	}

	public void setVgm(java.util.Date vgm) {
		this.vgm = vgm;
	}

	public java.lang.String getVescode() {
		return vescode;
	}

	public void setVescode(java.lang.String vescode) {
		this.vescode = vescode;
	}

	public java.util.Date getCls() {
		return cls;
	}

	public void setCls(java.util.Date cls) {
		this.cls = cls;
	}

	public java.util.Date getEta() {
		return eta;
	}

	public void setEta(java.util.Date eta) {
		this.eta = eta;
	}

	public java.util.Date getAta() {
		return ata;
	}

	public void setAta(java.util.Date ata) {
		this.ata = ata;
	}

	public java.util.Date getAtd() {
		return atd;
	}

	public void setAtd(java.util.Date atd) {
		this.atd = atd;
	}

	public java.util.Date getEtd() {
		return etd;
	}

	public void setEtd(java.util.Date etd) {
		this.etd = etd;
	}

}