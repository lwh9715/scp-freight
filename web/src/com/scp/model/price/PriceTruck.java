package com.scp.model.price;

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
@Table(name = "price_truck")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class PriceTruck implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	@Column(name = "pricename", length = 30)
	private java.lang.String pricename;
	
	@Column(name = "isrelease")
	private java.lang.Boolean isrelease;
	
	@Column(name = "daterrelease", length = 35)
	private java.util.Date daterrelease;
	
	@Column(name = "datefm", length = 35)
	private java.util.Date datefm;
	
	@Column(name = "dateto", length = 35)
	private java.util.Date dateto;
	
	@Column(name = "poa_city", length = 30)
	private java.lang.String poa_city;
	
	@Column(name = "poa_area", length = 30)
	private java.lang.String poa_area;
	
	@Column(name = "poa_street", length = 100)
	private java.lang.String poa_street;
	
	@Column(name = "pod", length = 30)
	private java.lang.String pod;
	
	@Column(name = "feeadd_desc")
	private java.lang.String feeadd_desc;
	
	@Column(name = "remark_in")
	private java.lang.String remark_in;
	
	@Column(name = "remark_out")
	private java.lang.String remark_out;
	
	@Column(name = "corpid")
	private java.lang.Long corpid;
	
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	@Column(name = "inputer", length = 30)
	private java.lang.String inputer;

	@Column(name = "inputtime", length = 35)
	private java.util.Date inputtime;

	@Column(name = "updater", length = 30)
	private java.lang.String updater;

	@Column(name = "updatetime", length = 35)
	private java.util.Date updatetime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getPricename() {
		return pricename;
	}

	public void setPricename(java.lang.String pricename) {
		this.pricename = pricename;
	}

	public java.lang.Boolean getIsrelease() {
		return isrelease;
	}

	public void setIsrelease(java.lang.Boolean isrelease) {
		this.isrelease = isrelease;
	}

	public java.util.Date getDaterrelease() {
		return daterrelease;
	}

	public void setDaterrelease(java.util.Date daterrelease) {
		this.daterrelease = daterrelease;
	}

	public java.util.Date getDatefm() {
		return datefm;
	}

	public void setDatefm(java.util.Date datefm) {
		this.datefm = datefm;
	}

	public java.util.Date getDateto() {
		return dateto;
	}

	public void setDateto(java.util.Date dateto) {
		this.dateto = dateto;
	}

	public java.lang.String getPoa_city() {
		return poa_city;
	}

	public void setPoa_city(java.lang.String poaCity) {
		poa_city = poaCity;
	}

	public java.lang.String getPoa_area() {
		return poa_area;
	}

	public void setPoa_area(java.lang.String poaArea) {
		poa_area = poaArea;
	}

	public java.lang.String getPoa_street() {
		return poa_street;
	}

	public void setPoa_street(java.lang.String poaStreet) {
		poa_street = poaStreet;
	}

	public java.lang.String getPod() {
		return pod;
	}

	public void setPod(java.lang.String pod) {
		this.pod = pod;
	}

	public java.lang.String getFeeadd_desc() {
		return feeadd_desc;
	}

	public void setFeeadd_desc(java.lang.String feeaddDesc) {
		feeadd_desc = feeaddDesc;
	}

	public java.lang.String getRemark_in() {
		return remark_in;
	}

	public void setRemark_in(java.lang.String remarkIn) {
		remark_in = remarkIn;
	}

	public java.lang.String getRemark_out() {
		return remark_out;
	}

	public void setRemark_out(java.lang.String remarkOut) {
		remark_out = remarkOut;
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