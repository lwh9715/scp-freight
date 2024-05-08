package com.scp.model.ship;

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
@Table(name = "bus_customs_taxret")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusCustomsTaxret implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy")
	private long id;

	/**
	 *@generated
	 */
	@Column(name = "customsid")
	private java.lang.Long customsid;

	/**
	 *@generated
	 */
	@Column(name = "isgetbill")
	private java.lang.Boolean isgetbill;

	/**
	 *@generated
	 */
	@Column(name = "getbilldate")
	private java.util.Date getbilldate;
	
	/**
	 *@generated
	 */
	@Column(name = "istocus")
	private java.lang.Boolean istocus;

	/**
	 *@generated
	 */
	@Column(name = "tocusdate")
	private java.util.Date tocusdate;
	
	/**
	 *@generated
	 */
	@Column(name = "isretcus")
	private java.lang.Boolean isretcus;

	/**
	 *@generated
	 */
	@Column(name = "retcusdate")
	private java.util.Date retcusdate;
	
	/**
	 *@generated
	 */
	@Column(name = "isfinasign")
	private java.lang.Boolean isfinasign;

	/**
	 *@generated
	 */
	@Column(name = "finasigndate")
	private java.util.Date finasigndate;
	
	/**
	 *@generated
	 */
	@Column(name = "isrescheck")
	private java.lang.Boolean isrescheck;
	
	/**
	 *@generated
	 */
	@Column(name = "reschecker", length = 30)
	private java.lang.String reschecker;

	/**
	 *@generated
	 */
	@Column(name = "rescheckdate")
	private java.util.Date rescheckdate;

	/**
	 *@generated
	 */
	@Column(name = "isres")
	private java.lang.Boolean isres;
	
	/**
	 *@generated
	 */
	@Column(name = "reser", length = 30)
	private java.lang.String reser;

	/**
	 *@generated
	 */
	@Column(name = "resdate")
	private java.util.Date resdate;
	
	/**
	 *@generated
	 */
	@Column(name = "expno", length = 30)
	private java.lang.String expno;
	
	/**
	 *@generated
	 */
	@Column(name = "expcus", length = 30)
	private java.lang.String expcus;
	
	/**
	 *@generated
	 */
	@Column(name = "iscssign")
	private java.lang.Boolean iscssign;
	
	/**
	 *@generated
	 */
	@Column(name = "cssigner", length = 30)
	private java.lang.String cssigner;

	/**
	 *@generated
	 */
	@Column(name = "cssigndate")
	private java.util.Date cssigndate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.Long getCustomsid() {
		return customsid;
	}

	public void setCustomsid(java.lang.Long customsid) {
		this.customsid = customsid;
	}

	public java.lang.Boolean getIsgetbill() {
		return isgetbill;
	}

	public void setIsgetbill(java.lang.Boolean isgetbill) {
		this.isgetbill = isgetbill;
	}

	public java.util.Date getGetbilldate() {
		return getbilldate;
	}

	public void setGetbilldate(java.util.Date getbilldate) {
		this.getbilldate = getbilldate;
	}

	public java.lang.Boolean getIstocus() {
		return istocus;
	}

	public void setIstocus(java.lang.Boolean istocus) {
		this.istocus = istocus;
	}

	public java.util.Date getTocusdate() {
		return tocusdate;
	}

	public void setTocusdate(java.util.Date tocusdate) {
		this.tocusdate = tocusdate;
	}

	public java.lang.Boolean getIsretcus() {
		return isretcus;
	}

	public void setIsretcus(java.lang.Boolean isretcus) {
		this.isretcus = isretcus;
	}

	public java.util.Date getRetcusdate() {
		return retcusdate;
	}

	public void setRetcusdate(java.util.Date retcusdate) {
		this.retcusdate = retcusdate;
	}

	public java.lang.Boolean getIsfinasign() {
		return isfinasign;
	}

	public void setIsfinasign(java.lang.Boolean isfinasign) {
		this.isfinasign = isfinasign;
	}

	public java.util.Date getFinasigndate() {
		return finasigndate;
	}

	public void setFinasigndate(java.util.Date finasigndate) {
		this.finasigndate = finasigndate;
	}

	public java.lang.Boolean getIsrescheck() {
		return isrescheck;
	}

	public void setIsrescheck(java.lang.Boolean isrescheck) {
		this.isrescheck = isrescheck;
	}

	public java.lang.String getReschecker() {
		return reschecker;
	}

	public void setReschecker(java.lang.String reschecker) {
		this.reschecker = reschecker;
	}

	public java.util.Date getRescheckdate() {
		return rescheckdate;
	}

	public void setRescheckdate(java.util.Date rescheckdate) {
		this.rescheckdate = rescheckdate;
	}

	public java.lang.Boolean getIsres() {
		return isres;
	}

	public void setIsres(java.lang.Boolean isres) {
		this.isres = isres;
	}

	public java.lang.String getReser() {
		return reser;
	}

	public void setReser(java.lang.String reser) {
		this.reser = reser;
	}

	public java.util.Date getResdate() {
		return resdate;
	}

	public void setResdate(java.util.Date resdate) {
		this.resdate = resdate;
	}

	public java.lang.String getExpno() {
		return expno;
	}

	public void setExpno(java.lang.String expno) {
		this.expno = expno;
	}

	public java.lang.String getExpcus() {
		return expcus;
	}

	public void setExpcus(java.lang.String expcus) {
		this.expcus = expcus;
	}

	public java.lang.Boolean getIscssign() {
		return iscssign;
	}

	public void setIscssign(java.lang.Boolean iscssign) {
		this.iscssign = iscssign;
	}

	public java.lang.String getCssigner() {
		return cssigner;
	}

	public void setCssigner(java.lang.String cssigner) {
		this.cssigner = cssigner;
	}

	public java.util.Date getCssigndate() {
		return cssigndate;
	}

	public void setCssigndate(java.util.Date cssigndate) {
		this.cssigndate = cssigndate;
	}
	
}