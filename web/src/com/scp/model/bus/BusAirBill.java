package com.scp.model.bus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "bus_air_bill")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusAirBill implements java.io.Serializable {

	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;

	@Column(name = "busairid")
	private java.lang.Long busairid;
	
	@Column(name = "jobid")
	private java.lang.Long jobid;
	
	@Column(name = "billcount")
	private java.lang.String billcount;
	
	@Column(name = "cneeid")
	private java.lang.Long cneeid;

	@Column(name = "cneetitle")
	private java.lang.String cneetitle;

	@Column(name = "cneename")
	private java.lang.String cneename;
	
	@Column(name = "cnorid")
	private java.lang.Long cnorid;

	@Column(name = "cnortitle")
	private java.lang.String cnortitle;

	@Column(name = "cnorname")
	private java.lang.String cnorname;

	@Column(name = "notifyid")
	private java.lang.Long notifyid;

	@Column(name = "notifytitle")
	private java.lang.String notifytitle;

	@Column(name = "notifyname")
	private java.lang.String notifyname;
	
	@Column(name = "notifyid2")
	private java.lang.Long notifyid2;

	@Column(name = "notifytitle2")
	private java.lang.String notifytitle2;

	@Column(name = "notifyname2")
	private java.lang.String notifyname2;
	
	@Column(name = "agentdesid")
	private java.lang.Long agentdesid;
	
	@Column(name = "agentdesabbr")
	private java.lang.String agentdesabbr;
	
	@Column(name = "agentdescode")
	private java.lang.String agentdescode;
	
	@Column(name = "pretrans ", length = 50)
	private java.lang.String pretrans;
	
	@Column(name = "hawbno ", length = 30)
	private java.lang.String hawbno;
	
	@Column(name = "mawbno ", length = 30)
	private java.lang.String mawbno;
	
	@Column(name = "bltype ", length = 1)
	private java.lang.String bltype;
	
	@Column(name = "signcarrier ", length = 30)
	private java.lang.String signcarrier;
	
	@Column(name = "atd", length = 13)
	private java.util.Date atd;
	
	@Column(name = "packid")
	private java.lang.Long packid;
	
	@Column(name = "piece")
	private java.lang.String piece;
	
	@Column(name = "polid")
	private java.lang.Long polid;

	@Column(name = "pol", length = 50)
	private java.lang.String pol;
	
	@Column(name = "podid")
	private java.lang.Long podid;

	@Column(name = "pod", length = 50)
	private java.lang.String pod;
	
	@Column(name = "agentid")
	private java.lang.Long agentid;
	
	@Column(name = "destination")
	private java.lang.String destination;
	
	@Column(name = "carrierid")
	private java.lang.Long carrierid;
	
	@Column(name = "flightno1 ", length = 100)
	private java.lang.String flightno1;
	
	@Column(name = "flightdate1", length = 29)
	private java.util.Date flightdate1;
	
	@Column(name = "flightno2 ", length = 100)
	private java.lang.String flightno2;
	
	@Column(name = "flightdate2", length = 29)
	private java.util.Date flightdate2;
	
	@Column(name = "marksno")
	private java.lang.String marksno;
	
	@Column(name = "goodsdesc")
	private java.lang.String goodsdesc;
	
	@Column(name = "totledesc")
	private java.lang.String totledesc;

	@Column(name = "grswgt")
	private java.lang.String grswgt;

	@Column(name = "cbm")
	private java.lang.String cbm;
	
	@Column(name = "isdetail")
	private java.lang.Boolean isdetail;
	
	@Column(name = "corpid")
	private java.lang.Long corpid;

	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;

	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;

	@Column(name = "updater", length = 100)
	private java.lang.String updater;

	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;
	
	@Column(name = "polcode", length = 20)
	private java.lang.String polcode;
	
	@Column(name = "podcode", length = 20)
	private java.lang.String podcode;
	
	@Column(name = "to1 ", length = 30)
	private java.lang.String to1;
	
	@Column(name = "to2 ", length = 30)
	private java.lang.String to2;
	
	@Column(name = "to3 ", length = 30)
	private java.lang.String to3;
	
	@Column(name = "by1 ", length = 30)
	private java.lang.String by1;
	
	@Column(name = "by2 ", length = 30)
	private java.lang.String by2;
	
	@Column(name = "by3 ", length = 30)
	private java.lang.String by3;
	
	@Column(name = "other1")
	private java.lang.String other1;
	
	@Column(name = "other2")
	private java.lang.String other2;
	
	@Column(name = "other3")
	private java.lang.String other3;
	
	@Column(name = "singtime", length = 29)
	private java.util.Date singtime;
	
	@Column(name = "refno ", length = 30)
	private java.lang.String refno;
	
	@Column(name = "sono ", length = 30)
	private java.lang.String sono;
	
	@Column(name = "ppccgoods ", length = 30)
	private java.lang.String ppccgoods;
	
	@Column(name = "ppccothfee ", length = 30)
	private java.lang.String ppccothfee;
	
	@Column(name = "ppccpaytype ", length = 30)
	private java.lang.String ppccpaytype;
	
	@Column(name = "eta", length = 29)
	private java.util.Date eta;
	
	@Column(name = "ata", length = 29)
	private java.util.Date ata;
	
	
	@Column(name = "airfreightpp ", length = 30)
	private java.lang.String airfreightpp;
	
	@Column(name = "airfreightcc ", length = 30)
	private java.lang.String airfreightcc;
	
	@Column(name = "valueaddpp ", length = 30)
	private java.lang.String valueaddpp;
	
	@Column(name = "valueaddcc ", length = 30)
	private java.lang.String valueaddcc;
	
	@Column(name = "insurancepp ", length = 30)
	private java.lang.String insurancepp;
	
	@Column(name = "insurancecc ", length = 30)
	private java.lang.String insurancecc;
	
	@Column(name = "taxfeepp ", length = 30)
	private java.lang.String taxfeepp;
	
	@Column(name = "taxfeecc ", length = 30)
	private java.lang.String taxfeecc;
	
	
	
	@Column(name = "agentothfeepp ", length = 30)
	private java.lang.String agentothfeepp;
	
	@Column(name = "agentothfeecc ", length = 30)
	private java.lang.String agentothfeecc;
	
	@Column(name = "carrothfeepp ", length = 30)
	private java.lang.String carrothfeepp;
	
	@Column(name = "carrothfeecc ", length = 30)
	private java.lang.String carrothfeecc;
	
	@Column(name = "otherfeepp ", length = 30)
	private java.lang.String otherfeepp;
	
	@Column(name = "otherfeecc ", length = 30)
	private java.lang.String otherfeecc;
	
	@Column(name = "feesumpp ", length = 30)
	private java.lang.String feesumpp;
	
	@Column(name = "feesumcc ", length = 30)
	private java.lang.String feesumcc;
	
	@Column(name = "polcyid ", length = 30)
	private java.lang.String polcyid;
	
	@Column(name = "transamt ", length = 30)
	private java.lang.String transamt;
	
	@Column(name = "podcyid ", length = 30)
	private java.lang.String podcyid;
	
	@Column(name = "podxrate ", length = 30)
	private java.lang.String podxrate;
	
	@Column(name = "podfee ", length = 30)
	private java.lang.String podfee;
	
	@Column(name = "ccsumto ", length = 30)
	private java.lang.String ccsumto;
	
	@Column(name = "sumto ", length = 30)
	private java.lang.String sumto;
	
	@Column(name = "insuranceamt ", length = 30)
	private java.lang.String insuranceamt;
	
	@Column(name = "customeamt ", length = 30)
	private java.lang.String customeamt;
	
	
	@Column(name = "weight")
	private java.lang.String weight;
	
	@Column(name = "unit")
	private java.lang.String unit;
	
	@Column(name = "rateclass")
	private java.lang.String rateclass;
	
	@Column(name = "markno")
	private java.lang.String markno;
	
	@Column(name = "chargeweight")
	private java.lang.String chargeweight;
	
	@Column(name = "volume")
	private java.lang.String volume;
	
	@Column(name = "volumeweight")
	private java.lang.String volumeweight;
	
	@Column(name = "charge")
	private java.lang.String charge;
	
	@Column(name = "amount")
	private java.lang.String amount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.Long getBusairid() {
		return busairid;
	}

	public void setBusairid(java.lang.Long busairid) {
		this.busairid = busairid;
	}

	public java.lang.Long getJobid() {
		return jobid;
	}

	public void setJobid(java.lang.Long jobid) {
		this.jobid = jobid;
	}

	public java.lang.String getBillcount() {
		return billcount;
	}

	public void setBillcount(java.lang.String billcount) {
		this.billcount = billcount;
	}

	public java.lang.Long getCneeid() {
		return cneeid;
	}

	public void setCneeid(java.lang.Long cneeid) {
		this.cneeid = cneeid;
	}

	public java.lang.String getCneetitle() {
		return cneetitle;
	}

	public void setCneetitle(java.lang.String cneetitle) {
		this.cneetitle = cneetitle;
	}

	public java.lang.String getCneename() {
		return cneename;
	}

	public void setCneename(java.lang.String cneename) {
		this.cneename = cneename;
	}

	public java.lang.Long getCnorid() {
		return cnorid;
	}

	public void setCnorid(java.lang.Long cnorid) {
		this.cnorid = cnorid;
	}

	public java.lang.String getCnortitle() {
		return cnortitle;
	}

	public void setCnortitle(java.lang.String cnortitle) {
		this.cnortitle = cnortitle;
	}

	public java.lang.String getCnorname() {
		return cnorname;
	}

	public void setCnorname(java.lang.String cnorname) {
		this.cnorname = cnorname;
	}

	public java.lang.Long getNotifyid() {
		return notifyid;
	}

	public void setNotifyid(java.lang.Long notifyid) {
		this.notifyid = notifyid;
	}

	public java.lang.String getNotifytitle() {
		return notifytitle;
	}

	public void setNotifytitle(java.lang.String notifytitle) {
		this.notifytitle = notifytitle;
	}

	public java.lang.String getNotifyname() {
		return notifyname;
	}

	public void setNotifyname(java.lang.String notifyname) {
		this.notifyname = notifyname;
	}

	public java.lang.Long getNotifyid2() {
		return notifyid2;
	}

	public void setNotifyid2(java.lang.Long notifyid2) {
		this.notifyid2 = notifyid2;
	}

	public java.lang.String getNotifytitle2() {
		return notifytitle2;
	}

	public void setNotifytitle2(java.lang.String notifytitle2) {
		this.notifytitle2 = notifytitle2;
	}

	public java.lang.String getNotifyname2() {
		return notifyname2;
	}

	public void setNotifyname2(java.lang.String notifyname2) {
		this.notifyname2 = notifyname2;
	}

	public java.lang.Long getAgentdesid() {
		return agentdesid;
	}

	public void setAgentdesid(java.lang.Long agentdesid) {
		this.agentdesid = agentdesid;
	}

	public java.lang.String getAgentdesabbr() {
		return agentdesabbr;
	}

	public void setAgentdesabbr(java.lang.String agentdesabbr) {
		this.agentdesabbr = agentdesabbr;
	}

	public java.lang.String getAgentdescode() {
		return agentdescode;
	}

	public void setAgentdescode(java.lang.String agentdescode) {
		this.agentdescode = agentdescode;
	}

	public java.lang.String getPretrans() {
		return pretrans;
	}

	public void setPretrans(java.lang.String pretrans) {
		this.pretrans = pretrans;
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

	public java.lang.String getBltype() {
		return bltype;
	}

	public void setBltype(java.lang.String bltype) {
		this.bltype = bltype;
	}

	public java.lang.String getSigncarrier() {
		return signcarrier;
	}

	public void setSigncarrier(java.lang.String signcarrier) {
		this.signcarrier = signcarrier;
	}

	public java.util.Date getAtd() {
		return atd;
	}

	public void setAtd(java.util.Date atd) {
		this.atd = atd;
	}

	public java.lang.Long getPackid() {
		return packid;
	}

	public void setPackid(java.lang.Long packid) {
		this.packid = packid;
	}

	public java.lang.String getPiece() {
		return piece;
	}

	public void setPiece(java.lang.String piece) {
		this.piece = piece;
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

	public java.lang.Long getAgentid() {
		return agentid;
	}

	public void setAgentid(java.lang.Long agentid) {
		this.agentid = agentid;
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

	public java.lang.String getMarksno() {
		return marksno;
	}

	public void setMarksno(java.lang.String marksno) {
		this.marksno = marksno;
	}

	public java.lang.String getGoodsdesc() {
		return goodsdesc;
	}

	public void setGoodsdesc(java.lang.String goodsdesc) {
		this.goodsdesc = goodsdesc;
	}

	public java.lang.String getTotledesc() {
		return totledesc;
	}

	public void setTotledesc(java.lang.String totledesc) {
		this.totledesc = totledesc;
	}

	public java.lang.String getGrswgt() {
		return grswgt;
	}

	public void setGrswgt(java.lang.String grswgt) {
		this.grswgt = grswgt;
	}

	public java.lang.String getCbm() {
		return cbm;
	}

	public void setCbm(java.lang.String cbm) {
		this.cbm = cbm;
	}

	public java.lang.Boolean getIsdetail() {
		return isdetail;
	}

	public void setIsdetail(java.lang.Boolean isdetail) {
		this.isdetail = isdetail;
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

	public java.lang.String getTo1() {
		return to1;
	}

	public void setTo1(java.lang.String to1) {
		this.to1 = to1;
	}

	public java.lang.String getTo2() {
		return to2;
	}

	public void setTo2(java.lang.String to2) {
		this.to2 = to2;
	}

	public java.lang.String getTo3() {
		return to3;
	}

	public void setTo3(java.lang.String to3) {
		this.to3 = to3;
	}

	public java.lang.String getBy1() {
		return by1;
	}

	public void setBy1(java.lang.String by1) {
		this.by1 = by1;
	}

	public java.lang.String getBy2() {
		return by2;
	}

	public void setBy2(java.lang.String by2) {
		this.by2 = by2;
	}

	public java.lang.String getBy3() {
		return by3;
	}

	public void setBy3(java.lang.String by3) {
		this.by3 = by3;
	}

	public java.lang.String getOther1() {
		return other1;
	}

	public void setOther1(java.lang.String other1) {
		this.other1 = other1;
	}

	public java.lang.String getOther2() {
		return other2;
	}

	public void setOther2(java.lang.String other2) {
		this.other2 = other2;
	}

	public java.lang.String getOther3() {
		return other3;
	}

	public void setOther3(java.lang.String other3) {
		this.other3 = other3;
	}

	public java.util.Date getSingtime() {
		return singtime;
	}

	public void setSingtime(java.util.Date singtime) {
		this.singtime = singtime;
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

	public java.lang.String getPpccgoods() {
		return ppccgoods;
	}

	public void setPpccgoods(java.lang.String ppccgoods) {
		this.ppccgoods = ppccgoods;
	}

	public java.lang.String getPpccothfee() {
		return ppccothfee;
	}

	public void setPpccothfee(java.lang.String ppccothfee) {
		this.ppccothfee = ppccothfee;
	}

	public java.lang.String getPpccpaytype() {
		return ppccpaytype;
	}

	public void setPpccpaytype(java.lang.String ppccpaytype) {
		this.ppccpaytype = ppccpaytype;
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

	public java.lang.String getAirfreightpp() {
		return airfreightpp;
	}

	public void setAirfreightpp(java.lang.String airfreightpp) {
		this.airfreightpp = airfreightpp;
	}

	public java.lang.String getAirfreightcc() {
		return airfreightcc;
	}

	public void setAirfreightcc(java.lang.String airfreightcc) {
		this.airfreightcc = airfreightcc;
	}

	public java.lang.String getValueaddpp() {
		return valueaddpp;
	}

	public void setValueaddpp(java.lang.String valueaddpp) {
		this.valueaddpp = valueaddpp;
	}

	public java.lang.String getValueaddcc() {
		return valueaddcc;
	}

	public void setValueaddcc(java.lang.String valueaddcc) {
		this.valueaddcc = valueaddcc;
	}

	public java.lang.String getInsurancepp() {
		return insurancepp;
	}

	public void setInsurancepp(java.lang.String insurancepp) {
		this.insurancepp = insurancepp;
	}

	public java.lang.String getInsurancecc() {
		return insurancecc;
	}

	public void setInsurancecc(java.lang.String insurancecc) {
		this.insurancecc = insurancecc;
	}

	public java.lang.String getTaxfeepp() {
		return taxfeepp;
	}

	public void setTaxfeepp(java.lang.String taxfeepp) {
		this.taxfeepp = taxfeepp;
	}

	public java.lang.String getTaxfeecc() {
		return taxfeecc;
	}

	public void setTaxfeecc(java.lang.String taxfeecc) {
		this.taxfeecc = taxfeecc;
	}

	public java.lang.String getAgentothfeepp() {
		return agentothfeepp;
	}

	public void setAgentothfeepp(java.lang.String agentothfeepp) {
		this.agentothfeepp = agentothfeepp;
	}

	public java.lang.String getAgentothfeecc() {
		return agentothfeecc;
	}

	public void setAgentothfeecc(java.lang.String agentothfeecc) {
		this.agentothfeecc = agentothfeecc;
	}

	public java.lang.String getCarrothfeepp() {
		return carrothfeepp;
	}

	public void setCarrothfeepp(java.lang.String carrothfeepp) {
		this.carrothfeepp = carrothfeepp;
	}

	public java.lang.String getCarrothfeecc() {
		return carrothfeecc;
	}

	public void setCarrothfeecc(java.lang.String carrothfeecc) {
		this.carrothfeecc = carrothfeecc;
	}

	public java.lang.String getOtherfeepp() {
		return otherfeepp;
	}

	public void setOtherfeepp(java.lang.String otherfeepp) {
		this.otherfeepp = otherfeepp;
	}

	public java.lang.String getOtherfeecc() {
		return otherfeecc;
	}

	public void setOtherfeecc(java.lang.String otherfeecc) {
		this.otherfeecc = otherfeecc;
	}

	public java.lang.String getFeesumpp() {
		return feesumpp;
	}

	public void setFeesumpp(java.lang.String feesumpp) {
		this.feesumpp = feesumpp;
	}

	public java.lang.String getFeesumcc() {
		return feesumcc;
	}

	public void setFeesumcc(java.lang.String feesumcc) {
		this.feesumcc = feesumcc;
	}

	public java.lang.String getPolcyid() {
		return polcyid;
	}

	public void setPolcyid(java.lang.String polcyid) {
		this.polcyid = polcyid;
	}

	public java.lang.String getTransamt() {
		return transamt;
	}

	public void setTransamt(java.lang.String transamt) {
		this.transamt = transamt;
	}

	public java.lang.String getPodcyid() {
		return podcyid;
	}

	public void setPodcyid(java.lang.String podcyid) {
		this.podcyid = podcyid;
	}

	public java.lang.String getPodxrate() {
		return podxrate;
	}

	public void setPodxrate(java.lang.String podxrate) {
		this.podxrate = podxrate;
	}

	public java.lang.String getPodfee() {
		return podfee;
	}

	public void setPodfee(java.lang.String podfee) {
		this.podfee = podfee;
	}

	public java.lang.String getCcsumto() {
		return ccsumto;
	}

	public void setCcsumto(java.lang.String ccsumto) {
		this.ccsumto = ccsumto;
	}

	public java.lang.String getSumto() {
		return sumto;
	}

	public void setSumto(java.lang.String sumto) {
		this.sumto = sumto;
	}

	public java.lang.String getInsuranceamt() {
		return insuranceamt;
	}

	public void setInsuranceamt(java.lang.String insuranceamt) {
		this.insuranceamt = insuranceamt;
	}

	public java.lang.String getCustomeamt() {
		return customeamt;
	}

	public void setCustomeamt(java.lang.String customeamt) {
		this.customeamt = customeamt;
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

	public java.lang.String getVolume() {
		return volume;
	}

	public void setVolume(java.lang.String volume) {
		this.volume = volume;
	}

	public java.lang.String getVolumeweight() {
		return volumeweight;
	}

	public void setVolumeweight(java.lang.String volumeweight) {
		this.volumeweight = volumeweight;
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
}