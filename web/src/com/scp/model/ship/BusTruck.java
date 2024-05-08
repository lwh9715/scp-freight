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
@Table(name = "bus_truck")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusTruck implements java.io.Serializable {

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
	@Column(name = "truckid")
	private java.lang.Long truckid;
	
	@Column(name = "logisticsid")
	private java.lang.Long logisticsid;

	/**
	 *@generated
	 */
	@Column(name = "truckabbr", length = 50)
	private java.lang.String truckabbr;

	/**
	 *@generated
	 */
	@Column(name = "areatype", nullable = false, length = 1)
	private java.lang.String areatype;

	public java.lang.Long getLogisticsid() {
		return logisticsid;
	}

	public void setLogisticsid(java.lang.Long logisticsid) {
		this.logisticsid = logisticsid;
	}

	/**
	 *@generated
	 */
	@Column(name = "driverno", length = 50)
	private java.lang.String driverno;

	/**
	 *@generated
	 */
	@Column(name = "driver", length = 50)
	private java.lang.String driver;

	/**
	 *@generated
	 */
	@Column(name = "drivertel", length = 30)
	private java.lang.String drivertel;

	/**
	 *@generated
	 */
	@Column(name = "drivermobile", length = 30)
	private java.lang.String drivermobile;

	/**
	 *@generated
	 */
	@Column(name = "loadtime", length = 29)
	private java.util.Date loadtime;

	/**
	 *@generated
	 */
	@Column(name = "loadaddress")
	private java.lang.String loadaddress;

	/**
	 *@generated
	 */
	@Column(name = "loadcontact", length = 50)
	private java.lang.String loadcontact;

	/**
	 *@generated
	 */
	@Column(name = "contacttel", length = 30)
	private java.lang.String contacttel;

	/**
	 *@generated
	 */
	@Column(name = "contactmobile", length = 30)
	private java.lang.String contactmobile;

	/**
	 *@generated
	 */
	@Column(name = "loadremarks")
	private java.lang.String loadremarks;
	
	@Column(name = "factory")
	private java.lang.String factory;

	/**
	 *@generated
	 */
	@Column(name = "isweighreq")
	private java.lang.Boolean isweighreq;
	
	@Column(name = "isinsurance")
	private java.lang.Boolean isinsurance;
	
	@Column(name = "iscls")
	private java.lang.Boolean iscls;
	
	@Column(name = "ischecknumber")
	private java.lang.Boolean ischecknumber;

	/**
	 *@generated
	 */
	@Column(name = "isdriverlicreq")
	private java.lang.Boolean isdriverlicreq;
	
	/**
	 *@generated
	 */
	@Column(name = "isgentrade")
	private java.lang.Boolean isgentrade;

	/**
	 *@generated
	 */
	@Column(name = "truckstate", nullable = false, length = 1)
	private java.lang.String truckstate;

	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	@Column(name = "trucktype")
	private java.lang.String trucktype;

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
	 *@generated
	 */
	@Column(name = "amountar")
	private java.math.BigDecimal amountar; 
	
	@Column(name = "user_ctrl")
	private java.lang.String user_ctrl;
	
	@Column(name = "cusdoc_toaddr")
	private java.lang.String cusdoc_toaddr;
	
	/**
	 *@generated
	 */
	@Column(name = "fumigatingcontact", length = 100)
	private java.lang.String fumigatingcontact;
	
	/**
	 *@generated
	 */
	@Column(name = "fumigatingtel", length = 100)
	private java.lang.String fumigatingtel;
	
	@Column(name = "reportname", length = 50)
	private java.lang.String reportname;
	
	@Column(name = "special")
	private java.lang.String special;
	
	@Column(name = "truckcontact", length = 100)
	private java.lang.String truckcontact;
	
	@Column(name = "truckaddress")
	private java.lang.String truckaddress;
	
	@Column(name = "unloadfactory")
	private java.lang.String unloadfactory;
	
	@Column(name = "unloadtime", length = 35)
	private java.util.Date unloadtime;
	
	@Column(name = "unloadcontact", length = 100)
	private java.lang.String unloadcontact;
	
	@Column(name = "unloadconttel", length = 100)
	private java.lang.String unloadconttel;
	
	@Column(name = "unloadaddress")
	private java.lang.String unloadaddress;
	
	@Column(name = "unloadremarks")
	private java.lang.String unloadremarks;
	
	@Column(name = "isdriverbook")
	private java.lang.Boolean isdriverbook;
	
	@Column(name = "customsid")
	private java.lang.Long customsid;
	
	@Column(name = "customscontact", length = 100)
	private java.lang.String customscontact;
	
	@Column(name = "customsconttel", length = 100)
	private java.lang.String customsconttel;
	
	@Column(name = "customsaddress")
	private java.lang.String customsaddress;
	
	@Column(name = "customsremarks")
	private java.lang.String customsremarks;
	
	@Column(name = "customstype", length = 1)
	private java.lang.String customstype;
	
	@Column(name = "customclass", length = 1)
	private java.lang.String customclass;
	
	@Column(name = "feedesc")
	private java.lang.String feedesc;
	
	@Column(name = "feeremarks")
	private java.lang.String feeremarks;
	
	@Column(name = "customno")
	private java.lang.String customno;
	
	
	@Column(name = "cabinetlocation", length = 50)
	private java.lang.String cabinetlocation;
	
	@Column(name = "returncabinet", length = 50)
	private java.lang.String returncabinet;
	
	@Column(name = "loadplace", length = 50)
	private java.lang.String loadplace;
	
	@Column(name = "destination", length = 50)
	private java.lang.String destination;
	
	@Column(name = "exitport")
	private java.lang.String exitport;
	
	@Column(name = "customsidhk")
	private java.lang.String customsidhk;
	
	@Column(name = "customscontacthk", length = 100)
	private java.lang.String customscontacthk;
	
	@Column(name = "customsconttelhk", length = 100)
	private java.lang.String customsconttelhk;
	
	@Column(name = "sono")
	private java.lang.String sono;
	
	@Column(name = "cls", length = 29)
	private java.util.Date cls;
	
	@Column(name = "carrierid")
	private java.lang.Long carrierid;
	
	@Column(name = "vessel", length = 50)
	private java.lang.String vessel;
	
	@Column(name = "voyage", length = 20)
	private java.lang.String voyage;
	
	@Column(name = "loadinfo")
	private java.lang.String loadinfo;
	
	@Column(name = "customhkinfo")
	private java.lang.String customhkinfo;
	
	@Column(name = "etd", length = 29)
	private java.util.Date etd;
	
	@Column(name = "currencyar", length = 10)
	private java.lang.String currencyar;
	
	@Column(name = "currencyap", length = 10)
	private java.lang.String currencyap;
	
	@Column(name = "driverno2", length = 50)
	private java.lang.String driverno2;

	@Column(name = "driver2", length = 50)
	private java.lang.String driver2;
	
	@Column(name = "drivertel2", length = 30)
	private java.lang.String drivertel2;
	
	@Column(name = "inlandway")
	private java.lang.String inlandway;

	@Column(name = "addrcntget")
	private java.lang.String addrcntget;

	@Column(name = "addcntret")
	private java.lang.String addcntret;

	@Column(name = "addwms")
	private java.lang.String addwms;


	/**
	 *@generated
	 */
	@Column(name = "goodsokdate", length = 35)
	private java.util.Date goodsokdate;


	public Date getGoodsokdate() {
		return goodsokdate;
	}

	public void setGoodsokdate(Date goodsokdate) {
		this.goodsokdate = goodsokdate;
	}

	public String getAddrcntget() {
		return addrcntget;
	}

	public void setAddrcntget(String addrcntget) {
		this.addrcntget = addrcntget;
	}

	public String getAddcntret() {
		return addcntret;
	}

	public void setAddcntret(String addcntret) {
		this.addcntret = addcntret;
	}

	public String getAddwms() {
		return addwms;
	}

	public void setAddwms(String addwms) {
		this.addwms = addwms;
	}

	public java.lang.String getInlandway() {
		return inlandway;
	}

	public void setInlandway(java.lang.String inlandway) {
		this.inlandway = inlandway;
	}

	public java.math.BigDecimal getMgruserid() {
		return mgruserid;
	}

	public void setMgruserid(java.math.BigDecimal mgruserid) {
		this.mgruserid = mgruserid;
	}

	/**
	 *@generated
	 */
	@Column(name = "mgruserid")
	private java.math.BigDecimal mgruserid; 
	
	
	public java.lang.Boolean getIsgentrade() {
		return isgentrade;
	}

	public void setIsgentrade(java.lang.Boolean isgentrade) {
		this.isgentrade = isgentrade;
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
	public java.lang.Long getTruckid() {
		return this.truckid;
	}

	/**
	 *@generated
	 */
	public void setTruckid(java.lang.Long value) {
		this.truckid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getTruckabbr() {
		return this.truckabbr;
	}

	/**
	 *@generated
	 */
	public void setTruckabbr(java.lang.String value) {
		this.truckabbr = value;
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
	public java.lang.String getDriverno() {
		return this.driverno;
	}

	/**
	 *@generated
	 */
	public void setDriverno(java.lang.String value) {
		this.driverno = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getDriver() {
		return this.driver;
	}

	/**
	 *@generated
	 */
	public void setDriver(java.lang.String value) {
		this.driver = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getDrivertel() {
		return this.drivertel;
	}

	/**
	 *@generated
	 */
	public void setDrivertel(java.lang.String value) {
		this.drivertel = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getDrivermobile() {
		return this.drivermobile;
	}

	/**
	 *@generated
	 */
	public void setDrivermobile(java.lang.String value) {
		this.drivermobile = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getLoadtime() {
		return this.loadtime;
	}

	/**
	 *@generated
	 */
	public void setLoadtime(java.util.Date value) {
		this.loadtime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getLoadaddress() {
		return this.loadaddress;
	}

	/**
	 *@generated
	 */
	public void setLoadaddress(java.lang.String value) {
		this.loadaddress = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getLoadcontact() {
		return this.loadcontact;
	}

	/**
	 *@generated
	 */
	public void setLoadcontact(java.lang.String value) {
		this.loadcontact = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getContacttel() {
		return this.contacttel;
	}

	/**
	 *@generated
	 */
	public void setContacttel(java.lang.String value) {
		this.contacttel = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getContactmobile() {
		return this.contactmobile;
	}

	/**
	 *@generated
	 */
	public void setContactmobile(java.lang.String value) {
		this.contactmobile = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getLoadremarks() {
		return this.loadremarks;
	}

	/**
	 *@generated
	 */
	public void setLoadremarks(java.lang.String value) {
		this.loadremarks = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIsweighreq() {
		return this.isweighreq;
	}

	/**
	 *@generated
	 */
	public void setIsweighreq(java.lang.Boolean value) {
		this.isweighreq = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIsdriverlicreq() {
		return this.isdriverlicreq;
	}

	/**
	 *@generated
	 */
	public void setIsdriverlicreq(java.lang.Boolean value) {
		this.isdriverlicreq = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getTruckstate() {
		return this.truckstate;
	}

	/**
	 *@generated
	 */
	public void setTruckstate(java.lang.String value) {
		this.truckstate = value;
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

	public java.lang.String getFactory() {
		return factory;
	}

	public void setFactory(java.lang.String factory) {
		this.factory = factory;
	}

	public java.math.BigDecimal getAmountar() {
		return amountar;
	}

	public void setAmountar(java.math.BigDecimal amountar) {
		this.amountar = amountar;
	}

	public java.lang.Boolean getIsinsurance() {
		return isinsurance;
	}

	public void setIsinsurance(java.lang.Boolean isinsurance) {
		this.isinsurance = isinsurance;
	}

	public java.lang.String getTrucktype() {
		return trucktype;
	}

	public void setTrucktype(java.lang.String trucktype) {
		this.trucktype = trucktype;
	}

	public java.lang.Boolean getIschecknumber() {
		return ischecknumber;
	}

	public void setIschecknumber(java.lang.Boolean ischecknumber) {
		this.ischecknumber = ischecknumber;
	}

	public java.lang.Boolean getIscls() {
		return iscls;
	}

	public void setIscls(java.lang.Boolean iscls) {
		this.iscls = iscls;
	}

	public java.lang.String getUser_ctrl() {
		return user_ctrl;
	}

	public void setUser_ctrl(java.lang.String userCtrl) {
		user_ctrl = userCtrl;
	}

	public java.lang.String getCusdoc_toaddr() {
		return cusdoc_toaddr;
	}

	public void setCusdoc_toaddr(java.lang.String cusdocToaddr) {
		cusdoc_toaddr = cusdocToaddr;
	}

	public java.lang.String getFumigatingcontact() {
		return fumigatingcontact;
	}

	public void setFumigatingcontact(java.lang.String fumigatingcontact) {
		this.fumigatingcontact = fumigatingcontact;
	}

	public java.lang.String getFumigatingtel() {
		return fumigatingtel;
	}

	public void setFumigatingtel(java.lang.String fumigatingtel) {
		this.fumigatingtel = fumigatingtel;
	}

	public java.lang.String getReportname() {
		return reportname;
	}

	public void setReportname(java.lang.String reportname) {
		this.reportname = reportname;
	}

	public java.lang.String getTruckcontact() {
		return truckcontact;
	}

	public void setTruckcontact(java.lang.String truckcontact) {
		this.truckcontact = truckcontact;
	}

	public java.lang.String getTruckaddress() {
		return truckaddress;
	}

	public void setTruckaddress(java.lang.String truckaddress) {
		this.truckaddress = truckaddress;
	}

	public java.lang.String getUnloadfactory() {
		return unloadfactory;
	}

	public void setUnloadfactory(java.lang.String unloadfactory) {
		this.unloadfactory = unloadfactory;
	}

	public java.util.Date getUnloadtime() {
		return unloadtime;
	}

	public void setUnloadtime(java.util.Date unloadtime) {
		this.unloadtime = unloadtime;
	}

	public java.lang.String getUnloadcontact() {
		return unloadcontact;
	}

	public void setUnloadcontact(java.lang.String unloadcontact) {
		this.unloadcontact = unloadcontact;
	}

	public java.lang.String getUnloadconttel() {
		return unloadconttel;
	}

	public void setUnloadconttel(java.lang.String unloadconttel) {
		this.unloadconttel = unloadconttel;
	}

	public java.lang.String getUnloadaddress() {
		return unloadaddress;
	}

	public void setUnloadaddress(java.lang.String unloadaddress) {
		this.unloadaddress = unloadaddress;
	}

	public java.lang.String getUnloadremarks() {
		return unloadremarks;
	}

	public void setUnloadremarks(java.lang.String unloadremarks) {
		this.unloadremarks = unloadremarks;
	}

	public java.lang.Boolean getIsdriverbook() {
		return isdriverbook;
	}

	public void setIsdriverbook(java.lang.Boolean isdriverbook) {
		this.isdriverbook = isdriverbook;
	}

	public java.lang.Long getCustomsid() {
		return customsid;
	}

	public void setCustomsid(java.lang.Long customsid) {
		this.customsid = customsid;
	}

	public java.lang.String getCustomscontact() {
		return customscontact;
	}

	public void setCustomscontact(java.lang.String customscontact) {
		this.customscontact = customscontact;
	}

	public java.lang.String getCustomsconttel() {
		return customsconttel;
	}

	public void setCustomsconttel(java.lang.String customsconttel) {
		this.customsconttel = customsconttel;
	}

	public java.lang.String getCustomsaddress() {
		return customsaddress;
	}

	public void setCustomsaddress(java.lang.String customsaddress) {
		this.customsaddress = customsaddress;
	}

	public java.lang.String getCustomsremarks() {
		return customsremarks;
	}

	public void setCustomsremarks(java.lang.String customsremarks) {
		this.customsremarks = customsremarks;
	}

	public java.lang.String getCustomstype() {
		return customstype;
	}

	public void setCustomstype(java.lang.String customstype) {
		this.customstype = customstype;
	}

	public java.lang.String getFeedesc() {
		return feedesc;
	}

	public void setFeedesc(java.lang.String feedesc) {
		this.feedesc = feedesc;
	}

	public java.lang.String getFeeremarks() {
		return feeremarks;
	}

	public void setFeeremarks(java.lang.String feeremarks) {
		this.feeremarks = feeremarks;
	}

	public java.lang.String getCabinetlocation() {
		return cabinetlocation;
	}

	public void setCabinetlocation(java.lang.String cabinetlocation) {
		this.cabinetlocation = cabinetlocation;
	}

	public java.lang.String getReturncabinet() {
		return returncabinet;
	}

	public void setReturncabinet(java.lang.String returncabinet) {
		this.returncabinet = returncabinet;
	}

	public java.lang.String getLoadplace() {
		return loadplace;
	}

	public void setLoadplace(java.lang.String loadplace) {
		this.loadplace = loadplace;
	}

	public java.lang.String getDestination() {
		return destination;
	}

	public void setDestination(java.lang.String destination) {
		this.destination = destination;
	}

	public java.lang.String getExitport() {
		return exitport;
	}

	public void setExitport(java.lang.String exitport) {
		this.exitport = exitport;
	}

	public java.lang.String getCustomscontacthk() {
		return customscontacthk;
	}

	public void setCustomscontacthk(java.lang.String customscontacthk) {
		this.customscontacthk = customscontacthk;
	}

	public java.lang.String getCustomsconttelhk() {
		return customsconttelhk;
	}

	public void setCustomsconttelhk(java.lang.String customsconttelhk) {
		this.customsconttelhk = customsconttelhk;
	}

	public java.lang.String getSono() {
		return sono;
	}

	public void setSono(java.lang.String sono) {
		this.sono = sono;
	}

	public java.util.Date getCls() {
		return cls;
	}

	public void setCls(java.util.Date cls) {
		this.cls = cls;
	}

	public java.lang.Long getCarrierid() {
		return carrierid;
	}

	public void setCarrierid(java.lang.Long carrierid) {
		this.carrierid = carrierid;
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

	public java.lang.String getSpecial() {
		return special;
	}

	public void setSpecial(java.lang.String special) {
		this.special = special;
	}

	public java.lang.String getCustomsidhk() {
		return customsidhk;
	}

	public void setCustomsidhk(java.lang.String customsidhk) {
		this.customsidhk = customsidhk;
	}

	public java.lang.String getLoadinfo() {
		return loadinfo;
	}

	public void setLoadinfo(java.lang.String loadinfo) {
		this.loadinfo = loadinfo;
	}

	public java.lang.String getCustomhkinfo() {
		return customhkinfo;
	}

	public void setCustomhkinfo(java.lang.String customhkinfo) {
		this.customhkinfo = customhkinfo;
	}

	public java.util.Date getEtd() {
		return etd;
	}

	public void setEtd(java.util.Date etd) {
		this.etd = etd;
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

	public java.lang.String getCustomclass() {
		return customclass;
	}

	public void setCustomclass(java.lang.String customclass) {
		this.customclass = customclass;
	}

	public java.lang.String getCustomno() {
		return customno;
	}

	public void setCustomno(java.lang.String customno) {
		this.customno = customno;
	}

	public java.lang.String getDriverno2() {
		return driverno2;
	}

	public void setDriverno2(java.lang.String driverno2) {
		this.driverno2 = driverno2;
	}

	public java.lang.String getDriver2() {
		return driver2;
	}

	public void setDriver2(java.lang.String driver2) {
		this.driver2 = driver2;
	}

	public java.lang.String getDrivertel2() {
		return drivertel2;
	}

	public void setDrivertel2(java.lang.String drivertel2) {
		this.drivertel2 = drivertel2;
	}
	
}