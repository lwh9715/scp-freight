package com.scp.model.ship;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.scp.util.StrUtils;

/**
 *@generated
 */
@Table(name = "bus_ship_booking")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusShipBooking implements java.io.Serializable {
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
	@Column(name = "carrierid")
	private java.lang.Long carrierid;
	
	@Column(name = "salesid")
	private java.lang.Long salesid;

	@Column(name = "jobid")
	private java.lang.Long jobid;

	/**
	 *@generated
	 */
	@Column(name = "vessel", length = 30)
	private java.lang.String vessel;

	/**
	 *@generated
	 */
	@Column(name = "voyage", length = 20)
	private java.lang.String voyage;

	/**
	 *@generated
	 */
	@Column(name = "polid")
	private java.lang.Long polid;

	/**
	 *@generated
	 */
	@Column(name = "pol", length = 50)
	private java.lang.String pol;

	/**
	 *@generated
	 */
	@Column(name = "podid")
	private java.lang.Long podid;

	/**
	 *@generated
	 */
	@Column(name = "pod", length = 50)
	private java.lang.String pod;

	@Column(name = "pddid")
	private java.lang.Long pddid;

	@Column(name = "pdd", length = 50)
	private java.lang.String pdd;
	
	/**
	 *@generated
	 */
	@Column(name = "schedule_year")
	private java.lang.Integer scheduleYear;

	/**
	 *@generated
	 */
	@Column(name = "schedule_month")
	private java.lang.Integer scheduleMonth;

	/**
	 *@generated
	 */
	@Column(name = "cls", length = 13)
	private java.util.Date cls;

	/**
	 *@generated
	 */
	@Column(name = "etd", length = 13)
	private java.util.Date etd;

	/**
	 *@generated
	 */
	@Column(name = "eta", length = 13)
	private java.util.Date eta;

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
	@Column(name = "inputtime", length = 35)
	private java.util.Date inputtime;

	/**
	 *@generated
	 */
	@Column(name = "updater", length = 100)
	private java.lang.String updater;

	/**
	 *@generated
	 */
	@Column(name = "updatetime", length = 35)
	private java.util.Date updatetime;

	/**
	 *@generated
	 */
	@Column(name = "scheduleid")
	private java.lang.Long scheduleid;

	/**
	 *@generated
	 */
	@Column(name = "schedule_week")
	private java.lang.Integer scheduleWeek;

	/**
	 *@generated
	 */
	@Column(name = "orderid")
	private java.lang.Long orderid;

	/**
	 *@generated
	 */
	@Column(name = "orderno", length = 30)
	private java.lang.String orderno;

	/**
	 *@generated
	 */
	@Column(name = "remarks2")
	private java.lang.String remarks2;

	/**
	 *@generated
	 */
	@Column(name = "barge", length = 1)
	private java.lang.String barge;

	/**
	 *@generated
	 */
	@Column(name = "bargedatefm", length = 13)
	private java.util.Date bargedatefm;

	/**
	 *@generated
	 */
	@Column(name = "bargedateto", length = 13)
	private java.util.Date bargedateto;

	/**
	 *@generated
	 */
	@Column(name = "feetype", length = 1)
	private java.lang.String feetype;

	/**
	 *@generated
	 */
	@Column(name = "nacaccount")
	private java.lang.String nacaccount;

	/**
	 *@generated
	 */
	@Column(name = "ppcctype", length = 2)
	private java.lang.String ppcctype;
	/**
	 *@generated
	 */
	@Column(name = "bookstate", length = 2)
	private java.lang.String bookstate;
	/**
	 *@generated
	 */
	/**
	 *@generated
	 */
	@Column(name = "ccnos")
	private java.lang.String ccnos;

	/**
	 *@generated
	 */
	@Column(name = "ccname")
	private java.lang.String ccname;

	/**
	 *@generated
	 */
	@Column(name = "line", length = 30)
	private java.lang.String line;

	/**
	 *@generated
	 */
	@Column(name = "clsbig", length = 13)
	private java.util.Date clsbig;

	/**
	 *@generated
	 */
	@Column(name = "userprice", length = 30)
	private java.lang.String userprice;

	/**
	 *@generated
	 */
	@Column(name = "userbook", length = 30)
	private java.lang.String userbook;

	/**
	 *@generated
	 */
	@Column(name = "cy", length = 35)
	private java.util.Date cy;

	/**
	 *@generated
	 */
	@Column(name = "cv", length = 35)
	private java.util.Date cv;

	/**
	 *@generated
	 */
	@Column(name = "vgm", length = 35)
	private java.util.Date vgm;

	/**
	 *@generated
	 */
	@Column(name = "sono")
	private java.lang.String sono;

	/**
	 *@generated
	 */
	@Column(name = "goodsnamec")
	private java.lang.String goodsnamec;

	/**
	 *@generated
	 */
	@Column(name = "goodsnamee")
	private java.lang.String goodsnamee;

	/**
	 *@generated
	 */
	@Column(name = "piece1")
	private java.lang.Short piece1;

	/**
	 *@generated
	 */
	@Column(name = "grswgt1")
	private java.math.BigDecimal grswgt1;

	/**
	 *@generated
	 */
	@Column(name = "piece2")
	private java.lang.Short piece2;

	/**
	 *@generated
	 */
	@Column(name = "grswgt2")
	private java.math.BigDecimal grswgt2;

	/**
	 *@generated
	 */
	@Column(name = "piece3")
	private java.lang.Short piece3;

	/**
	 *@generated
	 */
	@Column(name = "grswgt3")
	private java.math.BigDecimal grswgt3;

	/**
	 *@generated
	 */
	@Column(name = "piece4")
	private java.lang.Short piece4;

	/**
	 *@generated
	 */
	@Column(name = "grswgt4")
	private java.math.BigDecimal grswgt4;

	/**
	 *@generated
	 */
	@Column(name = "piece5")
	private java.lang.Short piece5;

	/**
	 *@generated
	 */
	@Column(name = "grswgt5")
	private java.math.BigDecimal grswgt5;

	/**
	 *@generated
	 */
	@Column(name = "piece6")
	private java.lang.Short piece6;

	/**
	 *@generated
	 */
	@Column(name = "grswgt6")
	private java.math.BigDecimal grswgt6;

	/**
	 *@generated
	 */
	@Column(name = "piece7")
	private java.lang.Short piece7;

	/**
	 *@generated
	 */
	@Column(name = "grswgt7")
	private java.math.BigDecimal grswgt7;

	/**
	 *@generated
	 */
	@Column(name = "piece8")
	private java.lang.Short piece8;

	/**
	 *@generated
	 */
	@Column(name = "grswgt8")
	private java.math.BigDecimal grswgt8;

	/**
	 *@generated
	 */
	@Column(name = "piece9")
	private java.lang.Short piece9;

	/**
	 *@generated
	 */
	@Column(name = "grswgt9")
	private java.math.BigDecimal grswgt9;

	/**
	 *@generated
	 */
	@Column(name = "piece10")
	private java.lang.Short piece10;

	/**
	 *@generated
	 */
	@Column(name = "grswgt10")
	private java.math.BigDecimal grswgt10;

	/**
	 *@generated
	 */
	@Column(name = "piece11")
	private java.lang.Short piece11;

	/**
	 *@generated
	 */
	@Column(name = "grswgt11")
	private java.math.BigDecimal grswgt11;

	/**
	 *@generated
	 */
	@Column(name = "piece12")
	private java.lang.Short piece12;

	/**
	 *@generated
	 */
	@Column(name = "grswgt12")
	private java.math.BigDecimal grswgt12;
	
	/**
	 *@generated
	 */
	@Column(name = "piece13")
	private java.lang.Short piece13;

	/**
	 *@generated
	 */
	@Column(name = "grswgt13")
	private java.math.BigDecimal grswgt13;
	
	/**
	 *@generated
	 */
	@Column(name = "remarks_cancel")
	private java.lang.String remarks_cancel;
	
	/**
	 *@generated
	 */
	@Column(name = "islast")
	private java.lang.Boolean islast;
	
	/**
	 *@generated
	 */
	@Column(name = "bargepol", length = 50)
	private java.lang.String bargepol;

	/**
	 *@generated
	 */
	@Column(name = "bargepolid")
	private java.lang.Long bargepolid;
	
	/**
	 *@generated
	 */
	@Column(name = "bargepod", length = 50)
	private java.lang.String bargepod;

	/**
	 *@generated
	 */
	@Column(name = "bargepodid")
	private java.lang.Long bargepodid;
	
	
	@Column(name = "bargepoa", length = 50)
	private java.lang.String bargepoa;

	/**
	 *@generated
	 */
	@Column(name = "bargepoaid")
	private java.lang.Long bargepoaid;
	
	
	@Column(name = "billtype")
	private java.lang.String billtype;
	
	
	@Column(name = "signtime")
	private java.util.Date signtime;
	
	
	@Column(name = "pddname")
	private java.lang.String pddname;
	
	@Column(name = "bargepoaname")
	private java.lang.String bargepoaname;
	
	public java.lang.String getPddname() {
		return pddname;
	}

	public void setPddname(java.lang.String pddname) {
		this.pddname = pddname;
	}

	public java.lang.String getBargepoaname() {
		return bargepoaname;
	}

	public void setBargepoaname(java.lang.String bargepoaname) {
		this.bargepoaname = bargepoaname;
	}

	public java.lang.String getBilltype() {
		return billtype;
	}

	public void setBilltype(java.lang.String billtype) {
		this.billtype = billtype;
	}

	public java.util.Date getSigntime() {
		return signtime;
	}

	public void setSigntime(java.util.Date signtime) {
		this.signtime = signtime;
	}

	public java.lang.String getBargepoa() {
		return bargepoa;
	}

	public void setBargepoa(java.lang.String bargepoa) {
		this.bargepoa = bargepoa;
	}

	public java.lang.Long getBargepoaid() {
		return bargepoaid;
	}

	public void setBargepoaid(java.lang.Long bargepoaid) {
		this.bargepoaid = bargepoaid;
	}

	public java.lang.String getBargepod() {
		return bargepod;
	}

	public void setBargepod(java.lang.String bargepod) {
		this.bargepod = bargepod;
	}

	public java.lang.Long getBargepodid() {
		return bargepodid;
	}

	public void setBargepodid(java.lang.Long bargepodid) {
		this.bargepodid = bargepodid;
	}

	@Column(name = "cntnos1")
	private java.lang.String cntnos1;
	
	@Column(name = "cntnos2")
	private java.lang.String cntnos2;
	
	@Column(name = "cntnos3")
	private java.lang.String cntnos3;
	
	@Column(name = "cntnos4")
	private java.lang.String cntnos4;
	
	@Column(name = "cntnos5")
	private java.lang.String cntnos5;
	
	@Column(name = "cntnos6")
	private java.lang.String cntnos6;
	
	@Column(name = "cntnos7")
	private java.lang.String cntnos7;
	
	@Column(name = "cntnos8")
	private java.lang.String cntnos8;
	
	@Column(name = "cntnos9")
	private java.lang.String cntnos9;
	
	@Column(name = "cntnos10")
	private java.lang.String cntnos10;
	
	@Column(name = "cntnos11")
	private java.lang.String cntnos11;
	
	@Column(name = "cntnos12")
	private java.lang.String cntnos12;
	
	@Column(name = "cntnos13")
	private java.lang.String cntnos13;
	
	
	@Column(name = "cntype1")
	private java.lang.String cntype1;
	
	@Column(name = "cntype2")
	private java.lang.String cntype2;
	
	@Column(name = "cntype3")
	private java.lang.String cntype3;
	
	@Column(name = "cntype4")
	private java.lang.String cntype4;
	
	@Column(name = "cntype5")
	private java.lang.String cntype5;
	
	@Column(name = "cntype6")
	private java.lang.String cntype6;
	
	@Column(name = "cntype7")
	private java.lang.String cntype7;
	
	@Column(name = "cntype8")
	private java.lang.String cntype8;
	
	@Column(name = "cntype9")
	private java.lang.String cntype9;
	
	@Column(name = "cntype10")
	private java.lang.String cntype10;
	
	@Column(name = "cntype11")
	private java.lang.String cntype11;
	
	@Column(name = "cntype12")
	private java.lang.String cntype12;
	
	@Column(name = "cntype13")
	private java.lang.String cntype13;
	
	@Column(name = "sealno1")
	private java.lang.String sealno1;
	
	@Column(name = "sealno2")
	private java.lang.String sealno2;
	
	@Column(name = "sealno3")
	private java.lang.String sealno3;
	
	@Column(name = "sealno4")
	private java.lang.String sealno4;
	
	@Column(name = "sealno5")
	private java.lang.String sealno5;
	
	@Column(name = "sealno6")
	private java.lang.String sealno6;
	
	@Column(name = "sealno7")
	private java.lang.String sealno7;
	
	@Column(name = "sealno8")
	private java.lang.String sealno8;
	
	@Column(name = "sealno9")
	private java.lang.String sealno9;
	
	@Column(name = "sealno10")
	private java.lang.String sealno10;
	
	@Column(name = "sealno11")
	private java.lang.String sealno11;
	
	@Column(name = "sealno12")
	private java.lang.String sealno12;
	
	@Column(name = "sealno13")
	private java.lang.String sealno13;
	
	@Column(name = "ata")
	private java.util.Date ata;
	
	@Column(name = "atd")
	private java.util.Date atd;
	
	
	
	@Column(name = "sisubtime")
	private java.util.Date sisubtime;
	
	@Column(name = "vgmsubtime")
	private java.util.Date vgmsubtime;
	
	public java.lang.String getCntdesc() {
		return cntdesc;
	}

	public void setCntdesc(java.lang.String cntdesc) {
		this.cntdesc = cntdesc;
	}

	@Column(name = "cntrettime")
	private java.util.Date cntrettime;
	
	@Column(name = "docreltime")
	private java.util.Date docreltime;
	
	@Column(name = "cntdesc")
	private java.lang.String cntdesc;
	
	
	public java.util.Date getSisubtime() {
		return sisubtime;
	}

	public void setSisubtime(java.util.Date sisubtime) {
		this.sisubtime = sisubtime;
	}

	public java.util.Date getVgmsubtime() {
		return vgmsubtime;
	}

	public void setVgmsubtime(java.util.Date vgmsubtime) {
		this.vgmsubtime = vgmsubtime;
	}

	public java.util.Date getCntrettime() {
		return cntrettime;
	}

	public void setCntrettime(java.util.Date cntrettime) {
		this.cntrettime = cntrettime;
	}

	public java.util.Date getDocreltime() {
		return docreltime;
	}

	public void setDocreltime(java.util.Date docreltime) {
		this.docreltime = docreltime;
	}

	@Column(name = "assignuserid")	//分配操作人
	private java.lang.Long assignuserid;
	
	
	@Column(name = "assigntime")	//分配操作时间
	private java.util.Date assigntime;

	
	public java.lang.Long getAssignuserid() {
		return assignuserid;
	}

	public void setAssignuserid(java.lang.Long assignuserid) {
		this.assignuserid = assignuserid;
	}

	public java.util.Date getAssigntime() {
		return assigntime;
	}

	public void setAssigntime(java.util.Date assigntime) {
		this.assigntime = assigntime;
	}

	public java.lang.String getCntype1() {
		return cntype1;
	}

	public void setCntype1(java.lang.String cntype1) {
		this.cntype1 = cntype1;
	}

	public java.lang.String getCntype2() {
		return cntype2;
	}

	public void setCntype2(java.lang.String cntype2) {
		this.cntype2 = cntype2;
	}

	public java.lang.String getCntype3() {
		return cntype3;
	}

	public void setCntype3(java.lang.String cntype3) {
		this.cntype3 = cntype3;
	}

	public java.lang.String getCntype4() {
		return cntype4;
	}

	public void setCntype4(java.lang.String cntype4) {
		this.cntype4 = cntype4;
	}

	public java.lang.String getCntype5() {
		return cntype5;
	}

	public void setCntype5(java.lang.String cntype5) {
		this.cntype5 = cntype5;
	}

	public java.lang.String getCntype6() {
		return cntype6;
	}

	public void setCntype6(java.lang.String cntype6) {
		this.cntype6 = cntype6;
	}

	public java.lang.String getCntype7() {
		return cntype7;
	}

	public void setCntype7(java.lang.String cntype7) {
		this.cntype7 = cntype7;
	}

	public java.lang.String getCntype8() {
		return cntype8;
	}

	public void setCntype8(java.lang.String cntype8) {
		this.cntype8 = cntype8;
	}

	public java.lang.String getCntype9() {
		return cntype9;
	}

	public void setCntype9(java.lang.String cntype9) {
		this.cntype9 = cntype9;
	}

	public java.lang.String getCntype10() {
		return cntype10;
	}

	public void setCntype10(java.lang.String cntype10) {
		this.cntype10 = cntype10;
	}

	public java.lang.String getCntype11() {
		return cntype11;
	}

	public void setCntype11(java.lang.String cntype11) {
		this.cntype11 = cntype11;
	}

	public java.lang.String getCntype12() {
		return cntype12;
	}

	public void setCntype12(java.lang.String cntype12) {
		this.cntype12 = cntype12;
	}

	public java.lang.String getCntype13() {
		return cntype13;
	}

	public void setCntype13(java.lang.String cntype13) {
		this.cntype13 = cntype13;
	}

	@Column(name = "bargevessel", length = 50)
	private java.lang.String bargevessel;
	
	@Column(name = "sidate", length = 35)
	private java.util.Date sidate;


	@Column(name = "masterbillno")
	private java.lang.String masterbillno;

	@Column(name = "contractnumber")
	private java.lang.String contractnumber;

	@Column(name = "inttrano")
	private java.lang.String inttrano;

	@Column(name = "shippingdivisioncontact")
	private java.lang.String shippingdivisioncontact;

	@Column(name = "servicename")
	private java.lang.String servicename;
	
	/**
	 *@generated
	 */
	@Column(name = "sodate", length = 13)
	private java.util.Date sodate;


	@Column(name = "srctype")
	private java.lang.String srctype;


	@Column(name = "emailtostr")
	private java.lang.String emailtostr;


	public String getEmailtostr() {
		return emailtostr;
	}

	public void setEmailtostr(String emailtostr) {
		this.emailtostr = emailtostr;
	}

	public String getSrctype() {
		return srctype;
	}

	public void setSrctype(String srctype) {
		this.srctype = srctype;
	}

	public String getSalesStateDesc() {
		String statedesc = "";
		if (this.getSalesid() != null) {
			statedesc = "已分配";
		}else {
			statedesc = "未分配";
		}
		return statedesc;
	}

	/**
	 *
	 *@generated
	 */
	public java.util.Date getSodate() {
		return this.sodate;
	}

	/**
	 *@generated
	 */
	public void setSodate(java.util.Date value) {
		this.sodate = value;
	}

	public String getMasterbillno() {
		return masterbillno;
	}

	public void setMasterbillno(String masterbillno) {
		this.masterbillno = masterbillno;
	}

	public String getContractnumber() {
		return contractnumber;
	}

	public void setContractnumber(String contractnumber) {
		this.contractnumber = contractnumber;
	}

	public String getInttrano() {
		return inttrano;
	}

	public void setInttrano(String inttrano) {
		this.inttrano = inttrano;
	}

	public String getShippingdivisioncontact() {
		return shippingdivisioncontact;
	}

	public void setShippingdivisioncontact(String shippingdivisioncontact) {
		this.shippingdivisioncontact = shippingdivisioncontact;
	}

	public String getServicename() {
		return servicename;
	}

	public void setServicename(String servicename) {
		this.servicename = servicename;
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
	public java.lang.Long getCarrierid() {
		return this.carrierid;
	}

	/**
	 *@generated
	 */
	public void setCarrierid(java.lang.Long value) {
		this.carrierid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getVessel() {
		return this.vessel;
	}

	/**
	 *@generated
	 */
	public void setVessel(java.lang.String value) {
		this.vessel = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getVoyage() {
		return this.voyage;
	}

	/**
	 *@generated
	 */
	public void setVoyage(java.lang.String value) {
		this.voyage = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getPolid() {
		return this.polid;
	}

	/**
	 *@generated
	 */
	public void setPolid(java.lang.Long value) {
		this.polid = value;
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
	public java.lang.Long getPodid() {
		return this.podid;
	}

	/**
	 *@generated
	 */
	public void setPodid(java.lang.Long value) {
		this.podid = value;
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
	
	public java.lang.Long getPddid() {
		return pddid;
	}

	public void setPddid(java.lang.Long pddid) {
		this.pddid = pddid;
	}

	public java.lang.String getPdd() {
		return pdd;
	}

	public void setPdd(java.lang.String pdd) {
		this.pdd = pdd;
	}

	/**
	 *@generated
	 */
	public java.lang.Integer getScheduleYear() {
		return this.scheduleYear;
	}

	/**
	 *@generated
	 */
	public void setScheduleYear(java.lang.Integer value) {
		this.scheduleYear = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Integer getScheduleMonth() {
		return this.scheduleMonth;
	}

	/**
	 *@generated
	 */
	public void setScheduleMonth(java.lang.Integer value) {
		this.scheduleMonth = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getCls() {
		return this.cls;
	}

	/**
	 *@generated
	 */
	public void setCls(java.util.Date value) {
		this.cls = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getEtd() {
		return this.etd;
	}

	/**
	 *@generated
	 */
	public void setEtd(java.util.Date value) {
		this.etd = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getEta() {
		return this.eta;
	}

	/**
	 *@generated
	 */
	public void setEta(java.util.Date value) {
		this.eta = value;
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

	/**
	 *@generated
	 */
	public java.lang.Long getScheduleid() {
		return this.scheduleid;
	}

	/**
	 *@generated
	 */
	public void setScheduleid(java.lang.Long value) {
		this.scheduleid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Integer getScheduleWeek() {
		return this.scheduleWeek;
	}

	/**
	 *@generated
	 */
	public void setScheduleWeek(java.lang.Integer value) {
		this.scheduleWeek = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getOrderid() {
		return this.orderid;
	}

	/**
	 *@generated
	 */
	public void setOrderid(java.lang.Long value) {
		this.orderid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getOrderno() {
		return this.orderno;
	}

	/**
	 *@generated
	 */
	public void setOrderno(java.lang.String value) {
		this.orderno = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getRemarks2() {
		return this.remarks2;
	}

	/**
	 *@generated
	 */
	public void setRemarks2(java.lang.String value) {
		this.remarks2 = value;
	}
	/**
	 *@generated
	 */
	public java.lang.String getRemarks_cancel() {
		return this.remarks_cancel;
	}

	/**
	 *@generated
	 */
	public void setRemarks_cancel(java.lang.String value) {
		this.remarks_cancel = value;
	}
	/**
	 *@generated
	 */
	public java.lang.String getBarge() {
		return this.barge;
	}

	/**
	 *@generated
	 */
	public void setBarge(java.lang.String value) {
		this.barge = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getBargedatefm() {
		return this.bargedatefm;
	}

	/**
	 *@generated
	 */
	public void setBargedatefm(java.util.Date value) {
		this.bargedatefm = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getBargedateto() {
		return this.bargedateto;
	}

	/**
	 *@generated
	 */
	public void setBargedateto(java.util.Date value) {
		this.bargedateto = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getFeetype() {
		return this.feetype;
	}

	/**
	 *@generated
	 */
	public void setFeetype(java.lang.String value) {
		this.feetype = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getNacaccount() {
		return this.nacaccount;
	}

	/**
	 *@generated
	 */
	public void setNacaccount(java.lang.String value) {
		this.nacaccount = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getPpcctype() {
		return this.ppcctype;
	}

	/**
	 *@generated
	 */
	public void setPpcctype(java.lang.String value) {
		this.ppcctype = value;
	}
	/**
	 *@generated
	 */
	public java.lang.String getBookstate() {
		return this.bookstate;
	}

	/**
	 *@generated
	 */
	public void setBookstate(java.lang.String value) {
		this.bookstate = value;
	}
	/**
	 *@generated
	 */
	public java.lang.String getCcnos() {
		return this.ccnos;
	}

	/**
	 *@generated
	 */
	public void setCcnos(java.lang.String value) {
		this.ccnos = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCcname() {
		return this.ccname;
	}

	/**
	 *@generated
	 */
	public void setCcname(java.lang.String value) {
		this.ccname = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getLine() {
		return this.line;
	}

	/**
	 *@generated
	 */
	public void setLine(java.lang.String value) {
		this.line = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getClsbig() {
		return this.clsbig;
	}

	/**
	 *@generated
	 */
	public void setClsbig(java.util.Date value) {
		this.clsbig = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getUserprice() {
		return StrUtils.isNull(this.userprice)?"":this.userprice;
	}

	/**
	 *@generated
	 */
	public void setUserprice(java.lang.String value) {
		this.userprice = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getUserbook() {
		return StrUtils.isNull(this.userbook)?"":this.userbook;
	}

	/**
	 *@generated
	 */
	public void setUserbook(java.lang.String value) {
		this.userbook = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getCy() {
		return this.cy;
	}

	/**
	 *@generated
	 */
	public void setCy(java.util.Date value) {
		this.cy = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getCv() {
		return this.cv;
	}

	/**
	 *@generated
	 */
	public void setCv(java.util.Date value) {
		this.cv = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getVgm() {
		return this.vgm;
	}

	/**
	 *@generated
	 */
	public void setVgm(java.util.Date value) {
		this.vgm = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getSono() {
		return this.sono;
	}

	/**
	 *@generated
	 */
	public void setSono(java.lang.String value) {
		this.sono = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getGoodsnamec() {
		return this.goodsnamec;
	}

	/**
	 *@generated
	 */
	public void setGoodsnamec(java.lang.String value) {
		this.goodsnamec = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getGoodsnamee() {
		return this.goodsnamee;
	}

	/**
	 *@generated
	 */
	public void setGoodsnamee(java.lang.String value) {
		this.goodsnamee = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece1() {
		return this.piece1;
	}

	/**
	 *@generated
	 */
	public void setPiece1(java.lang.Short value) {
		this.piece1 = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGrswgt1() {
		return this.grswgt1;
	}

	/**
	 *@generated
	 */
	public void setGrswgt1(java.math.BigDecimal value) {
		this.grswgt1 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece2() {
		return this.piece2;
	}

	/**
	 *@generated
	 */
	public void setPiece2(java.lang.Short value) {
		this.piece2 = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGrswgt2() {
		return this.grswgt2;
	}

	/**
	 *@generated
	 */
	public void setGrswgt2(java.math.BigDecimal value) {
		this.grswgt2 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece3() {
		return this.piece3;
	}

	/**
	 *@generated
	 */
	public void setPiece3(java.lang.Short value) {
		this.piece3 = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGrswgt3() {
		return this.grswgt3;
	}

	/**
	 *@generated
	 */
	public void setGrswgt3(java.math.BigDecimal value) {
		this.grswgt3 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece4() {
		return this.piece4;
	}

	/**
	 *@generated
	 */
	public void setPiece4(java.lang.Short value) {
		this.piece4 = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGrswgt4() {
		return this.grswgt4;
	}

	/**
	 *@generated
	 */
	public void setGrswgt4(java.math.BigDecimal value) {
		this.grswgt4 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece5() {
		return this.piece5;
	}

	/**
	 *@generated
	 */
	public void setPiece5(java.lang.Short value) {
		this.piece5 = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGrswgt5() {
		return this.grswgt5;
	}

	/**
	 *@generated
	 */
	public void setGrswgt5(java.math.BigDecimal value) {
		this.grswgt5 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece6() {
		return this.piece6;
	}

	/**
	 *@generated
	 */
	public void setPiece6(java.lang.Short value) {
		this.piece6 = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGrswgt6() {
		return this.grswgt6;
	}

	/**
	 *@generated
	 */
	public void setGrswgt6(java.math.BigDecimal value) {
		this.grswgt6 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece7() {
		return this.piece7;
	}

	/**
	 *@generated
	 */
	public void setPiece7(java.lang.Short value) {
		this.piece7 = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGrswgt7() {
		return this.grswgt7;
	}

	/**
	 *@generated
	 */
	public void setGrswgt7(java.math.BigDecimal value) {
		this.grswgt7 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece8() {
		return this.piece8;
	}

	/**
	 *@generated
	 */
	public void setPiece8(java.lang.Short value) {
		this.piece8 = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGrswgt8() {
		return this.grswgt8;
	}

	/**
	 *@generated
	 */
	public void setGrswgt8(java.math.BigDecimal value) {
		this.grswgt8 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece9() {
		return this.piece9;
	}

	/**
	 *@generated
	 */
	public void setPiece9(java.lang.Short value) {
		this.piece9 = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGrswgt9() {
		return this.grswgt9;
	}

	/**
	 *@generated
	 */
	public void setGrswgt9(java.math.BigDecimal value) {
		this.grswgt9 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece10() {
		return this.piece10;
	}

	/**
	 *@generated
	 */
	public void setPiece10(java.lang.Short value) {
		this.piece10 = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGrswgt10() {
		return this.grswgt10;
	}

	/**
	 *@generated
	 */
	public void setGrswgt10(java.math.BigDecimal value) {
		this.grswgt10 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece11() {
		return this.piece11;
	}

	/**
	 *@generated
	 */
	public void setPiece11(java.lang.Short value) {
		this.piece11 = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGrswgt11() {
		return this.grswgt11;
	}

	/**
	 *@generated
	 */
	public void setGrswgt11(java.math.BigDecimal value) {
		this.grswgt11 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getPiece12() {
		return this.piece12;
	}

	/**
	 *@generated
	 */
	public void setPiece12(java.lang.Short value) {
		this.piece12 = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGrswgt12() {
		return this.grswgt12;
	}

	/**
	 *@generated
	 */
	public void setGrswgt12(java.math.BigDecimal value) {
		this.grswgt12 = value;
	}

	public java.lang.Boolean getIslast() {
		return islast;
	}

	public void setIslast(java.lang.Boolean islast) {
		this.islast = islast;
	}

	public java.lang.Short getPiece13() {
		return piece13;
	}

	public void setPiece13(java.lang.Short piece13) {
		this.piece13 = piece13;
	}

	public java.math.BigDecimal getGrswgt13() {
		return grswgt13;
	}

	public void setGrswgt13(java.math.BigDecimal grswgt13) {
		this.grswgt13 = grswgt13;
	}

	public java.lang.String getBargepol() {
		return bargepol;
	}

	public void setBargepol(java.lang.String bargepol) {
		this.bargepol = bargepol;
	}

	public java.lang.Long getBargepolid() {
		return bargepolid;
	}

	public void setBargepolid(java.lang.Long bargepolid) {
		this.bargepolid = bargepolid;
	}

	public java.lang.String getCntnos1() {
		return cntnos1;
	}

	public void setCntnos1(java.lang.String cntnos1) {
		this.cntnos1 = cntnos1;
	}

	public java.lang.String getCntnos2() {
		return cntnos2;
	}

	public void setCntnos2(java.lang.String cntnos2) {
		this.cntnos2 = cntnos2;
	}

	public java.lang.String getCntnos3() {
		return cntnos3;
	}

	public void setCntnos3(java.lang.String cntnos3) {
		this.cntnos3 = cntnos3;
	}

	public java.lang.String getCntnos4() {
		return cntnos4;
	}

	public void setCntnos4(java.lang.String cntnos4) {
		this.cntnos4 = cntnos4;
	}

	public java.lang.String getCntnos5() {
		return cntnos5;
	}

	public void setCntnos5(java.lang.String cntnos5) {
		this.cntnos5 = cntnos5;
	}

	public java.lang.String getCntnos6() {
		return cntnos6;
	}

	public void setCntnos6(java.lang.String cntnos6) {
		this.cntnos6 = cntnos6;
	}

	public java.lang.String getCntnos7() {
		return cntnos7;
	}

	public void setCntnos7(java.lang.String cntnos7) {
		this.cntnos7 = cntnos7;
	}

	public java.lang.String getCntnos8() {
		return cntnos8;
	}

	public void setCntnos8(java.lang.String cntnos8) {
		this.cntnos8 = cntnos8;
	}

	public java.lang.String getCntnos9() {
		return cntnos9;
	}

	public void setCntnos9(java.lang.String cntnos9) {
		this.cntnos9 = cntnos9;
	}

	public java.lang.String getCntnos10() {
		return cntnos10;
	}

	public void setCntnos10(java.lang.String cntnos10) {
		this.cntnos10 = cntnos10;
	}

	public java.lang.String getCntnos11() {
		return cntnos11;
	}

	public void setCntnos11(java.lang.String cntnos11) {
		this.cntnos11 = cntnos11;
	}

	public java.lang.String getCntnos12() {
		return cntnos12;
	}

	public void setCntnos12(java.lang.String cntnos12) {
		this.cntnos12 = cntnos12;
	}

	public java.lang.String getCntnos13() {
		return cntnos13;
	}

	public void setCntnos13(java.lang.String cntnos13) {
		this.cntnos13 = cntnos13;
	}

	public java.lang.String getBargevessel() {
		return bargevessel;
	}

	public void setBargevessel(java.lang.String bargevessel) {
		this.bargevessel = bargevessel;
	}

	public java.util.Date getSidate() {
		return sidate;
	}

	public void setSidate(java.util.Date sidate) {
		this.sidate = sidate;
	}

	public java.lang.Long getSalesid() {
		return salesid;
	}

	public void setSalesid(java.lang.Long salesid) {
		this.salesid = salesid;
	}

	public java.lang.String getSealno1() {
		return sealno1;
	}

	public void setSealno1(java.lang.String sealno1) {
		this.sealno1 = sealno1;
	}

	public java.lang.String getSealno2() {
		return sealno2;
	}

	public void setSealno2(java.lang.String sealno2) {
		this.sealno2 = sealno2;
	}

	public java.lang.String getSealno3() {
		return sealno3;
	}

	public void setSealno3(java.lang.String sealno3) {
		this.sealno3 = sealno3;
	}

	public java.lang.String getSealno4() {
		return sealno4;
	}

	public void setSealno4(java.lang.String sealno4) {
		this.sealno4 = sealno4;
	}

	public java.lang.String getSealno5() {
		return sealno5;
	}

	public void setSealno5(java.lang.String sealno5) {
		this.sealno5 = sealno5;
	}

	public java.lang.String getSealno6() {
		return sealno6;
	}

	public void setSealno6(java.lang.String sealno6) {
		this.sealno6 = sealno6;
	}

	public java.lang.String getSealno7() {
		return sealno7;
	}

	public void setSealno7(java.lang.String sealno7) {
		this.sealno7 = sealno7;
	}

	public java.lang.String getSealno8() {
		return sealno8;
	}

	public void setSealno8(java.lang.String sealno8) {
		this.sealno8 = sealno8;
	}

	public java.lang.String getSealno9() {
		return sealno9;
	}

	public void setSealno9(java.lang.String sealno9) {
		this.sealno9 = sealno9;
	}

	public java.lang.String getSealno10() {
		return sealno10;
	}

	public void setSealno10(java.lang.String sealno10) {
		this.sealno10 = sealno10;
	}

	public java.lang.String getSealno11() {
		return sealno11;
	}

	public void setSealno11(java.lang.String sealno11) {
		this.sealno11 = sealno11;
	}

	public java.lang.String getSealno12() {
		return sealno12;
	}

	public void setSealno12(java.lang.String sealno12) {
		this.sealno12 = sealno12;
	}

	public java.lang.String getSealno13() {
		return sealno13;
	}

	public void setSealno13(java.lang.String sealno13) {
		this.sealno13 = sealno13;
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

	public Long getJobid() {
		return jobid;
	}

	public void setJobid(Long jobid) {
		this.jobid = jobid;
	}

	@Override
	public String toString() {
		return "BusShipBooking{" +
				"id=" + id +
				", nos='" + nos + '\'' +
				", carrierid=" + carrierid +
				", salesid=" + salesid +
				", jobid=" + jobid +
				", vessel='" + vessel + '\'' +
				", voyage='" + voyage + '\'' +
				", polid=" + polid +
				", pol='" + pol + '\'' +
				", podid=" + podid +
				", pod='" + pod + '\'' +
				", pddid=" + pddid +
				", pdd='" + pdd + '\'' +
				", scheduleYear=" + scheduleYear +
				", scheduleMonth=" + scheduleMonth +
				", cls=" + cls +
				", etd=" + etd +
				", eta=" + eta +
				", remarks='" + remarks + '\'' +
				", corpid=" + corpid +
				", isdelete=" + isdelete +
				", inputer='" + inputer + '\'' +
				", inputtime=" + inputtime +
				", updater='" + updater + '\'' +
				", updatetime=" + updatetime +
				", scheduleid=" + scheduleid +
				", scheduleWeek=" + scheduleWeek +
				", orderid=" + orderid +
				", orderno='" + orderno + '\'' +
				", remarks2='" + remarks2 + '\'' +
				", barge='" + barge + '\'' +
				", bargedatefm=" + bargedatefm +
				", bargedateto=" + bargedateto +
				", feetype='" + feetype + '\'' +
				", nacaccount='" + nacaccount + '\'' +
				", ppcctype='" + ppcctype + '\'' +
				", bookstate='" + bookstate + '\'' +
				", ccnos='" + ccnos + '\'' +
				", ccname='" + ccname + '\'' +
				", line='" + line + '\'' +
				", clsbig=" + clsbig +
				", userprice='" + userprice + '\'' +
				", userbook='" + userbook + '\'' +
				", cy=" + cy +
				", cv=" + cv +
				", vgm=" + vgm +
				", sono='" + sono + '\'' +
				", goodsnamec='" + goodsnamec + '\'' +
				", goodsnamee='" + goodsnamee + '\'' +
				", piece1=" + piece1 +
				", grswgt1=" + grswgt1 +
				", piece2=" + piece2 +
				", grswgt2=" + grswgt2 +
				", piece3=" + piece3 +
				", grswgt3=" + grswgt3 +
				", piece4=" + piece4 +
				", grswgt4=" + grswgt4 +
				", piece5=" + piece5 +
				", grswgt5=" + grswgt5 +
				", piece6=" + piece6 +
				", grswgt6=" + grswgt6 +
				", piece7=" + piece7 +
				", grswgt7=" + grswgt7 +
				", piece8=" + piece8 +
				", grswgt8=" + grswgt8 +
				", piece9=" + piece9 +
				", grswgt9=" + grswgt9 +
				", piece10=" + piece10 +
				", grswgt10=" + grswgt10 +
				", piece11=" + piece11 +
				", grswgt11=" + grswgt11 +
				", piece12=" + piece12 +
				", grswgt12=" + grswgt12 +
				", piece13=" + piece13 +
				", grswgt13=" + grswgt13 +
				", remarks_cancel='" + remarks_cancel + '\'' +
				", islast=" + islast +
				", bargepol='" + bargepol + '\'' +
				", bargepolid=" + bargepolid +
				", cntnos1='" + cntnos1 + '\'' +
				", cntnos2='" + cntnos2 + '\'' +
				", cntnos3='" + cntnos3 + '\'' +
				", cntnos4='" + cntnos4 + '\'' +
				", cntnos5='" + cntnos5 + '\'' +
				", cntnos6='" + cntnos6 + '\'' +
				", cntnos7='" + cntnos7 + '\'' +
				", cntnos8='" + cntnos8 + '\'' +
				", cntnos9='" + cntnos9 + '\'' +
				", cntnos10='" + cntnos10 + '\'' +
				", cntnos11='" + cntnos11 + '\'' +
				", cntnos12='" + cntnos12 + '\'' +
				", cntnos13='" + cntnos13 + '\'' +
				", cntype1='" + cntype1 + '\'' +
				", cntype2='" + cntype2 + '\'' +
				", cntype3='" + cntype3 + '\'' +
				", cntype4='" + cntype4 + '\'' +
				", cntype5='" + cntype5 + '\'' +
				", cntype6='" + cntype6 + '\'' +
				", cntype7='" + cntype7 + '\'' +
				", cntype8='" + cntype8 + '\'' +
				", cntype9='" + cntype9 + '\'' +
				", cntype10='" + cntype10 + '\'' +
				", cntype11='" + cntype11 + '\'' +
				", cntype12='" + cntype12 + '\'' +
				", cntype13='" + cntype13 + '\'' +
				", sealno1='" + sealno1 + '\'' +
				", sealno2='" + sealno2 + '\'' +
				", sealno3='" + sealno3 + '\'' +
				", sealno4='" + sealno4 + '\'' +
				", sealno5='" + sealno5 + '\'' +
				", sealno6='" + sealno6 + '\'' +
				", sealno7='" + sealno7 + '\'' +
				", sealno8='" + sealno8 + '\'' +
				", sealno9='" + sealno9 + '\'' +
				", sealno10='" + sealno10 + '\'' +
				", sealno11='" + sealno11 + '\'' +
				", sealno12='" + sealno12 + '\'' +
				", sealno13='" + sealno13 + '\'' +
				", ata=" + ata +
				", atd=" + atd +
				", assignuserid=" + assignuserid +
				", assigntime=" + assigntime +
				", bargevessel='" + bargevessel + '\'' +
				", sidate=" + sidate +
				", masterbillno='" + masterbillno + '\'' +
				", contractnumber='" + contractnumber + '\'' +
				", inttrano='" + inttrano + '\'' +
				", shippingdivisioncontact='" + shippingdivisioncontact + '\'' +
				", servicename='" + servicename + '\'' +
				", sodate=" + sodate +
				'}';
	}
}