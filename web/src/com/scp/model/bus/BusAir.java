package com.scp.model.bus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "bus_air")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusAir implements java.io.Serializable {

	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;

	@Column(name = "singtime", length = 29)
	private java.util.Date singtime;
	
	@Column(name = "jobid")
	private java.lang.Long jobid;

	@Column(name = "refno", length = 30)
	private java.lang.String refno;
	
	@Column(name = "sono", length = 30)
	private java.lang.String sono;
	
	@Column(name = "hawbno", length = 30)
	private java.lang.String hawbno;
	
	@Column(name = "mawbno", length = 30)
	private java.lang.String mawbno;
	
	@Column(name = "polid")
	private java.lang.Long polid;
	
	@Column(name = "pol", length = 50)
	private java.lang.String pol;
	
	@Column(name = "polcode", length = 10)
	private java.lang.String polcode;
	
	@Column(name = "podid")
	private java.lang.Long podid;
	
	@Column(name = "pod", length = 50)
	private java.lang.String pod;
	
	@Column(name = "podcode", length = 10)
	private java.lang.String podcode;
	
	@Column(name = "destination")
	private java.lang.String destination;
	
	@Column(name = "carrierid")
	private java.lang.Long carrierid;
	
	@Column(name = "ppccgoods", length = 30)
	private java.lang.String ppccgoods;
	
	@Column(name = "cneeid")
	private java.lang.Long cneeid;
	
	@Column(name = "cneename")
	private java.lang.String cneename;
	
	@Column(name = "cneetitle")
	private java.lang.String cneetitle;
	
	@Column(name = "cnorid")
	private java.lang.Long cnorid;
	
	@Column(name = "cnorname")
	private java.lang.String cnorname;
	
	@Column(name = "cnortitle")
	private java.lang.String cnortitle;
	
	@Column(name = "busstatus")
	private java.lang.String busstatus;
	
	@Column(name = "notifyid")
	private java.lang.Long notifyid;
    
	@Column(name = "notifyname")
	private java.lang.String notifyname;
	
	@Column(name = "notifytitle")
	private java.lang.String notifytitle;
	
	@Column(name = "notify2id")
	private java.lang.Long notify2id;
	
	@Column(name = "notify2name")
	private java.lang.String notify2name;
	
	@Column(name = "notify2title")
	private java.lang.String notify2title;
    
	@Column(name = "mblcneeid")
	private java.lang.Long mblcneeid;
	
	@Column(name = "mblcneename")
	private java.lang.String mblcneename;
	
	@Column(name = "mblcneetitle")
	private java.lang.String mblcneetitle;
	
	@Column(name = "mblcnorid")
	private java.lang.Long mblcnorid;
	
	@Column(name = "mblcnorname")
	private java.lang.String mblcnorname;

	@Column(name = "mblcnortitle")
	private java.lang.String mblcnortitle;

	@Column(name = "mblnotifyid")
	private java.lang.Long mblnotifyid;
	
	@Column(name = "mblnotifyname")
	private java.lang.String mblnotifyname;
	
	@Column(name = "mblnotifytitle")
	private java.lang.String mblnotifytitle;
	
	@Column(name = "piece")
	private java.lang.String piece;
	
	@Column(name = "piece2")
	private java.lang.Integer piece2;
	
	@Column(name = "weight")
	private java.lang.String weight;
	
	@Column(name = "weight2")
	private java.math.BigDecimal weight2;

	@Column(name = "unit")
	private java.lang.String unit;

	@Column(name = "rateclass")
	private java.lang.String rateclass;
	
	@Column(name = "markno")
	private java.lang.String markno;
	
	@Column(name = "chargeweight")
	private java.lang.String chargeweight;
	
	@Column(name = "chargeweight2")
	private java.math.BigDecimal chargeweight2;
	
	@Column(name = "charge")
	private java.lang.String charge;
	
	@Column(name = "amount")
	private java.lang.String amount;
	
	@Column(name = "goodsdesc")
	private java.lang.String goodsdesc;
	
	@Column(name = "corpid")
	private java.lang.Long corpid;
	
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	@Column(name = "inputer" , length = 100)
	private java.lang.String inputer;
	
	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;

	@Column(name = "updater", length = 100)
	private java.lang.String updater;

	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;
	
	@Column(name = "flightno1")
	private java.lang.String flightno1;
	
	@Column(name = "flightdate1 ", length = 29)
	private java.util.Date flightdate1;
	
	@Column(name = "flightno2")
	private java.lang.String flightno2;
	
	@Column(name = "flightdate2 ", length = 29)
	private java.util.Date flightdate2;

	@Column(name = "ishold")
	private boolean ishold;
	
	@Column(name = "hoder", length = 30)
	private String holder;
	
	@Column(name = "holdtime")
	private java.util.Date holdtime;
	
	@Column(name = "holddesc")
	private String holddesc;

	@Column(name = "isput")
	private boolean isput;
	
	@Column(name = "puter", length = 30)
	private String puter;
	
	@Column(name = "puttime")
	private java.util.Date puttime;
	
	@Column(name = "putdesc")
	private String putdesc;
	
	@Column(name = "eta")
	private java.util.Date eta;
	
	@Column(name = "custype", length = 1)
	private java.lang.String custype;
	
	@Column(name = "agentid")
	private java.lang.Long agentid;
	
	@Column(name = "agentdesid ")
	private java.lang.Long agentdesid;
	
	@Column(name = "agentdesabbr")
	private String agentdesabbr;
	
	@Column(name = "agentdescode")
	private String agentdescode;
	
	@Column(name = "goodsname")
	private String goodsname;
	
	@Column(name = "volume")
	private String volume;
	
	@Column(name = "volume2")
	private java.math.BigDecimal volume2;
	
	@Column(name = "volumeweight")
	private String volumeweight;
	
	@Column(name = "customid")
	private java.lang.Long customid;	
	
	@Column(name = "arrwmstime")
	private java.util.Date arrwmstime;
	
	@Column(name = "ata")
	private java.util.Date ata;
	
	@Column(name = "putstatus" , updatable = false)
	private String putstatus;
	
	@Column(name = "airfreightpp", length = 30)
	private String airfreightpp;
	
	@Column(name = "airfreightcc", length = 30)
	private String airfreightcc;
	
	@Column(name = "ppccothfee", length = 30)
	private String ppccothfee;
	
	@Column(name = "valueaddpp", length = 30)
	private String valueaddpp;
	
	@Column(name = "ppccpaytype", length = 30)
	private String ppccpaytype;
	
	@Column(name = "to1", length = 30)
	private String to1;

	@Column(name = "to2", length = 30)
	private String to2;
	
	@Column(name = "to3", length = 30)
	private String to3;
	
	@Column(name = "by1", length = 30)
	private String by1;
	
	@Column(name = "by2", length = 30)
	private String by2;
	
	@Column(name = "by3", length = 30)
	private String by3;
	
	@Column(name = "other1")
	private String other1;
	
	@Column(name = "other2")
	private String other2;
	
	@Column(name = "other3")
	private String other3;
	
	public String getInsurancepp() {
		return insurancepp;
	}

	public void setInsurancepp(String insurancepp) {
		this.insurancepp = insurancepp;
	}

	@Column(name = "taxfeepp", length = 30)
	private String taxfeepp;
	
	@Column(name = "taxfeecc", length = 30)
	private String taxfeecc;
	
	@Column(name = "agentothfeepp", length = 30)
	private String agentothfeepp;
	
	@Column(name = "agentothfeecc", length = 30)
	private String agentothfeecc;
	
	@Column(name = "carrothfeepp", length = 30)
	private String carrothfeepp;
	
	@Column(name = "carrothfeecc", length = 30)
	private String carrothfeecc;
	
	@Column(name = "otherfeepp", length = 30)
	private String otherfeepp;
	
	@Column(name = "otherfeecc", length = 30)
	private String otherfeecc;
	
	@Column(name = "feesumpp", length = 30)
	private String feesumpp;
	
	@Column(name = "feesumcc", length = 30)
	private String feesumcc;
	
	@Column(name = "polcyid", length = 30)
	private String polcyid;
	
	@Column(name = "transamt", length = 30)
	private String transamt;
	
	@Column(name = "podcyid", length = 30)
	private String podcyid;
	
	@Column(name = "podxrate", length = 30)
	private String podxrate;
	
	@Column(name = "podfee", length = 30)
	private String podfee;
	
	@Column(name = "ccsumto", length = 30)
	private String ccsumto;
	
	@Column(name = "sumto", length = 30)
	private String sumto;
	
	@Column(name = "insuranceamt", length = 30)
	private String insuranceamt;
	
	@Column(name = "customeamt", length = 30)
	private String customeamt;
	
	@Column(name = "insurancepp", length = 30)
	private String insurancepp;
	
	@Column(name = "insurancecc", length = 30)
	private String insurancecc;
	
	@Column(name = "valueaddcc", length = 30)
	private String valueaddcc;
	
	@Column(name = "iscommongoods")
	private java.lang.Boolean iscommongoods;
	
	@Column(name = "ismagnetic")
	private java.lang.Boolean ismagnetic;
	
	@Column(name = "iswithbattery")
	private java.lang.Boolean iswithbattery;
	
	@Column(name = "isequipment")
	private java.lang.Boolean isequipment;
	
	@Column(name = "isgas")
	private java.lang.Boolean isgas;
	
	@Column(name = "ispowder")
	private java.lang.Boolean ispowder;
	
	@Column(name = "isparticles")
	private java.lang.Boolean isparticles;
	
	@Column(name = "isliquid")
	private java.lang.Boolean isliquid;
	
	@Column(name = "ispaste")
	private java.lang.Boolean ispaste;
	
	@Column(name = "isphotograph")
	private java.lang.Boolean isphotograph;
	
	@Column(name = "iscertificateoforigin")
	private java.lang.Boolean iscertificateoforigin;
	
	@Column(name = "isfumigationcertificate")
	private java.lang.Boolean isfumigationcertificate;
	
	@Column(name = "ispallet")
	private java.lang.Boolean ispallet;
	
	@Column(name = "isinvoiceendorsement")
	private java.lang.Boolean isinvoiceendorsement;
	
	@Column(name = "isdetect")
	private java.lang.Boolean isdetect;
	
	@Column(name = "isletterofcredit")
	private java.lang.Boolean isletterofcredit;
	
	@Column(name = "islabel")
	private java.lang.Boolean islabel;
	
	@Column(name = "isbuyinsurance")
	private java.lang.Boolean isbuyinsurance;
	
	@Column(name = "flightno3")
	private java.lang.String flightno3;
	@Column(name = "flightdate3 ", length = 29)
	private java.util.Date flightdate3;
	@Column(name = "deliverytype")
	private java.lang.String deliverytype;
	@Column(name = "towcompanyid")
	private java.lang.Long towcompanyid;
	@Column(name = "deliverytime")
	private java.util.Date deliverytime;
	@Column(name = "schedulflight")
	private java.util.Date schedulflight;
	
	
	
	@Column(name = "customsclearanceway")
	private java.lang.String customsclearanceway;
	@Column(name = "customsclearancetype")
	private java.lang.String customsclearancetype;
	@Column(name = "ordertype")
	private java.lang.String ordertype;
	
	@Column(name = "piece3")
	private java.lang.Integer piece3;
	@Column(name = "weight3")
	private java.math.BigDecimal weight3;
	@Column(name = "chargeweight3")
	private java.math.BigDecimal chargeweight3;
	@Column(name = "volume3")
	private java.math.BigDecimal volume3;
	
	@Column(name = "piece4")
	private java.lang.Integer piece4;
	@Column(name = "weight4")
	private java.math.BigDecimal weight4;
	@Column(name = "chargeweight4")
	private java.math.BigDecimal chargeweight4;
	@Column(name = "volume4")
	private java.math.BigDecimal volume4;
	
	
	@Column(name = "arrivalpiece1")
	private java.lang.String arrivalpiece1;
	@Column(name = "trucknumber1")
	private java.lang.String trucknumber1;
	@Column(name = "driverphone1")
	private java.lang.String driverphone1;
	@Column(name = "arrivalpiece2")
	private java.lang.String arrivalpiece2;
	@Column(name = "trucknumber2")
	private java.lang.String trucknumber2;
	@Column(name = "driverphone2")
	private java.lang.String driverphone2;
	@Column(name = "arrivalpiece3")
	private java.lang.String arrivalpiece3;
	@Column(name = "trucknumber3")
	private java.lang.String trucknumber3;
	@Column(name = "driverphone3")
	private java.lang.String driverphone3;
	
	@Column(name = "info")
	private java.lang.String info;
	@Column(name = "remarks")
	private java.lang.String remarks;
	@Column(name = "arrivaltime ", length = 29)
	private java.util.Date arrivaltime;
	@Column(name = "receiver")
	private java.lang.String receiver;
	@Column(name = "inputer2")
	private java.lang.String inputer2;
	@Column(name = "ordertype2")
	private java.lang.String ordertype2;
	@Column(name = "sizeremarks")
	private java.lang.String sizeremarks;
	
	@Column(name = "volwgt2")
	private java.math.BigDecimal volwgt2;
	@Column(name = "volwgt3")
	private java.math.BigDecimal volwgt3;
	@Column(name = "volwgt4")
	private java.math.BigDecimal volwgt4;
	
	
	@Column(name = "cls", length = 29)
	private java.util.Date cls;
	
	@Column(name = "entrytype")
	private java.lang.String entrytype;
	
	@Column(name = "freightstation")
	private java.lang.String freightstation;
	
	@Column(name = "deliveryplace")
	private java.lang.String deliveryplace;
	
	@Column(name = "customscompid")
	private java.lang.Long customscompid;
	
	
	
	@Column(name = "remarks2")
	private java.lang.String remarks2;
	
	@Column(name = "airline")
	private java.lang.String airline;
	
	@Column(name = "bookingcode")
	private java.lang.String bookingcode;
	
	@Column(name = "remarks3")
	private java.lang.String remarks3;
	
	@Column(name = "polcode2")
	private java.lang.String polcode2;
	
	@Column(name = "transitport1")
	private java.lang.String transitport1;
	
	@Column(name = "transitport2")
	private java.lang.String transitport2;
	
	@Column(name = "podcode2")
	private java.lang.String podcode2;
	
	@Column(name = "flightno4")
	private java.lang.String flightno4;
	@Column(name = "flightdate4", length = 29)
	private java.util.Date flightdate4;
	@Column(name = "flightno5")
	private java.lang.String flightno5;
	@Column(name = "flightdate5", length = 29)
	private java.util.Date flightdate5;
	@Column(name = "flightno6")
	private java.lang.String flightno6;
	@Column(name = "flightdate6", length = 29)
	private java.util.Date flightdate6;
	
	@Column(name = "flightno7")
	private java.lang.String flightno7;
	@Column(name = "flightdate7", length = 29)
	private java.util.Date flightdate7;
	@Column(name = "flightno8")
	private java.lang.String flightno8;
	@Column(name = "flightdate8", length = 29)
	private java.util.Date flightdate8;
	@Column(name = "flightno9")
	private java.lang.String flightno9;
	@Column(name = "flightdate9", length = 29)
	private java.util.Date flightdate9;
	
	@Column(name = "piece5")
	private java.lang.Integer piece5;
	@Column(name = "weight5")
	private java.math.BigDecimal weight5;
	@Column(name = "chargeweight5")
	private java.math.BigDecimal chargeweight5;
	@Column(name = "volume5")
	private java.math.BigDecimal volume5;
	@Column(name = "volwgt5")
	private java.math.BigDecimal volwgt5;
	
	@Column(name = "billno")
	private java.lang.String billno;
	
	@Column(name = "customscontact")
	private java.lang.String customscontact;
	
	@Column(name = "bookingstatus")
	private java.lang.String bookingstatus;
	
	@Column(name = "remarks4")
	private java.lang.String remarks4;
	
	@Column(name = "goodsnamee")
	private java.lang.String goodsnamee;
	
	@Column(name = "other4")
	private java.lang.String other4;
	
	@Column(name = "iswreceipt")
	private java.lang.Boolean iswreceipt;
	
	@Column(name = "iswreceiptoriginal")
	private java.lang.Boolean iswreceiptoriginal;
	
	@Column(name = "ispackinglist")
	private java.lang.Boolean ispackinglist;
	
	@Column(name = "isinvoice")
	private java.lang.Boolean isinvoice;
	
	@Column(name = "iscofo")
	private java.lang.Boolean iscofo;
	
	@Column(name = "other5")
	private java.lang.String other5;
	
	
	public String getValueaddcc() {
		return valueaddcc;
	}

	public void setValueaddcc(String valueaddcc) {
		this.valueaddcc = valueaddcc;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.util.Date getSingtime() {
		return singtime;
	}

	public void setSingtime(java.util.Date singtime) {
		this.singtime = singtime;
	}

	public java.lang.Long getJobid() {
		return jobid;
	}

	public void setJobid(java.lang.Long jobid) {
		this.jobid = jobid;
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

	public java.lang.String getHawbno() {
		return hawbno;
	}

	public void setHawbno(java.lang.String hawbno) {
		this.hawbno = hawbno;
	}

	public java.lang.String getMawbno() {
		return mawbno;
	}

	public void setMawbno(java.lang.String mawbno) {
		this.mawbno = mawbno;
	}

	public java.lang.Long getPolid() {
		return polid;
	}

	public void setPolid(java.lang.Long polid) {
		this.polid = polid;
	}

	public java.lang.String getPol() {
		return pol;
	}

	public void setPol(java.lang.String pol) {
		this.pol = pol;
	}

	public java.lang.Long getPodid() {
		return podid;
	}

	public void setPodid(java.lang.Long podid) {
		this.podid = podid;
	}

	public java.lang.String getPod() {
		return pod;
	}

	public void setPod(java.lang.String pod) {
		this.pod = pod;
	}

	public java.lang.String getDestination() {
		return destination;
	}

	public void setDestination(java.lang.String destination) {
		this.destination = destination;
	}

	public java.lang.Long getCarrierid() {
		return carrierid;
	}

	public void setCarrierid(java.lang.Long carrierid) {
		this.carrierid = carrierid;
	}

	public java.lang.String getPpccgoods() {
		return ppccgoods;
	}

	public void setPpccgoods(java.lang.String ppccgoods) {
		this.ppccgoods = ppccgoods;
	}

	public java.lang.Long getCneeid() {
		return cneeid;
	}

	public void setCneeid(java.lang.Long cneeid) {
		this.cneeid = cneeid;
	}

	public java.lang.String getCneename() {
		return cneename;
	}

	public void setCneename(java.lang.String cneename) {
		this.cneename = cneename;
	}

	public java.lang.String getCneetitle() {
		return cneetitle;
	}

	public void setCneetitle(java.lang.String cneetitle) {
		this.cneetitle = cneetitle;
	}

	public java.lang.Long getCnorid() {
		return cnorid;
	}

	public void setCnorid(java.lang.Long cnorid) {
		this.cnorid = cnorid;
	}

	public java.lang.String getCnorname() {
		return cnorname;
	}

	public void setCnorname(java.lang.String cnorname) {
		this.cnorname = cnorname;
	}

	public java.lang.String getCnortitle() {
		return cnortitle;
	}

	public void setCnortitle(java.lang.String cnortitle) {
		this.cnortitle = cnortitle;
	}

	public java.lang.Long getNotifyid() {
		return notifyid;
	}

	public void setNotifyid(java.lang.Long notifyid) {
		this.notifyid = notifyid;
	}

	public java.lang.String getNotifyname() {
		return notifyname;
	}

	public void setNotifyname(java.lang.String notifyname) {
		this.notifyname = notifyname;
	}

	public java.lang.String getNotifytitle() {
		return notifytitle;
	}

	public void setNotifytitle(java.lang.String notifytitle) {
		this.notifytitle = notifytitle;
	}

	public java.lang.Long getNotify2id() {
		return notify2id;
	}

	public void setNotify2id(java.lang.Long notify2id) {
		this.notify2id = notify2id;
	}

	public java.lang.String getNotify2name() {
		return notify2name;
	}

	public void setNotify2name(java.lang.String notify2name) {
		this.notify2name = notify2name;
	}

	public java.lang.String getNotify2title() {
		return notify2title;
	}

	public void setNotify2title(java.lang.String notify2title) {
		this.notify2title = notify2title;
	}

	public java.lang.Long getMblcneeid() {
		return mblcneeid;
	}

	public void setMblcneeid(java.lang.Long mblcneeid) {
		this.mblcneeid = mblcneeid;
	}

	public java.lang.String getMblcneename() {
		return mblcneename;
	}

	public void setMblcneename(java.lang.String mblcneename) {
		this.mblcneename = mblcneename;
	}

	public java.lang.String getMblcneetitle() {
		return mblcneetitle;
	}

	public void setMblcneetitle(java.lang.String mblcneetitle) {
		this.mblcneetitle = mblcneetitle;
	}

	public java.lang.Long getMblcnorid() {
		return mblcnorid;
	}

	public void setMblcnorid(java.lang.Long mblcnorid) {
		this.mblcnorid = mblcnorid;
	}

	public java.lang.String getMblcnorname() {
		return mblcnorname;
	}

	public void setMblcnorname(java.lang.String mblcnorname) {
		this.mblcnorname = mblcnorname;
	}

	public java.lang.String getMblcnortitle() {
		return mblcnortitle;
	}

	public void setMblcnortitle(java.lang.String mblcnortitle) {
		this.mblcnortitle = mblcnortitle;
	}

	public java.lang.Long getMblnotifyid() {
		return mblnotifyid;
	}

	public void setMblnotifyid(java.lang.Long mblnotifyid) {
		this.mblnotifyid = mblnotifyid;
	}

	public java.lang.String getMblnotifyname() {
		return mblnotifyname;
	}

	public void setMblnotifyname(java.lang.String mblnotifyname) {
		this.mblnotifyname = mblnotifyname;
	}

	public java.lang.String getMblnotifytitle() {
		return mblnotifytitle;
	}

	public void setMblnotifytitle(java.lang.String mblnotifytitle) {
		this.mblnotifytitle = mblnotifytitle;
	}

	public java.lang.String getPiece() {
		return piece;
	}

	public void setPiece(java.lang.String piece) {
		this.piece = piece;
	}

	public java.lang.String getWeight() {
		return weight;
	}

	public void setWeight(java.lang.String weight) {
		this.weight = weight;
	}

	public java.lang.String getUnit() {
		return unit;
	}

	public void setUnit(java.lang.String unit) {
		this.unit = unit;
	}

	public java.lang.String getRateclass() {
		return rateclass;
	}

	public void setRateclass(java.lang.String rateclass) {
		this.rateclass = rateclass;
	}

	public java.lang.String getMarkno() {
		return markno;
	}

	public void setMarkno(java.lang.String markno) {
		this.markno = markno;
	}

	public java.lang.String getChargeweight() {
		return chargeweight;
	}

	public void setChargeweight(java.lang.String chargeweight) {
		this.chargeweight = chargeweight;
	}

	public java.lang.String getCharge() {
		return charge;
	}

	public void setCharge(java.lang.String charge) {
		this.charge = charge;
	}

	public java.lang.String getAmount() {
		return amount;
	}

	public void setAmount(java.lang.String amount) {
		this.amount = amount;
	}

	public java.lang.String getGoodsdesc() {
		return goodsdesc;
	}

	public void setGoodsdesc(java.lang.String goodsdesc) {
		this.goodsdesc = goodsdesc;
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

	public java.lang.String getFlightno1() {
		return flightno1;
	}

	public void setFlightno1(java.lang.String flightno1) {
		this.flightno1 = flightno1;
	}

	public java.util.Date getFlightdate1() {
		return flightdate1;
	}

	public void setFlightdate1(java.util.Date flightdate1) {
		this.flightdate1 = flightdate1;
	}

	public java.lang.String getFlightno2() {
		return flightno2;
	}

	public void setFlightno2(java.lang.String flightno2) {
		this.flightno2 = flightno2;
	}

	public java.util.Date getFlightdate2() {
		return flightdate2;
	}

	public void setFlightdate2(java.util.Date flightdate2) {
		this.flightdate2 = flightdate2;
	}

	public boolean isIshold() {
		return ishold;
	}

	public void setIshold(boolean ishold) {
		this.ishold = ishold;
	}

	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

	public java.util.Date getHoldtime() {
		return holdtime;
	}

	public void setHoldtime(java.util.Date holdtime) {
		this.holdtime = holdtime;
	}

	public String getHolddesc() {
		return holddesc;
	}

	public void setHolddesc(String holddesc) {
		this.holddesc = holddesc;
	}

	public boolean isIsput() {
		return isput;
	}

	public void setIsput(boolean isput) {
		this.isput = isput;
	}

	public String getPuter() {
		return puter;
	}

	public void setPuter(String puter) {
		this.puter = puter;
	}

	public java.util.Date getPuttime() {
		return puttime;
	}

	public void setPuttime(java.util.Date puttime) {
		this.puttime = puttime;
	}

	public String getPutdesc() {
		return putdesc;
	}

	public void setPutdesc(String putdesc) {
		this.putdesc = putdesc;
	}

	public java.util.Date getEta() {
		return eta;
	}

	public void setEta(java.util.Date eta) {
		this.eta = eta;
	}

	public java.lang.String getCustype() {
		return custype;
	}

	public void setCustype(java.lang.String custype) {
		this.custype = custype;
	}

	public java.lang.Long getAgentid() {
		return agentid;
	}

	public void setAgentid(java.lang.Long agentid) {
		this.agentid = agentid;
	}

	public java.lang.Long getAgentdesid() {
		return agentdesid;
	}

	public void setAgentdesid(java.lang.Long agentdesid) {
		this.agentdesid = agentdesid;
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getVolumeweight() {
		return volumeweight;
	}

	public void setVolumeweight(String volumeweight) {
		this.volumeweight = volumeweight;
	}

	public java.lang.Long getCustomid() {
		return customid;
	}

	public void setCustomid(java.lang.Long customid) {
		this.customid = customid;
	}

	public String getAgentdesabbr() {
		return agentdesabbr;
	}

	public void setAgentdesabbr(String agentdesabbr) {
		this.agentdesabbr = agentdesabbr;
	}

	public String getAgentdescode() {
		return agentdescode;
	}

	public void setAgentdescode(String agentdescode) {
		this.agentdescode = agentdescode;
	}

	public java.util.Date getArrwmstime() {
		return arrwmstime;
	}

	public void setArrwmstime(java.util.Date arrwmstime) {
		this.arrwmstime = arrwmstime;
	}

	public java.util.Date getAta() {
		return ata;
	}

	public void setAta(java.util.Date ata) {
		this.ata = ata;
	}

	public String getPutstatus() {
		return putstatus;
	}

	public void setPutstatus(String putstatus) {
		this.putstatus = putstatus;
	}

	public java.lang.Integer getPiece2() {
		return piece2;
	}

	public void setPiece2(java.lang.Integer piece2) {
		this.piece2 = piece2;
	}

	public java.math.BigDecimal getWeight2() {
		return weight2;
	}

	public void setWeight2(java.math.BigDecimal weight2) {
		this.weight2 = weight2;
	}

	public java.math.BigDecimal getChargeweight2() {
		return chargeweight2;
	}

	public void setChargeweight2(java.math.BigDecimal chargeweight2) {
		this.chargeweight2 = chargeweight2;
	}

	public java.math.BigDecimal getVolume2() {
		return volume2;
	}

	public void setVolume2(java.math.BigDecimal volume2) {
		this.volume2 = volume2;
	}

	public String getAirfreightpp() {
		return airfreightpp;
	}

	public void setAirfreightpp(String airfreightpp) {
		this.airfreightpp = airfreightpp;
	}

	public String getAirfreightcc() {
		return airfreightcc;
	}

	public void setAirfreightcc(String airfreightcc) {
		this.airfreightcc = airfreightcc;
	}

	public String getValueaddpp() {
		return valueaddpp;
	}

	public void setValueaddpp(String valueaddpp) {
		this.valueaddpp = valueaddpp;
	}

	public String getInsurancecc() {
		return insurancecc;
	}

	public void setInsurancecc(String insurancecc) {
		this.insurancecc = insurancecc;
	}

	public String getTaxfeepp() {
		return taxfeepp;
	}

	public void setTaxfeepp(String taxfeepp) {
		this.taxfeepp = taxfeepp;
	}

	public String getTaxfeecc() {
		return taxfeecc;
	}

	public void setTaxfeecc(String taxfeecc) {
		this.taxfeecc = taxfeecc;
	}

	public String getAgentothfeepp() {
		return agentothfeepp;
	}

	public void setAgentothfeepp(String agentothfeepp) {
		this.agentothfeepp = agentothfeepp;
	}

	public String getAgentothfeecc() {
		return agentothfeecc;
	}

	public void setAgentothfeecc(String agentothfeecc) {
		this.agentothfeecc = agentothfeecc;
	}

	public String getCarrothfeepp() {
		return carrothfeepp;
	}

	public void setCarrothfeepp(String carrothfeepp) {
		this.carrothfeepp = carrothfeepp;
	}

	public String getCarrothfeecc() {
		return carrothfeecc;
	}

	public void setCarrothfeecc(String carrothfeecc) {
		this.carrothfeecc = carrothfeecc;
	}

	public String getOtherfeepp() {
		return otherfeepp;
	}

	public void setOtherfeepp(String otherfeepp) {
		this.otherfeepp = otherfeepp;
	}

	public String getOtherfeecc() {
		return otherfeecc;
	}

	public void setOtherfeecc(String otherfeecc) {
		this.otherfeecc = otherfeecc;
	}

	public String getFeesumpp() {
		return feesumpp;
	}

	public void setFeesumpp(String feesumpp) {
		this.feesumpp = feesumpp;
	}

	public String getFeesumcc() {
		return feesumcc;
	}

	public void setFeesumcc(String feesumcc) {
		this.feesumcc = feesumcc;
	}

	public String getPolcyid() {
		return polcyid;
	}

	public void setPolcyid(String polcyid) {
		this.polcyid = polcyid;
	}

	public String getTransamt() {
		return transamt;
	}

	public void setTransamt(String transamt) {
		this.transamt = transamt;
	}

	public String getPodcyid() {
		return podcyid;
	}

	public void setPodcyid(String podcyid) {
		this.podcyid = podcyid;
	}

	public String getPodxrate() {
		return podxrate;
	}

	public void setPodxrate(String podxrate) {
		this.podxrate = podxrate;
	}

	public String getPodfee() {
		return podfee;
	}

	public void setPodfee(String podfee) {
		this.podfee = podfee;
	}

	public String getCcsumto() {
		return ccsumto;
	}

	public void setCcsumto(String ccsumto) {
		this.ccsumto = ccsumto;
	}

	public String getSumto() {
		return sumto;
	}

	public void setSumto(String sumto) {
		this.sumto = sumto;
	}

	public String getInsuranceamt() {
		return insuranceamt;
	}

	public void setInsuranceamt(String insuranceamt) {
		this.insuranceamt = insuranceamt;
	}

	public String getCustomeamt() {
		return customeamt;
	}

	public void setCustomeamt(String customeamt) {
		this.customeamt = customeamt;
	}

	public String getPpccothfee() {
		return ppccothfee;
	}

	public void setPpccothfee(String ppccothfee) {
		this.ppccothfee = ppccothfee;
	}

	public String getPpccpaytype() {
		return ppccpaytype;
	}

	public void setPpccpaytype(String ppccpaytype) {
		this.ppccpaytype = ppccpaytype;
	}

	public String getTo1() {
		return to1;
	}

	public void setTo1(String to1) {
		this.to1 = to1;
	}

	public String getTo2() {
		return to2;
	}

	public void setTo2(String to2) {
		this.to2 = to2;
	}

	public String getTo3() {
		return to3;
	}

	public void setTo3(String to3) {
		this.to3 = to3;
	}

	public String getBy1() {
		return by1;
	}

	public void setBy1(String by1) {
		this.by1 = by1;
	}

	public String getBy2() {
		return by2;
	}

	public void setBy2(String by2) {
		this.by2 = by2;
	}

	public String getBy3() {
		return by3;
	}

	public void setBy3(String by3) {
		this.by3 = by3;
	}

	public String getOther1() {
		return other1;
	}

	public void setOther1(String other1) {
		this.other1 = other1;
	}

	public String getOther2() {
		return other2;
	}

	public void setOther2(String other2) {
		this.other2 = other2;
	}

	public String getOther3() {
		return other3;
	}

	public void setOther3(String other3) {
		this.other3 = other3;
	}

	public java.lang.String getPolcode() {
		return polcode;
	}

	public void setPolcode(java.lang.String polcode) {
		this.polcode = polcode;
	}

	public java.lang.String getPodcode() {
		return podcode;
	}

	public void setPodcode(java.lang.String podcode) {
		this.podcode = podcode;
	}

	public java.lang.Boolean getIscommongoods() {
		return iscommongoods;
	}

	public void setIscommongoods(java.lang.Boolean iscommongoods) {
		this.iscommongoods = iscommongoods;
	}

	public java.lang.Boolean getIsmagnetic() {
		return ismagnetic;
	}

	public void setIsmagnetic(java.lang.Boolean ismagnetic) {
		this.ismagnetic = ismagnetic;
	}

	public java.lang.Boolean getIswithbattery() {
		return iswithbattery;
	}

	public void setIswithbattery(java.lang.Boolean iswithbattery) {
		this.iswithbattery = iswithbattery;
	}

	public java.lang.Boolean getIsequipment() {
		return isequipment;
	}

	public void setIsequipment(java.lang.Boolean isequipment) {
		this.isequipment = isequipment;
	}

	public java.lang.Boolean getIsgas() {
		return isgas;
	}

	public void setIsgas(java.lang.Boolean isgas) {
		this.isgas = isgas;
	}

	public java.lang.Boolean getIspowder() {
		return ispowder;
	}

	public void setIspowder(java.lang.Boolean ispowder) {
		this.ispowder = ispowder;
	}

	public java.lang.Boolean getIsparticles() {
		return isparticles;
	}

	public void setIsparticles(java.lang.Boolean isparticles) {
		this.isparticles = isparticles;
	}

	public java.lang.Boolean getIsliquid() {
		return isliquid;
	}

	public void setIsliquid(java.lang.Boolean isliquid) {
		this.isliquid = isliquid;
	}

	public java.lang.Boolean getIspaste() {
		return ispaste;
	}

	public void setIspaste(java.lang.Boolean ispaste) {
		this.ispaste = ispaste;
	}

	public java.lang.Boolean getIsphotograph() {
		return isphotograph;
	}

	public void setIsphotograph(java.lang.Boolean isphotograph) {
		this.isphotograph = isphotograph;
	}

	public java.lang.Boolean getIscertificateoforigin() {
		return iscertificateoforigin;
	}

	public void setIscertificateoforigin(java.lang.Boolean iscertificateoforigin) {
		this.iscertificateoforigin = iscertificateoforigin;
	}

	public java.lang.Boolean getIsfumigationcertificate() {
		return isfumigationcertificate;
	}

	public void setIsfumigationcertificate(java.lang.Boolean isfumigationcertificate) {
		this.isfumigationcertificate = isfumigationcertificate;
	}

	public java.lang.Boolean getIspallet() {
		return ispallet;
	}

	public void setIspallet(java.lang.Boolean ispallet) {
		this.ispallet = ispallet;
	}

	public java.lang.Boolean getIsinvoiceendorsement() {
		return isinvoiceendorsement;
	}

	public void setIsinvoiceendorsement(java.lang.Boolean isinvoiceendorsement) {
		this.isinvoiceendorsement = isinvoiceendorsement;
	}

	public java.lang.Boolean getIsdetect() {
		return isdetect;
	}

	public void setIsdetect(java.lang.Boolean isdetect) {
		this.isdetect = isdetect;
	}

	public java.lang.Boolean getIsletterofcredit() {
		return isletterofcredit;
	}

	public void setIsletterofcredit(java.lang.Boolean isletterofcredit) {
		this.isletterofcredit = isletterofcredit;
	}

	public java.lang.Boolean getIslabel() {
		return islabel;
	}

	public void setIslabel(java.lang.Boolean islabel) {
		this.islabel = islabel;
	}

	public java.lang.Boolean getIsbuyinsurance() {
		return isbuyinsurance;
	}

	public void setIsbuyinsurance(java.lang.Boolean isbuyinsurance) {
		this.isbuyinsurance = isbuyinsurance;
	}

	public java.lang.String getFlightno3() {
		return flightno3;
	}

	public void setFlightno3(java.lang.String flightno3) {
		this.flightno3 = flightno3;
	}

	public java.util.Date getFlightdate3() {
		return flightdate3;
	}

	public void setFlightdate3(java.util.Date flightdate3) {
		this.flightdate3 = flightdate3;
	}

	public java.lang.String getDeliverytype() {
		return deliverytype;
	}

	public void setDeliverytype(java.lang.String deliverytype) {
		this.deliverytype = deliverytype;
	}

	public java.lang.Long getTowcompanyid() {
		return towcompanyid;
	}

	public void setTowcompanyid(java.lang.Long towcompanyid) {
		this.towcompanyid = towcompanyid;
	}

	public java.util.Date getDeliverytime() {
		return deliverytime;
	}

	public void setDeliverytime(java.util.Date deliverytime) {
		this.deliverytime = deliverytime;
	}
	public java.util.Date getSchedulflight() {
		return schedulflight;
	}

	public void setSchedulflight(java.util.Date schedulflight) {
		this.schedulflight = schedulflight;
	}

	public java.lang.String getCustomsclearanceway() {
		return customsclearanceway;
	}

	public void setCustomsclearanceway(java.lang.String customsclearanceway) {
		this.customsclearanceway = customsclearanceway;
	}

	public java.lang.String getCustomsclearancetype() {
		return customsclearancetype;
	}

	public void setCustomsclearancetype(java.lang.String customsclearancetype) {
		this.customsclearancetype = customsclearancetype;
	}

	public java.lang.String getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(java.lang.String ordertype) {
		this.ordertype = ordertype;
	}

	public java.lang.String getArrivalpiece1() {
		return arrivalpiece1;
	}

	public void setArrivalpiece1(java.lang.String arrivalpiece1) {
		this.arrivalpiece1 = arrivalpiece1;
	}

	public java.lang.String getTrucknumber1() {
		return trucknumber1;
	}

	public void setTrucknumber1(java.lang.String trucknumber1) {
		this.trucknumber1 = trucknumber1;
	}

	public java.lang.String getDriverphone1() {
		return driverphone1;
	}

	public void setDriverphone1(java.lang.String driverphone1) {
		this.driverphone1 = driverphone1;
	}

	public java.lang.String getArrivalpiece2() {
		return arrivalpiece2;
	}

	public void setArrivalpiece2(java.lang.String arrivalpiece2) {
		this.arrivalpiece2 = arrivalpiece2;
	}

	public java.lang.String getTrucknumber2() {
		return trucknumber2;
	}

	public void setTrucknumber2(java.lang.String trucknumber2) {
		this.trucknumber2 = trucknumber2;
	}

	public java.lang.String getDriverphone2() {
		return driverphone2;
	}

	public void setDriverphone2(java.lang.String driverphone2) {
		this.driverphone2 = driverphone2;
	}

	public java.lang.String getArrivalpiece3() {
		return arrivalpiece3;
	}

	public void setArrivalpiece3(java.lang.String arrivalpiece3) {
		this.arrivalpiece3 = arrivalpiece3;
	}

	public java.lang.String getTrucknumber3() {
		return trucknumber3;
	}

	public void setTrucknumber3(java.lang.String trucknumber3) {
		this.trucknumber3 = trucknumber3;
	}

	public java.lang.String getDriverphone3() {
		return driverphone3;
	}

	public void setDriverphone3(java.lang.String driverphone3) {
		this.driverphone3 = driverphone3;
	}

	public java.lang.String getInfo() {
		return info;
	}

	public void setInfo(java.lang.String info) {
		this.info = info;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
	}

	public java.util.Date getArrivaltime() {
		return arrivaltime;
	}

	public void setArrivaltime(java.util.Date arrivaltime) {
		this.arrivaltime = arrivaltime;
	}

	public java.lang.String getReceiver() {
		return receiver;
	}

	public void setReceiver(java.lang.String receiver) {
		this.receiver = receiver;
	}

	public java.lang.String getInputer2() {
		return inputer2;
	}

	public void setInputer2(java.lang.String inputer2) {
		this.inputer2 = inputer2;
	}

	public java.lang.String getOrdertype2() {
		return ordertype2;
	}

	public void setOrdertype2(java.lang.String ordertype2) {
		this.ordertype2 = ordertype2;
	}

	public java.lang.String getSizeremarks() {
		return sizeremarks;
	}

	public void setSizeremarks(java.lang.String sizeremarks) {
		this.sizeremarks = sizeremarks;
	}

	public java.lang.Integer getPiece3() {
		return piece3;
	}

	public void setPiece3(java.lang.Integer piece3) {
		this.piece3 = piece3;
	}

	public java.math.BigDecimal getWeight3() {
		return weight3;
	}

	public void setWeight3(java.math.BigDecimal weight3) {
		this.weight3 = weight3;
	}

	public java.math.BigDecimal getChargeweight3() {
		return chargeweight3;
	}

	public void setChargeweight3(java.math.BigDecimal chargeweight3) {
		this.chargeweight3 = chargeweight3;
	}

	public java.math.BigDecimal getVolume3() {
		return volume3;
	}

	public void setVolume3(java.math.BigDecimal volume3) {
		this.volume3 = volume3;
	}

	public java.lang.Integer getPiece4() {
		return piece4;
	}

	public void setPiece4(java.lang.Integer piece4) {
		this.piece4 = piece4;
	}

	public java.math.BigDecimal getWeight4() {
		return weight4;
	}

	public void setWeight4(java.math.BigDecimal weight4) {
		this.weight4 = weight4;
	}

	public java.math.BigDecimal getChargeweight4() {
		return chargeweight4;
	}

	public void setChargeweight4(java.math.BigDecimal chargeweight4) {
		this.chargeweight4 = chargeweight4;
	}

	public java.math.BigDecimal getVolume4() {
		return volume4;
	}

	public void setVolume4(java.math.BigDecimal volume4) {
		this.volume4 = volume4;
	}

	public java.math.BigDecimal getVolwgt2() {
		return volwgt2;
	}

	public void setVolwgt2(java.math.BigDecimal volwgt2) {
		this.volwgt2 = volwgt2;
	}

	public java.math.BigDecimal getVolwgt3() {
		return volwgt3;
	}

	public void setVolwgt3(java.math.BigDecimal volwgt3) {
		this.volwgt3 = volwgt3;
	}

	public java.math.BigDecimal getVolwgt4() {
		return volwgt4;
	}

	public void setVolwgt4(java.math.BigDecimal volwgt4) {
		this.volwgt4 = volwgt4;
	}

	public java.util.Date getCls() {
		return cls;
	}

	public void setCls(java.util.Date cls) {
		this.cls = cls;
	}

	public java.lang.String getEntrytype() {
		return entrytype;
	}

	public void setEntrytype(java.lang.String entrytype) {
		this.entrytype = entrytype;
	}

	public java.lang.String getFreightstation() {
		return freightstation;
	}

	public void setFreightstation(java.lang.String freightstation) {
		this.freightstation = freightstation;
	}

	public java.lang.String getDeliveryplace() {
		return deliveryplace;
	}

	public void setDeliveryplace(java.lang.String deliveryplace) {
		this.deliveryplace = deliveryplace;
	}

	public java.lang.Long getCustomscompid() {
		return customscompid;
	}

	public void setCustomscompid(java.lang.Long customscompid) {
		this.customscompid = customscompid;
	}

	public java.lang.String getRemarks2() {
		return remarks2;
	}

	public void setRemarks2(java.lang.String remarks2) {
		this.remarks2 = remarks2;
	}

	public java.lang.String getAirline() {
		return airline;
	}

	public void setAirline(java.lang.String airline) {
		this.airline = airline;
	}

	public java.lang.String getBookingcode() {
		return bookingcode;
	}

	public void setBookingcode(java.lang.String bookingcode) {
		this.bookingcode = bookingcode;
	}

	public java.lang.String getRemarks3() {
		return remarks3;
	}

	public void setRemarks3(java.lang.String remarks3) {
		this.remarks3 = remarks3;
	}

	public java.lang.String getPolcode2() {
		return polcode2;
	}

	public void setPolcode2(java.lang.String polcode2) {
		this.polcode2 = polcode2;
	}

	public java.lang.String getTransitport1() {
		return transitport1;
	}

	public void setTransitport1(java.lang.String transitport1) {
		this.transitport1 = transitport1;
	}

	public java.lang.String getTransitport2() {
		return transitport2;
	}

	public void setTransitport2(java.lang.String transitport2) {
		this.transitport2 = transitport2;
	}

	public java.lang.String getPodcode2() {
		return podcode2;
	}

	public void setPodcode2(java.lang.String podcode2) {
		this.podcode2 = podcode2;
	}

	public java.lang.String getFlightno4() {
		return flightno4;
	}

	public void setFlightno4(java.lang.String flightno4) {
		this.flightno4 = flightno4;
	}

	public java.util.Date getFlightdate4() {
		return flightdate4;
	}

	public void setFlightdate4(java.util.Date flightdate4) {
		this.flightdate4 = flightdate4;
	}

	public java.lang.String getFlightno5() {
		return flightno5;
	}

	public void setFlightno5(java.lang.String flightno5) {
		this.flightno5 = flightno5;
	}

	public java.util.Date getFlightdate5() {
		return flightdate5;
	}

	public void setFlightdate5(java.util.Date flightdate5) {
		this.flightdate5 = flightdate5;
	}

	public java.lang.String getFlightno6() {
		return flightno6;
	}

	public void setFlightno6(java.lang.String flightno6) {
		this.flightno6 = flightno6;
	}

	public java.util.Date getFlightdate6() {
		return flightdate6;
	}

	public void setFlightdate6(java.util.Date flightdate6) {
		this.flightdate6 = flightdate6;
	}

	public java.lang.String getFlightno7() {
		return flightno7;
	}

	public void setFlightno7(java.lang.String flightno7) {
		this.flightno7 = flightno7;
	}

	public java.util.Date getFlightdate7() {
		return flightdate7;
	}

	public void setFlightdate7(java.util.Date flightdate7) {
		this.flightdate7 = flightdate7;
	}

	public java.lang.String getFlightno8() {
		return flightno8;
	}

	public void setFlightno8(java.lang.String flightno8) {
		this.flightno8 = flightno8;
	}

	public java.util.Date getFlightdate8() {
		return flightdate8;
	}

	public void setFlightdate8(java.util.Date flightdate8) {
		this.flightdate8 = flightdate8;
	}

	public java.lang.String getFlightno9() {
		return flightno9;
	}

	public void setFlightno9(java.lang.String flightno9) {
		this.flightno9 = flightno9;
	}

	public java.util.Date getFlightdate9() {
		return flightdate9;
	}

	public void setFlightdate9(java.util.Date flightdate9) {
		this.flightdate9 = flightdate9;
	}

	public java.lang.Integer getPiece5() {
		return piece5;
	}

	public void setPiece5(java.lang.Integer piece5) {
		this.piece5 = piece5;
	}

	public java.math.BigDecimal getWeight5() {
		return weight5;
	}

	public void setWeight5(java.math.BigDecimal weight5) {
		this.weight5 = weight5;
	}

	public java.math.BigDecimal getChargeweight5() {
		return chargeweight5;
	}

	public void setChargeweight5(java.math.BigDecimal chargeweight5) {
		this.chargeweight5 = chargeweight5;
	}

	public java.math.BigDecimal getVolume5() {
		return volume5;
	}

	public void setVolume5(java.math.BigDecimal volume5) {
		this.volume5 = volume5;
	}

	public java.math.BigDecimal getVolwgt5() {
		return volwgt5;
	}

	public void setVolwgt5(java.math.BigDecimal volwgt5) {
		this.volwgt5 = volwgt5;
	}

	public java.lang.String getBillno() {
		return billno;
	}

	public void setBillno(java.lang.String billno) {
		this.billno = billno;
	}

	public java.lang.String getCustomscontact() {
		return customscontact;
	}

	public void setCustomscontact(java.lang.String customscontact) {
		this.customscontact = customscontact;
	}

	public java.lang.String getBookingstatus() {
		return bookingstatus;
	}

	public void setBookingstatus(java.lang.String bookingstatus) {
		this.bookingstatus = bookingstatus;
	}

	public java.lang.String getRemarks4() {
		return remarks4;
	}

	public void setRemarks4(java.lang.String remarks4) {
		this.remarks4 = remarks4;
	}

	public java.lang.String getGoodsnamee() {
		return goodsnamee;
	}

	public void setGoodsnamee(java.lang.String goodsnamee) {
		this.goodsnamee = goodsnamee;
	}

	public java.lang.String getOther4() {
		return other4;
	}

	public void setOther4(java.lang.String other4) {
		this.other4 = other4;
	}

	public java.lang.Boolean getIswreceipt() {
		return iswreceipt;
	}

	public void setIswreceipt(java.lang.Boolean iswreceipt) {
		this.iswreceipt = iswreceipt;
	}

	public java.lang.Boolean getIswreceiptoriginal() {
		return iswreceiptoriginal;
	}

	public void setIswreceiptoriginal(java.lang.Boolean iswreceiptoriginal) {
		this.iswreceiptoriginal = iswreceiptoriginal;
	}

	public java.lang.Boolean getIspackinglist() {
		return ispackinglist;
	}

	public void setIspackinglist(java.lang.Boolean ispackinglist) {
		this.ispackinglist = ispackinglist;
	}

	public java.lang.Boolean getIsinvoice() {
		return isinvoice;
	}

	public void setIsinvoice(java.lang.Boolean isinvoice) {
		this.isinvoice = isinvoice;
	}

	public java.lang.Boolean getIscofo() {
		return iscofo;
	}

	public void setIscofo(java.lang.Boolean iscofo) {
		this.iscofo = iscofo;
	}

	public java.lang.String getOther5() {
		return other5;
	}

	public void setOther5(java.lang.String other5) {
		this.other5 = other5;
	}

	public java.lang.String getBusstatus() {
		return busstatus;
	}

	public void setBusstatus(java.lang.String busstatus) {
		this.busstatus = busstatus;
	}
	
}