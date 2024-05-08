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
@Table(name = "bus_train_bill")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusTrainBill implements java.io.Serializable {

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
	@Column(name = "hblno", length = 30)
	private java.lang.String hblno;

	/**
	 *@generated
	 */
	@Column(name = "mblno ", length = 30)
	private java.lang.String mblno;

	/**
	 *@generated
	 */
	@Column(name = "bltype", length = 1)
	private java.lang.String bltype;

	/**
	 *@generated
	 */
	@Column(name = "billcount")
	private java.lang.String billcount;

	/**
	 *@generated
	 */
	@Column(name = "trainid")
	private java.lang.Long trainid;

	/**
	 *@generated
	 */
	@Column(name = "reportid")
	private java.lang.Long reportid;

	/**
	 *@generated
	 */
	@Column(name = "jobid")
	private java.lang.Long jobid;

	/**
	 *@generated
	 */
	@Column(name = "cneeid")
	private java.lang.Long cneeid;

	/**
	 *@generated
	 */
	@Column(name = "cneetitle")
	private java.lang.String cneetitle;

	/**
	 *@generated
	 */
	@Column(name = "cneename")
	private java.lang.String cneename;

	/**
	 *@generated
	 */
	@Column(name = "cnorid")
	private java.lang.Long cnorid;

	/**
	 *@generated
	 */
	@Column(name = "cnortitle")
	private java.lang.String cnortitle;

	/**
	 *@generated
	 */
	@Column(name = "cnorname")
	private java.lang.String cnorname;

	/**
	 *@generated
	 */
	@Column(name = "notifyid")
	private java.lang.Long notifyid;

	/**
	 *@generated
	 */
	@Column(name = "notifytitle")
	private java.lang.String notifytitle;

	/**
	 *@generated
	 */
	@Column(name = "notifyname")
	private java.lang.String notifyname;

	/**
	 *@generated
	 */
	@Column(name = "agenid")
	private java.lang.Long agenid;

	/**
	 *@generated
	 */
	@Column(name = "agentitle")
	private java.lang.String agentitle;

	/**
	 *@generated
	 */
	@Column(name = "agenname")
	private java.lang.String agenname;

	/**
	 *@generated
	 */
	@Column(name = "pretrans", length = 50)
	private java.lang.String pretrans;

	/**
	 *@generated
	 */
	@Column(name = "poa")
	private java.lang.String poa;

	/**
	 *@generated
	 */
	@Column(name = "hbltype", nullable = false, length = 1)
	private java.lang.String hbltype;
	
	
	@Column(name = "mbltype", nullable = false, length = 1)
	private java.lang.String mbltype;
	
	
	@Column(name = "paymentitem")
	private java.lang.String paymentitem;
	
	
	@Column(name = "payplace")
	private java.lang.String payplace;

	/**
	 *@generated
	 */
	@Column(name = "carrierid")
	private java.lang.Long carrierid;
	
	@Column(name = "polcode ", length = 20)
	private java.lang.String polcode;
	
	@Column(name = "pddcode ", length = 20)
	private java.lang.String pddcode;
	
	@Column(name = "podcode ", length = 20)
	private java.lang.String podcode;
	
	@Column(name = "signplace ", length = 50)
	private java.lang.String signplace;
	
	@Column(name = "shcountrycode ", length = 10)
	private java.lang.String shcountrycode;
	
	@Column(name = "shenterprisecode ", length = 30)
	private java.lang.String shenterprisecode;
	
	@Column(name = "shcontacts ", length = 30)
	private java.lang.String shcontacts;
	
	@Column(name = "cocountrycode ", length = 10)
	private java.lang.String cocountrycode;
	
	@Column(name = "coenterprisecode ", length = 30)
	private java.lang.String coenterprisecode;
	
	@Column(name = "cocontacts ", length = 30)
	private java.lang.String cocontacts;
	
	@Column(name = "nocountrycode ", length = 10)
	private java.lang.String nocountrycode;
	
	@Column(name = "noenterprisecode ", length = 30)
	private java.lang.String noenterprisecode;
	
	@Column(name = "nocontacts ", length = 30)
	private java.lang.String nocontacts;
	
	@Column(name = "destinationcode ", length = 20)
	private java.lang.String destinationcode;

	public java.lang.String getMbltype() {
		return mbltype;
	}

	public void setMbltype(java.lang.String mbltype) {
		this.mbltype = mbltype;
	}

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

	/**
	 *@generated
	 */
	@Column(name = "pddid")
	private java.lang.Long pddid;

	/**
	 *@generated
	 */
	@Column(name = "pdd", length = 50)
	private java.lang.String pdd;

	/**
	 *@generated
	 */
	@Column(name = "atd", length = 13)
	private java.util.Date atd;

	/**
	 *@generated
	 */
	@Column(name = "piece")
	private java.lang.String piece;

	/**
	 *@generated
	 */
	@Column(name = "grswgt")
	private java.lang.String grswgt;

	/**
	 *@generated
	 */
	@Column(name = "cbm")
	private java.lang.String cbm;

	/**
	 *@generated
	 */
	@Column(name = "carryitem", length = 15)
	private java.lang.String carryitem;

	/**
	 *@generated
	 */
	@Column(name = "freightitem", length = 2)
	private java.lang.String freightitem;

	/**
	 *@generated
	 */
	@Column(name = "loaditem")
	private java.lang.String loaditem;

	/**
	 *@generated
	 */
	@Column(name = "packid")
	private java.lang.Long packid;

	/**
	 *@generated
	 */
	@Column(name = "marksno")
	private java.lang.String marksno;

	/**
	 *@generated
	 */
	@Column(name = "goodsdesc")
	private java.lang.String goodsdesc;

	/**
	 *@generated
	 */
	@Column(name = "totledesc")
	private java.lang.String totledesc;

	/**
	 *@generated
	 */
	@Column(name = "corpid")
	private java.lang.Long corpid;

	/**
	 *@generated
	 */
	@Column(name = "isprintlock")
	private java.lang.Boolean isprintlock;

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
	@Column(name = "signcarrier", length = 30)
	private java.lang.String signcarrier;

	@Column(name = "isshowship")
	private java.lang.Boolean isshowship;

	@Column(name = "ismake")
	private java.lang.Boolean ismake;

	@Column(name = "datemake", length = 29)
	private java.util.Date datemake;

	@Column(name = "usermake", length = 30)
	private java.lang.String usermake;

	@Column(name = "isadjust")
	private java.lang.Boolean isadjust;

	@Column(name = "dateadjust", length = 29)
	private java.util.Date dateadjust;

	@Column(name = "useradjust", length = 30)
	private java.lang.String useradjust;

	@Column(name = "isconfirm")
	private java.lang.Boolean isconfirm;

	@Column(name = "dateconfirm", length = 29)
	private java.util.Date dateconfirm;

	@Column(name = "userconfirm", length = 30)
	private java.lang.String userconfirm;

	@Column(name = "isprint")
	private java.lang.Boolean isprint;

	@Column(name = "dateprint", length = 29)
	private java.util.Date dateprint;

	@Column(name = "userprint", length = 30)
	private java.lang.String userprint;

	@Column(name = "issign")
	private java.lang.Boolean issign;

	@Column(name = "datesign", length = 29)
	private java.util.Date datesign;

	@Column(name = "usersign", length = 30)
	private java.lang.String usersign;

	@Column(name = "iscomplete")
	private java.lang.Boolean iscomplete;

	@Column(name = "datecomplete", length = 29)
	private java.util.Date datecomplete;

	@Column(name = "usercomplete", length = 30)
	private java.lang.String usercomplete;
	
	@Column(name = "isdetail")
	private java.lang.Boolean isdetail;
	
	@Column(name = "destination")
	private java.lang.String destination;
	
	
	@Column(name = "isput")
	private boolean isput;
	
	@Column(name = "puter", length = 30)
	private String puter;
	
	@Column(name = "putdesc")
	private String putdesc;
	
	@Column(name = "ishold")
	private boolean ishold;
	
	@Column(name = "holder", length = 30)
	private String holder;
	
	@Column(name = "holddesc")
	private String holddesc;
	
	@Column(name = "puttime")
	private java.util.Date puttime;
	
	@Column(name = "holdtime")
	private java.util.Date holdtime;
	
	@Column(name = "istelrel")
	private java.lang.Boolean istelrel;

	@Column(name = "telreltime", length = 29)
	private java.util.Date telreltime;
	
	@Column(name = "telrelnos", length = 50)
	private java.lang.String telrelnos;
	
	@Column(name = "telreler", length = 30)
	private java.lang.String telreler;
	
	@Column(name = "telreltype ", length = 1)
	private java.lang.String telreltype;
	
	@Column(name = "telrelrptid")
	private java.lang.Long telrelrptid;
	
	/**
	 *@generated
	 */
	@Column(name = "format", length = 30)
	private java.lang.String format;

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
	public java.lang.String getHblno() {
		return this.hblno;
	}

	/**
	 *@generated
	 */
	public void setHblno(java.lang.String value) {
		this.hblno = value;
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
	public java.lang.Long getCneeid() {
		return this.cneeid;
	}

	/**
	 *@generated
	 */
	public void setCneeid(java.lang.Long value) {
		this.cneeid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCneetitle() {
		return this.cneetitle;
	}

	/**
	 *@generated
	 */
	public void setCneetitle(java.lang.String value) {
		this.cneetitle = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCneename() {
		return this.cneename;
	}

	/**
	 *@generated
	 */
	public void setCneename(java.lang.String value) {
		this.cneename = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getCnorid() {
		return this.cnorid;
	}

	/**
	 *@generated
	 */
	public void setCnorid(java.lang.Long value) {
		this.cnorid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCnortitle() {
		return this.cnortitle;
	}

	/**
	 *@generated
	 */
	public void setCnortitle(java.lang.String value) {
		this.cnortitle = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCnorname() {
		return this.cnorname;
	}

	/**
	 *@generated
	 */
	public void setCnorname(java.lang.String value) {
		this.cnorname = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getNotifyid() {
		return this.notifyid;
	}

	/**
	 *@generated
	 */
	public void setNotifyid(java.lang.Long value) {
		this.notifyid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getNotifytitle() {
		return this.notifytitle;
	}

	/**
	 *@generated
	 */
	public void setNotifytitle(java.lang.String value) {
		this.notifytitle = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getNotifyname() {
		return this.notifyname;
	}

	/**
	 *@generated
	 */
	public void setNotifyname(java.lang.String value) {
		this.notifyname = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getAgenid() {
		return this.agenid;
	}

	/**
	 *@generated
	 */
	public void setAgenid(java.lang.Long value) {
		this.agenid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getAgentitle() {
		return this.agentitle;
	}

	/**
	 *@generated
	 */
	public void setAgentitle(java.lang.String value) {
		this.agentitle = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getAgenname() {
		return this.agenname;
	}

	/**
	 *@generated
	 */
	public void setAgenname(java.lang.String value) {
		this.agenname = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getPretrans() {
		return this.pretrans;
	}

	/**
	 *@generated
	 */
	public void setPretrans(java.lang.String value) {
		this.pretrans = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getPoa() {
		return this.poa;
	}

	/**
	 *@generated
	 */
	public void setPoa(java.lang.String value) {
		this.poa = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getHbltype() {
		return this.hbltype;
	}

	/**
	 *@generated
	 */
	public void setHbltype(java.lang.String value) {
		this.hbltype = value;
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

	/**
	 *@generated
	 */
	public java.lang.Long getPddid() {
		return this.pddid;
	}

	/**
	 *@generated
	 */
	public void setPddid(java.lang.Long value) {
		this.pddid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getPdd() {
		return this.pdd;
	}

	/**
	 *@generated
	 */
	public void setPdd(java.lang.String value) {
		this.pdd = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getAtd() {
		return this.atd;
	}

	/**
	 *@generated
	 */
	public void setAtd(java.util.Date value) {
		this.atd = value;
	}

	public java.lang.String getBillcount() {
		return billcount;
	}

	public void setBillcount(java.lang.String billcount) {
		this.billcount = billcount;
	}

	public java.lang.String getPiece() {
		return piece;
	}

	public void setPiece(java.lang.String piece) {
		this.piece = piece;
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

	/**
	 *@generated
	 */
	public java.lang.String getCarryitem() {
		return this.carryitem;
	}

	/**
	 *@generated
	 */
	public void setCarryitem(java.lang.String value) {
		this.carryitem = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getFreightitem() {
		return this.freightitem;
	}

	/**
	 *@generated
	 */
	public void setFreightitem(java.lang.String value) {
		this.freightitem = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getLoaditem() {
		return this.loaditem;
	}

	/**
	 *@generated
	 */
	public void setLoaditem(java.lang.String value) {
		this.loaditem = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getPackid() {
		return this.packid;
	}

	/**
	 *@generated
	 */
	public void setPackid(java.lang.Long value) {
		this.packid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getMarksno() {
		return this.marksno;
	}

	/**
	 *@generated
	 */
	public void setMarksno(java.lang.String value) {
		this.marksno = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getGoodsdesc() {
		return this.goodsdesc;
	}

	/**
	 *@generated
	 */
	public void setGoodsdesc(java.lang.String value) {
		this.goodsdesc = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getTotledesc() {
		return this.totledesc;
	}

	/**
	 *@generated
	 */
	public void setTotledesc(java.lang.String value) {
		this.totledesc = value;
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

	public java.lang.Boolean getIsprintlock() {
		return isprintlock == null?false:isprintlock;
	}

	public void setIsprintlock(java.lang.Boolean isprintlock) {
		this.isprintlock = isprintlock;
	}

	public java.lang.Long getReportid() {
		return reportid;
	}

	public void setReportid(java.lang.Long reportid) {
		this.reportid = reportid;
	}

	public java.lang.String getMblno() {
		return mblno;
	}

	public void setMblno(java.lang.String mblno) {
		this.mblno = mblno;
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

	public java.lang.Boolean getIsmake() {
		return ismake;
	}

	public void setIsmake(java.lang.Boolean ismake) {
		this.ismake = ismake;
	}

	public java.util.Date getDatemake() {
		return datemake;
	}

	public void setDatemake(java.util.Date datemake) {
		this.datemake = datemake;
	}

	public java.lang.String getUsermake() {
		return usermake;
	}

	public void setUsermake(java.lang.String usermake) {
		this.usermake = usermake;
	}

	public java.lang.Boolean getIsadjust() {
		return isadjust;
	}

	public void setIsadjust(java.lang.Boolean isadjust) {
		this.isadjust = isadjust;
	}

	public java.util.Date getDateadjust() {
		return dateadjust;
	}

	public void setDateadjust(java.util.Date dateadjust) {
		this.dateadjust = dateadjust;
	}

	public java.lang.String getUseradjust() {
		return useradjust;
	}

	public void setUseradjust(java.lang.String useradjust) {
		this.useradjust = useradjust;
	}

	public java.lang.Boolean getIsconfirm() {
		return isconfirm;
	}

	public void setIsconfirm(java.lang.Boolean isconfirm) {
		this.isconfirm = isconfirm;
	}

	public java.util.Date getDateconfirm() {
		return dateconfirm;
	}

	public void setDateconfirm(java.util.Date dateconfirm) {
		this.dateconfirm = dateconfirm;
	}

	public java.lang.String getUserconfirm() {
		return userconfirm;
	}

	public void setUserconfirm(java.lang.String userconfirm) {
		this.userconfirm = userconfirm;
	}

	public java.lang.Boolean getIsprint() {
		return isprint;
	}

	public void setIsprint(java.lang.Boolean isprint) {
		this.isprint = isprint;
	}

	public java.util.Date getDateprint() {
		return dateprint;
	}

	public void setDateprint(java.util.Date dateprint) {
		this.dateprint = dateprint;
	}

	public java.lang.String getUserprint() {
		return userprint;
	}

	public void setUserprint(java.lang.String userprint) {
		this.userprint = userprint;
	}

	public java.lang.Boolean getIssign() {
		return issign;
	}

	public void setIssign(java.lang.Boolean issign) {
		this.issign = issign;
	}

	public java.util.Date getDatesign() {
		return datesign;
	}

	public void setDatesign(java.util.Date datesign) {
		this.datesign = datesign;
	}

	public java.lang.String getUsersign() {
		return usersign;
	}

	public void setUsersign(java.lang.String usersign) {
		this.usersign = usersign;
	}

	public java.lang.Boolean getIscomplete() {
		return iscomplete;
	}

	public void setIscomplete(java.lang.Boolean iscomplete) {
		this.iscomplete = iscomplete;
	}

	public java.util.Date getDatecomplete() {
		return datecomplete;
	}

	public void setDatecomplete(java.util.Date datecomplete) {
		this.datecomplete = datecomplete;
	}

	public java.lang.String getUsercomplete() {
		return usercomplete;
	}

	public void setUsercomplete(java.lang.String usercomplete) {
		this.usercomplete = usercomplete;
	}

	public java.lang.Boolean getIsshowship() {
		return isshowship;
	}

	public void setIsshowship(java.lang.Boolean isshowship) {
		this.isshowship = isshowship;
	}

	public java.lang.Boolean getIsdetail() {
		return isdetail;
	}

	public void setIsdetail(java.lang.Boolean isdetail) {
		this.isdetail = isdetail;
	}

	public java.lang.String getPaymentitem() {
		return paymentitem;
	}

	public void setPaymentitem(java.lang.String paymentitem) {
		this.paymentitem = paymentitem;
	}

	public java.lang.String getPayplace() {
		return payplace;
	}

	public void setPayplace(java.lang.String payplace) {
		this.payplace = payplace;
	}

	public java.lang.String getDestination() {
		return destination;
	}

	public void setDestination(java.lang.String destination) {
		this.destination = destination;
	}

	public java.lang.String getFormat() {
		return format;
	}

	public void setFormat(java.lang.String format) {
		this.format = format;
	}

	public java.lang.String getPolcode() {
		return polcode;
	}

	public void setPolcode(java.lang.String polcode) {
		this.polcode = polcode;
	}

	public java.lang.String getPddcode() {
		return pddcode;
	}

	public void setPddcode(java.lang.String pddcode) {
		this.pddcode = pddcode;
	}

	public java.lang.String getPodcode() {
		return podcode;
	}

	public void setPodcode(java.lang.String podcode) {
		this.podcode = podcode;
	}

	public java.lang.String getSignplace() {
		return signplace;
	}

	public void setSignplace(java.lang.String signplace) {
		this.signplace = signplace;
	}

	public java.lang.String getShcountrycode() {
		return shcountrycode;
	}

	public void setShcountrycode(java.lang.String shcountrycode) {
		this.shcountrycode = shcountrycode;
	}

	public java.lang.String getShenterprisecode() {
		return shenterprisecode;
	}

	public void setShenterprisecode(java.lang.String shenterprisecode) {
		this.shenterprisecode = shenterprisecode;
	}

	public java.lang.String getShcontacts() {
		return shcontacts;
	}

	public void setShcontacts(java.lang.String shcontacts) {
		this.shcontacts = shcontacts;
	}

	public java.lang.String getCocountrycode() {
		return cocountrycode;
	}

	public void setCocountrycode(java.lang.String cocountrycode) {
		this.cocountrycode = cocountrycode;
	}

	public java.lang.String getCoenterprisecode() {
		return coenterprisecode;
	}

	public void setCoenterprisecode(java.lang.String coenterprisecode) {
		this.coenterprisecode = coenterprisecode;
	}

	public java.lang.String getCocontacts() {
		return cocontacts;
	}

	public void setCocontacts(java.lang.String cocontacts) {
		this.cocontacts = cocontacts;
	}

	public java.lang.String getNocountrycode() {
		return nocountrycode;
	}

	public void setNocountrycode(java.lang.String nocountrycode) {
		this.nocountrycode = nocountrycode;
	}

	public java.lang.String getNoenterprisecode() {
		return noenterprisecode;
	}

	public void setNoenterprisecode(java.lang.String noenterprisecode) {
		this.noenterprisecode = noenterprisecode;
	}

	public java.lang.String getNocontacts() {
		return nocontacts;
	}

	public void setNocontacts(java.lang.String nocontacts) {
		this.nocontacts = nocontacts;
	}

	public java.lang.String getDestinationcode() {
		return destinationcode;
	}

	public void setDestinationcode(java.lang.String destinationcode) {
		this.destinationcode = destinationcode;
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

	public String getPutdesc() {
		return putdesc;
	}

	public void setPutdesc(String putdesc) {
		this.putdesc = putdesc;
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

	public String getHolddesc() {
		return holddesc;
	}

	public void setHolddesc(String holddesc) {
		this.holddesc = holddesc;
	}

	public java.util.Date getPuttime() {
		return puttime;
	}

	public void setPuttime(java.util.Date puttime) {
		this.puttime = puttime;
	}

	public java.util.Date getHoldtime() {
		return holdtime;
	}

	public void setHoldtime(java.util.Date holdtime) {
		this.holdtime = holdtime;
	}

	public java.lang.Boolean getIstelrel() {
		return istelrel;
	}

	public void setIstelrel(java.lang.Boolean istelrel) {
		this.istelrel = istelrel;
	}

	public java.util.Date getTelreltime() {
		return telreltime;
	}

	public void setTelreltime(java.util.Date telreltime) {
		this.telreltime = telreltime;
	}

	public java.lang.String getTelrelnos() {
		return telrelnos;
	}

	public void setTelrelnos(java.lang.String telrelnos) {
		this.telrelnos = telrelnos;
	}

	public java.lang.String getTelreler() {
		return telreler;
	}

	public void setTelreler(java.lang.String telreler) {
		this.telreler = telreler;
	}

	public java.lang.String getTelreltype() {
		return telreltype;
	}

	public void setTelreltype(java.lang.String telreltype) {
		this.telreltype = telreltype;
	}

	public java.lang.Long getTelrelrptid() {
		return telrelrptid;
	}

	public void setTelrelrptid(java.lang.Long telrelrptid) {
		this.telrelrptid = telrelrptid;
	}

	public java.lang.Long getTrainid() {
		return trainid;
	}

	public void setTrainid(java.lang.Long trainid) {
		this.trainid = trainid;
	}
	
}