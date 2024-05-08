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
@Table(name = "price_train_lcl")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class PriceTrainLcl implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;
	
	@Column(name = "pol", length = 30)
	private java.lang.String pol;
	
	@Column(name = "pod", length = 30)
	private java.lang.String pod;
	
	@Column(name = "isrelease")
	private java.lang.Boolean isrelease;
	
	@Column(name = "daterrelease", length = 35)
	private java.util.Date daterrelease;
	
	@Column(name = "datefm", length = 35)
	private java.util.Date datefm;
	
	@Column(name = "dateto", length = 35)
	private java.util.Date dateto;
	
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
	
	@Column(name = "poa")
	private java.lang.String poa;
	
	@Column(name = "costpricel")
	private java.math.BigDecimal costpricel;
	
	@Column(name = "basepricel")
	private java.math.BigDecimal basepricel;
	
	@Column(name = "costpriceh")
	private java.math.BigDecimal costpriceh;
	
	@Column(name = "basepriceh")
	private java.math.BigDecimal basepriceh;
	
	@Column(name = "tt")
	private java.lang.String tt;
	
	@Column(name = "etd")
	private java.lang.String etd;

	public java.lang.String getEtd() {
		return etd;
	}

	public void setEtd(java.lang.String etd) {
		this.etd = etd;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public java.lang.String getPoa() {
		return poa;
	}

	public void setPoa(java.lang.String poa) {
		this.poa = poa;
	}

	public java.math.BigDecimal getCostpricel() {
		return costpricel;
	}

	public void setCostpricel(java.math.BigDecimal costpricel) {
		this.costpricel = costpricel;
	}

	public java.math.BigDecimal getBasepricel() {
		return basepricel;
	}

	public void setBasepricel(java.math.BigDecimal basepricel) {
		this.basepricel = basepricel;
	}

	public java.math.BigDecimal getCostpriceh() {
		return costpriceh;
	}

	public void setCostpriceh(java.math.BigDecimal costpriceh) {
		this.costpriceh = costpriceh;
	}

	public java.math.BigDecimal getBasepriceh() {
		return basepriceh;
	}

	public void setBasepriceh(java.math.BigDecimal basepriceh) {
		this.basepriceh = basepriceh;
	}

	public java.lang.String getTt() {
		return tt;
	}

	public void setTt(java.lang.String tt) {
		this.tt = tt;
	}

	
	
}