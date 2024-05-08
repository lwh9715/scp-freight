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
@Table(name = "bus_airflight")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusAirFlight implements java.io.Serializable {

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
	
	@Column(name = "code", length = 30)
	private java.lang.String code;
	
	@Column(name = "line", length = 20)
	private java.lang.String line;
	
	/**
	 *@generated
	 */
	@Column(name = "carrier", length = 20)
	private java.lang.String carrier;
	
	@Column(name = "schedule", length = 20)
	private java.lang.String schedule;
	

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
	@Column(name = "via", length = 20)
	private java.lang.String via;

	/**
	 *@generated
	 */
	@Column(name = "cls", length = 20)
	private java.lang.String cls;

	

	/**
	 *@generated
	 */
	@Column(name = "eta", length = 20)
	private java.lang.String eta;
	
	/**
	 *@generated
	 */
	@Column(name = "etd", length = 20)
	private java.lang.String etd;
	
	/**
	 *@generated
	 */
	@Column(name = "tt", length = 30)
	private java.lang.String tt;
	
	
	@Column(name = "carrytype", length = 20)
	private java.lang.String carrytype;

	@Column(name = "corpid")
	private long corpid;
	
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getCode() {
		return code;
	}

	public void setCode(java.lang.String code) {
		this.code = code;
	}


	public java.lang.String getCarrier() {
		return carrier;
	}

	public void setCarrier(java.lang.String carrier) {
		this.carrier = carrier;
	}

	public java.lang.String getSchedule() {
		return schedule;
	}

	public void setSchedule(java.lang.String schedule) {
		this.schedule = schedule;
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

	public java.lang.String getVia() {
		return via;
	}

	public void setVia(java.lang.String via) {
		this.via = via;
	}

	public java.lang.String getCls() {
		return cls;
	}

	public void setCls(java.lang.String cls) {
		this.cls = cls;
	}

	public java.lang.String getEta() {
		return eta;
	}

	public void setEta(java.lang.String eta) {
		this.eta = eta;
	}

	public java.lang.String getEtd() {
		return etd;
	}

	public void setEtd(java.lang.String etd) {
		this.etd = etd;
	}

	public java.lang.String getTt() {
		return tt;
	}

	public void setTt(java.lang.String tt) {
		this.tt = tt;
	}

	public java.lang.String getCarrytype() {
		return carrytype;
	}

	public void setCarrytype(java.lang.String carrytype) {
		this.carrytype = carrytype;
	}

	public long getCorpid() {
		return corpid;
	}

	public void setCorpid(long corpid) {
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

	public java.lang.String getLine() {
		return line;
	}

	public void setLine(java.lang.String line) {
		this.line = line;
	}

	
	
}