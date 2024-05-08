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

/**
 *@generated
 */
@Table(name = "bus_customs")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusCustoms implements java.io.Serializable {

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
	@Column(name = "nos", length = 30)
	private java.lang.String nos;

	/**
	 *@generated
	 */
	@Column(name = "singtime", length = 35)
	private java.util.Date singtime;

	/**
	 *@generated
	 */
	@Column(name = "jobid")
	private java.lang.Long jobid;

	/**
	 *@generated
	 */
	@Column(name = "customid")
	private java.lang.Long customid;

	/**
	 *@generated
	 */
	@Column(name = "customabbr", length = 50)
	private java.lang.String customabbr;

	/**
	 *@generated
	 */
	@Column(name = "areatype", nullable = false, length = 1)
	private java.lang.String areatype;

	/**
	 *@generated
	 */
	@Column(name = "custype", length = 1)
	private java.lang.String custype;

	/**
	 *@generated
	 */
	@Column(name = "taxretno")
	private java.lang.String taxretno;
	
	@Column(name = "status")
	private java.lang.String status;

	/**
	 *@generated
	 */
	@Column(name = "releasetime", length = 15)
	private java.util.Date releasetime;

	/**
	 *@generated
	 */
	@Column(name = "customstate", nullable = false, length = 1)
	private java.lang.String customstate;

	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String remarks;

	/**
	 *@generated
	 */
	@Column(name = "corpid")
	private java.lang.Long corpid;

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
	
	/**
	 *@generated
	 */
	@Column(name = "amountap")
	private java.math.BigDecimal amountAp;
	
	
	/**
	 *@generated 清关
	 */
	@Column(name = "clstype")
	private java.lang.String clstype;
	
	/**
	 *@generated
	 */
	@Column(name = "dono",length=30)
	private java.lang.String dono;
	/**
	 *@generated
	 */
	@Column(name = "clsdonedate")
	private java.util.Date clsdonedate;
	/**
	 *@generated
	 */
	@Column(name = "impcode",length=30)
	private java.lang.String impcode;
	/**
	 *@generated
	 */
	@Column(name = "clsuserid")
	private java.math.BigDecimal clsuserid;
	/**
	 *@generated
	 */
	@Column(name = "taxretitle")
	private java.lang.String taxretitle;
	/**
	 *@generated
	 */
	@Column(name = "expaddress")
	private java.lang.String expaddress ;
	/**
	 *@generated
	 */
	@Column(name = "exptel")
	private java.lang.String exptel ;
	/**
	 *@generated
	 */
	@Column(name = "expcontact")
	private java.lang.String expcontact ;
	
	/**
	 *@generated
	 */
	@Column(name = "amountar")
	private java.math.BigDecimal amountar; 
	
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
	@Column(name = "date_lfday")
	private java.util.Date date_lfday;
	/**
	 *@generated
	 */
	@Column(name = "date_lfd")
	private java.util.Date date_lfd;
	/**
	 *@generated
	 */
	@Column(name = "date_ecr")
	private java.util.Date date_ecr;
	/**
	 *@generated
	 */
	@Column(name = "date_dt")
	private java.util.Date date_dt;
	
	/**
	 *@generated
	 */
	@Column(name = "cargolocation",length=30)
	private java.lang.String cargolocation;
	
	/**
	 *@generated
	 */
	@Column(name = "address")
	private java.lang.String address;
	
	/**
	 *@generated
	 */
	@Column(name = "itno",length=30)
	private java.lang.String itno;
	/**
	 *@generated
	 */
	@Column(name = "itdate")
	private java.util.Date itdate;
	/**
	 *@generated
	 */
	@Column(name = "itlocation")
	private java.lang.String itlocation;
	/**
	 *@generated
	 */
	@Column(name = "amsno",length=30)
	private java.lang.String amsno;
	
	/**
	 *@generated
	 */
	@Column(name = "miscellaneous")
	private java.lang.String miscellaneous;
	
	/**
	 *@generated
	 */
	@Column(name = "isfno",length=30)
	private java.lang.String isfno;
	
	/**
	 *@generated
	 */
	@Column(name = "remarks4")
	private java.lang.String remarks4;
	
	/**
	 *@generated
	 */
	@Column(name = "remarks2")
	private java.lang.String remarks2;
	
	/**
	 *@generated
	 */
	@Column(name = "remarks3")
	private java.lang.String remarks3;
	
	@Column(name = "clearancecusid")
	private long clearancecusid;
	
	@Column(name = "clearancedate")
	private Date clearancedate;
	
	@Column(name = "staircasedate")
	private Date staircasedate;
	
	@Column(name = "staircaseeta")
	private Date staircaseeta;
	
	@Column(name = "clearanceremark")
	private java.lang.String clearanceremark;
	
	@Column(name = "description")
	private java.lang.String description;
	
	@Column(name = "stealgoods")
	private java.lang.Boolean stealgoods;
	
	@Column(name = "lessgoods")
	private java.lang.Boolean lessgoods;
	
	@Column(name = "waterwet")
	private java.lang.Boolean waterwet;
	
	@Column(name = "moregoods")
	private java.lang.Boolean moregoods;
	
	@Column(name = "othergoods")
	private java.lang.Boolean othergoods;
	
	@Column(name = "stealboxes")
	private java.lang.Integer stealboxes;
	
	@Column(name = "lessboxes")
	private java.lang.Integer lessboxes;
	
	@Column(name = "waterwetboxes")
	private java.lang.Integer waterwetboxes;
	
	@Column(name = "moreboxes")
	private java.lang.Integer moreboxes;

	@Column(name = "otherboxes")
	private java.lang.Integer otherboxes;
	
	@Column(name = "clearingstate")
	private java.lang.String clearingstate;
	
	@Column(name = "destination")
	private java.lang.String destination;
	
	@Column(name = "clearancedays")
	private java.lang.Integer clearancedays;
	
	@Column(name = "clearancedelay")
	private java.lang.Integer clearancedelay;
	
	@Column(name = "clearancebqdays")
	private java.lang.Integer clearancebqdays;
	
	@Column(name = "clearancedelays")
	private java.lang.Integer clearancedelays;

	@Column(name = "agentdesid")
	private long agentdesid;
	
	@Column(name = "clearancenos", length = 30)
	private java.lang.String clearancenos;
	
	/**
	 *@generated
	 */
	@Column(name = "storehousedate")
	private java.util.Date storehousedate;
	
	@Column(name = "cntcheck_time", length = 15)
	private java.util.Date cntcheck_time;
	
	@Column(name = "cntrelease_time", length = 15)
	private java.util.Date cntrelease_time;
	
	@Column(name = "cabinet")
	private java.lang.Boolean cabinet;
	
	@Column(name = "refno", length = 100)
	private java.lang.String refno;
	
	@Column(name = "sono")
	private java.lang.String sono;
	
	@Column(name = "customno")
	private java.lang.String customno;
	
	@Column(name = "operationid")
	private java.lang.Long operationid;

	@Column(name = "vessel", length = 50)
	private java.lang.String vessel;
	
	@Column(name = "voyage", length = 20)
	private java.lang.String voyage;
	
	@Column(name = "poa", length = 50)
	private java.lang.String poa;
	
	@Column(name = "poacls", length = 15)
	private java.util.Date poacls;
	
	@Column(name = "pol", length = 20)
	private java.lang.String pol;
	
	@Column(name = "cls", length = 15)
	private java.util.Date cls;
	
	
	@Column(name = "cusclass")
	private java.lang.String cusclass;
	
	/**
	 *@generated
	 */
	@Column(name = "cusbackdate", length = 35)
	private java.util.Date cusbackdate;
	
	@Column(name = "backtodate", length = 35)
	private java.util.Date backtodate;
	
	
	@Column(name = "currencyar", length = 10)
	private java.lang.String currencyar;
	
	@Column(name = "currencyap", length = 10)
	private java.lang.String currencyap;
	
	@Column(name = "delivercompany")
	private java.lang.String delivercompany;
	
	public java.lang.String getTaxretitle() {
		return taxretitle;
	}

	public void setTaxretitle(java.lang.String taxretitle) {
		this.taxretitle = taxretitle;
	}

	public java.lang.String getExpaddress() {
		return expaddress;
	}

	public void setExpaddress(java.lang.String expaddress) {
		this.expaddress = expaddress;
	}

	public java.lang.String getExptel() {
		return exptel;
	}

	public void setExptel(java.lang.String exptel) {
		this.exptel = exptel;
	}

	public java.lang.String getExpcontact() {
		return expcontact;
	}

	public void setExpcontact(java.lang.String expcontact) {
		this.expcontact = expcontact;
	}


	

	public java.lang.String getClstype() {
		return clstype;
	}

	public void setClstype(java.lang.String clstype) {
		this.clstype = clstype;
	}

	public java.lang.String getDono() {
		return dono;
	}

	public void setDono(java.lang.String dono) {
		this.dono = dono;
	}

	public java.util.Date getClsdonedate() {
		return clsdonedate;
	}

	public void setClsdonedate(java.util.Date clsdonedate) {
		this.clsdonedate = clsdonedate;
	}

	public java.lang.String getImpcode() {
		return impcode;
	}

	public void setImpcode(java.lang.String impcode) {
		this.impcode = impcode;
	}

	public java.math.BigDecimal getClsuserid() {
		return clsuserid;
	}

	public void setClsuserid(java.math.BigDecimal clsuserid) {
		this.clsuserid = clsuserid;
	}

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
	public java.lang.String getNos() {
		return this.nos;
	}

	/**
	 *@generated
	 */
	public void setNos(java.lang.String value) {
		this.nos = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getSingtime() {
		return this.singtime;
	}

	/**
	 *@generated
	 */
	public void setSingtime(java.util.Date value) {
		this.singtime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getJobid() {
		return this.jobid;
	}

	/**
	 *@generated
	 */
	public void setJobid(java.lang.Long value) {
		this.jobid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getCustomid() {
		return this.customid;
	}

	/**
	 *@generated
	 */
	public void setCustomid(java.lang.Long value) {
		this.customid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCustomabbr() {
		return this.customabbr;
	}

	/**
	 *@generated
	 */
	public void setCustomabbr(java.lang.String value) {
		this.customabbr = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getAreatype() {
		return this.areatype;
	}

	/**
	 *@generated
	 */
	public void setAreatype(java.lang.String value) {
		this.areatype = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCustype() {
		return this.custype;
	}

	/**
	 *@generated
	 */
	public void setCustype(java.lang.String value) {
		this.custype = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getTaxretno() {
		return this.taxretno;
	}

	/**
	 *@generated
	 */
	public void setTaxretno(java.lang.String value) {
		this.taxretno = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getReleasetime() {
		return this.releasetime;
	}

	/**
	 *@generated
	 */
	public void setReleasetime(java.util.Date value) {
		this.releasetime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCustomstate() {
		return this.customstate;
	}

	/**
	 *@generated
	 */
	public void setCustomstate(java.lang.String value) {
		this.customstate = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getRemarks() {
		return this.remarks;
	}

	/**
	 *@generated
	 */
	public void setRemarks(java.lang.String value) {
		this.remarks = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getCorpid() {
		return this.corpid;
	}

	/**
	 *@generated
	 */
	public void setCorpid(java.lang.Long value) {
		this.corpid = value;
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

	public java.math.BigDecimal getAmountAp() {
		return amountAp;
	}

	public void setAmountAp(java.math.BigDecimal amountAp) {
		this.amountAp = amountAp;
	}

	public java.math.BigDecimal getAmountar() {
		return amountar;
	}

	public void setAmountar(java.math.BigDecimal amountar) {
		this.amountar = amountar;
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

	public java.util.Date getDate_lfday() {
		return date_lfday;
	}

	public void setDate_lfday(java.util.Date dateLfday) {
		date_lfday = dateLfday;
	}

	public java.util.Date getDate_lfd() {
		return date_lfd;
	}

	public void setDate_lfd(java.util.Date dateLfd) {
		date_lfd = dateLfd;
	}

	public java.util.Date getDate_ecr() {
		return date_ecr;
	}

	public void setDate_ecr(java.util.Date dateEcr) {
		date_ecr = dateEcr;
	}

	public java.util.Date getDate_dt() {
		return date_dt;
	}

	public void setDate_dt(java.util.Date dateDt) {
		date_dt = dateDt;
	}

	public java.lang.String getCargolocation() {
		return cargolocation;
	}

	public void setCargolocation(java.lang.String cargolocation) {
		this.cargolocation = cargolocation;
	}

	public java.lang.String getAddress() {
		return address;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	public java.lang.String getItno() {
		return itno;
	}

	public void setItno(java.lang.String itno) {
		this.itno = itno;
	}

	public java.util.Date getItdate() {
		return itdate;
	}

	public void setItdate(java.util.Date itdate) {
		this.itdate = itdate;
	}

	public java.lang.String getItlocation() {
		return itlocation;
	}

	public void setItlocation(java.lang.String itlocation) {
		this.itlocation = itlocation;
	}

	public java.lang.String getAmsno() {
		return amsno;
	}

	public void setAmsno(java.lang.String amsno) {
		this.amsno = amsno;
	}

	public java.lang.String getMiscellaneous() {
		return miscellaneous;
	}

	public void setMiscellaneous(java.lang.String miscellaneous) {
		this.miscellaneous = miscellaneous;
	}

	public java.lang.String getIsfno() {
		return isfno;
	}

	public void setIsfno(java.lang.String isfno) {
		this.isfno = isfno;
	}

	public java.lang.String getRemarks4() {
		return remarks4;
	}

	public void setRemarks4(java.lang.String remarks4) {
		this.remarks4 = remarks4;
	}

	public java.lang.String getRemarks2() {
		return remarks2;
	}

	public void setRemarks2(java.lang.String remarks2) {
		this.remarks2 = remarks2;
	}

	public java.lang.String getRemarks3() {
		return remarks3;
	}

	public void setRemarks3(java.lang.String remarks3) {
		this.remarks3 = remarks3;
	}

	public java.lang.String getStatus() {
		return status;
	}

	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	public long getClearancecusid() {
		return clearancecusid;
	}

	public void setClearancecusid(long clearancecusid) {
		this.clearancecusid = clearancecusid;
	}

	public Date getClearancedate() {
		return clearancedate;
	}

	public void setClearancedate(Date clearancedate) {
		this.clearancedate = clearancedate;
	}

	public Date getStaircasedate() {
		return staircasedate;
	}

	public void setStaircasedate(Date staircasedate) {
		this.staircasedate = staircasedate;
	}

	public Date getStaircaseeta() {
		return staircaseeta;
	}

	public void setStaircaseeta(Date staircaseeta) {
		this.staircaseeta = staircaseeta;
	}

	public java.lang.String getClearanceremark() {
		return clearanceremark;
	}

	public void setClearanceremark(java.lang.String clearanceremark) {
		this.clearanceremark = clearanceremark;
	}

	public java.lang.String getDescription() {
		return description;
	}

	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	public java.lang.Boolean getStealgoods() {
		return stealgoods;
	}

	public void setStealgoods(java.lang.Boolean stealgoods) {
		this.stealgoods = stealgoods;
	}

	public java.lang.Boolean getLessgoods() {
		return lessgoods;
	}

	public void setLessgoods(java.lang.Boolean lessgoods) {
		this.lessgoods = lessgoods;
	}

	public java.lang.Boolean getWaterwet() {
		return waterwet;
	}

	public void setWaterwet(java.lang.Boolean waterwet) {
		this.waterwet = waterwet;
	}

	public java.lang.Boolean getMoregoods() {
		return moregoods;
	}

	public void setMoregoods(java.lang.Boolean moregoods) {
		this.moregoods = moregoods;
	}

	public java.lang.Boolean getOthergoods() {
		return othergoods;
	}

	public void setOthergoods(java.lang.Boolean othergoods) {
		this.othergoods = othergoods;
	}

	public java.lang.Integer getStealboxes() {
		return stealboxes;
	}

	public void setStealboxes(java.lang.Integer stealboxes) {
		this.stealboxes = stealboxes;
	}

	public java.lang.Integer getLessboxes() {
		return lessboxes;
	}

	public void setLessboxes(java.lang.Integer lessboxes) {
		this.lessboxes = lessboxes;
	}

	public java.lang.Integer getWaterwetboxes() {
		return waterwetboxes;
	}

	public void setWaterwetboxes(java.lang.Integer waterwetboxes) {
		this.waterwetboxes = waterwetboxes;
	}

	public java.lang.Integer getMoreboxes() {
		return moreboxes;
	}

	public void setMoreboxes(java.lang.Integer moreboxes) {
		this.moreboxes = moreboxes;
	}

	public java.lang.Integer getOtherboxes() {
		return otherboxes;
	}

	public void setOtherboxes(java.lang.Integer otherboxes) {
		this.otherboxes = otherboxes;
	}

	public java.lang.String getClearingstate() {
		return clearingstate;
	}

	public void setClearingstate(java.lang.String clearingstate) {
		this.clearingstate = clearingstate;
	}

	public java.lang.String getDestination() {
		return destination;
	}

	public void setDestination(java.lang.String destination) {
		this.destination = destination;
	}

	public java.lang.Integer getClearancedays() {
		return clearancedays;
	}

	public void setClearancedays(java.lang.Integer clearancedays) {
		this.clearancedays = clearancedays;
	}

	public java.lang.Integer getClearancedelay() {
		return clearancedelay;
	}

	public void setClearancedelay(java.lang.Integer clearancedelay) {
		this.clearancedelay = clearancedelay;
	}

	public java.lang.Integer getClearancebqdays() {
		return clearancebqdays;
	}

	public void setClearancebqdays(java.lang.Integer clearancebqdays) {
		this.clearancebqdays = clearancebqdays;
	}

	public java.lang.Integer getClearancedelays() {
		return clearancedelays;
	}

	public void setClearancedelays(java.lang.Integer clearancedelays) {
		this.clearancedelays = clearancedelays;
	}

	public long getAgentdesid() {
		return agentdesid;
	}

	public void setAgentdesid(long agentdesid) {
		this.agentdesid = agentdesid;
	}

	public java.lang.String getClearancenos() {
		return clearancenos;
	}

	public void setClearancenos(java.lang.String clearancenos) {
		this.clearancenos = clearancenos;
	}

	public java.util.Date getStorehousedate() {
		return storehousedate;
	}

	public void setStorehousedate(java.util.Date storehousedate) {
		this.storehousedate = storehousedate;
	}

	public java.util.Date getCntcheck_time() {
		return cntcheck_time;
	}

	public void setCntcheck_time(java.util.Date cntcheckTime) {
		cntcheck_time = cntcheckTime;
	}

	public java.util.Date getCntrelease_time() {
		return cntrelease_time;
	}

	public void setCntrelease_time(java.util.Date cntreleaseTime) {
		cntrelease_time = cntreleaseTime;
	}

	public java.lang.Boolean getCabinet() {
		return cabinet;
	}

	public void setCabinet(java.lang.Boolean cabinet) {
		this.cabinet = cabinet;
	}

	public java.lang.String getRefno() {
		return refno;
	}

	public void setRefno(java.lang.String refno) {
		this.refno = refno;
	}

	public java.lang.String getSono() {
		return sono;
	}

	public void setSono(java.lang.String sono) {
		this.sono = sono;
	}

	public java.lang.String getCustomno() {
		return customno;
	}

	public void setCustomno(java.lang.String customno) {
		this.customno = customno;
	}

	public java.lang.Long getOperationid() {
		return operationid;
	}

	public void setOperationid(java.lang.Long operationid) {
		this.operationid = operationid;
	}

	public java.lang.String getVessel() {
		return vessel;
	}

	public void setVessel(java.lang.String vessel) {
		this.vessel = vessel;
	}

	public java.lang.String getVoyage() {
		return voyage;
	}

	public void setVoyage(java.lang.String voyage) {
		this.voyage = voyage;
	}

	public java.lang.String getPoa() {
		return poa;
	}

	public void setPoa(java.lang.String poa) {
		this.poa = poa;
	}

	public java.util.Date getPoacls() {
		return poacls;
	}

	public void setPoacls(java.util.Date poacls) {
		this.poacls = poacls;
	}

	public java.lang.String getPol() {
		return pol;
	}

	public void setPol(java.lang.String pol) {
		this.pol = pol;
	}

	public java.util.Date getCls() {
		return cls;
	}

	public void setCls(java.util.Date cls) {
		this.cls = cls;
	}

	public java.util.Date getCusbackdate() {
		return cusbackdate;
	}

	public void setCusbackdate(java.util.Date cusbackdate) {
		this.cusbackdate = cusbackdate;
	}

	public java.util.Date getBacktodate() {
		return backtodate;
	}

	public void setBacktodate(java.util.Date backtodate) {
		this.backtodate = backtodate;
	}

	public java.lang.String getCurrencyar() {
		return currencyar;
	}

	public void setCurrencyar(java.lang.String currencyar) {
		this.currencyar = currencyar;
	}

	public java.lang.String getCurrencyap() {
		return currencyap;
	}

	public void setCurrencyap(java.lang.String currencyap) {
		this.currencyap = currencyap;
	}

	public java.lang.String getCusclass() {
		return cusclass;
	}

	public void setCusclass(java.lang.String cusclass) {
		this.cusclass = cusclass;
	}

	public java.lang.String getDelivercompany() {
		return delivercompany;
	}

	public void setDelivercompany(java.lang.String delivercompany) {
		this.delivercompany = delivercompany;
	}

}