package com.scp.model.sys;

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
@Table(name = "price_fcl_asking")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class PriceFclAsk implements java.io.Serializable{
	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "askerid")
	private java.lang.Long askerid;
	
	/**
	 *@generated
	 */
	@Column(name = "fclid", length = 100)
	private java.lang.String fclid; 
	
	/**
	 *@generated
	 */
	@Column(name = "pod", length = 30)
	private java.lang.String pod;
	
	/**
	 *@generated
	 */
	@Column(name = "pol", length = 30)
	private java.lang.String pol;
	
	/**
	 *@generated
	 */
	@Column(name = "carrier")
	private java.lang.String carrier;
	
	/**
	 *@generated
	 */
	@Column(name = "cls")
	private java.util.Date cls;
	
	
	@Column(name = "cost20")
	private java.math.BigDecimal cost20;
	
	
	@Column(name = "cost40gp")
	private java.math.BigDecimal cost40gp;
	
	
	@Column(name = "cost40hq")
	private java.math.BigDecimal cost40hq;
	
	
	@Column(name = "cost45hq")
	private java.math.BigDecimal cost45hq;
	
	/**
	 *@generated
	 */
	@Column(name = "asker")
	private java.lang.String asker;
	
	@Column(name = "asktime")
	private java.util.Date asktime;
	
	@Column(name = "resp_cost20")
	private java.math.BigDecimal resp_cost20;
	
	@Column(name = "resp_cost40gp")
	private java.math.BigDecimal resp_cost40gp;
	
	@Column(name = "resp_cost40hq")
	private java.math.BigDecimal resp_cost40hq;
	
	@Column(name = "resp_cost45hq")
	private java.math.BigDecimal resp_cost45hq;
	
	@Column(name = "resaskerid")
	private java.lang.Long resaskerid;
	/**
	 *@generated
	 */
	@Column(name = "resper")
	private java.lang.String resper;

	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	/**
	 *@generated
	 */
	@Column(name = "resp_remarks")
	private java.lang.String resp_remarks;
	
	/**
	 *@generated
	 */
	@Column(name = "resptime")
	private java.util.Date resptime;
	
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
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	
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

	public java.lang.Long getAskerid() {
		return askerid;
	}

	public void setAskerid(java.lang.Long askerid) {
		this.askerid = askerid;
	}

	public java.lang.Long getResaskerid() {
		return resaskerid;
	}

	public void setResaskerid(java.lang.Long resaskerid) {
		this.resaskerid = resaskerid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getFclid() {
		return fclid;
	}

	public void setFclid(java.lang.String fclid) {
		this.fclid = fclid;
	}

	public java.lang.String getPod() {
		return pod;
	}

	public void setPod(java.lang.String pod) {
		this.pod = pod;
	}

	public java.lang.String getPol() {
		return pol;
	}

	public void setPol(java.lang.String pol) {
		this.pol = pol;
	}

	public java.lang.String getCarrier() {
		return carrier;
	}

	public void setCarrier(java.lang.String carrier) {
		this.carrier = carrier;
	}

	public java.util.Date getCls() {
		return cls;
	}

	public void setCls(java.util.Date cls) {
		this.cls = cls;
	}

	public java.math.BigDecimal getCost20() {
		return cost20;
	}

	public void setCost20(java.math.BigDecimal cost20) {
		this.cost20 = cost20;
	}

	public java.math.BigDecimal getCost40gp() {
		return cost40gp;
	}

	public void setCost40gp(java.math.BigDecimal cost40gp) {
		this.cost40gp = cost40gp;
	}

	public java.math.BigDecimal getCost40hq() {
		return cost40hq;
	}

	public void setCost40hq(java.math.BigDecimal cost40hq) {
		this.cost40hq = cost40hq;
	}

	public java.math.BigDecimal getCost45hq() {
		return cost45hq;
	}

	public void setCost45hq(java.math.BigDecimal cost45hq) {
		this.cost45hq = cost45hq;
	}

	public java.lang.String getAsker() {
		return asker;
	}

	public void setAsker(java.lang.String asker) {
		this.asker = asker;
	}

	public java.util.Date getAsktime() {
		return asktime;
	}

	public void setAsktime(java.util.Date asktime) {
		this.asktime = asktime;
	}

	public java.math.BigDecimal getResp_cost20() {
		return resp_cost20;
	}

	public void setResp_cost20(java.math.BigDecimal respCost20) {
		resp_cost20 = respCost20;
	}

	public java.math.BigDecimal getResp_cost40gp() {
		return resp_cost40gp;
	}

	public void setResp_cost40gp(java.math.BigDecimal respCost40gp) {
		resp_cost40gp = respCost40gp;
	}

	public java.math.BigDecimal getResp_cost40hq() {
		return resp_cost40hq;
	}

	public void setResp_cost40hq(java.math.BigDecimal respCost40hq) {
		resp_cost40hq = respCost40hq;
	}

	public java.math.BigDecimal getResp_cost45hq() {
		return resp_cost45hq;
	}

	public void setResp_cost45hq(java.math.BigDecimal respCost45hq) {
		resp_cost45hq = respCost45hq;
	}

	public java.lang.String getResper() {
		return resper;
	}

	public void setResper(java.lang.String resper) {
		this.resper = resper;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
	}

	public java.lang.String getResp_remarks() {
		return resp_remarks;
	}

	public void setResp_remarks(java.lang.String respRemarks) {
		resp_remarks = respRemarks;
	}

	public java.util.Date getResptime() {
		return resptime;
	}

	public void setResptime(java.util.Date resptime) {
		this.resptime = resptime;
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

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
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

 