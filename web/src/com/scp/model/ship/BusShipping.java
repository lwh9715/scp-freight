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
@Table(name = "bus_shipping")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusShipping implements java.io.Serializable {

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
	@Column(name = "jobid")
	private java.lang.Long jobid;

	/**
	 *@generated
	 */
	@Column(name = "nos", length = 30)
	private java.lang.String nos;

	/**
	 *@generated
	 */
	@Column(name = "dono", length = 30)
	private java.lang.String dono;

	@Column(name = "signplace")
	private java.lang.String signplace1;

	/**
	 *@generated
	 */
	@Column(name = "singtime", length = 29)
	private java.util.Date singtime;

	/**
	 *@generated
	 */
	@Column(name = "refno", length = 100)
	private java.lang.String refno;

	/**
	 *@generated
	 */
	@Column(name = "customerid")
	private java.lang.Long customerid;

	@Column(name = "goodstypeid")
	private java.lang.Long goodstypeid;

	/**
	 *@generated
	 */
	@Column(name = "customerabbr", length = 50)
	private java.lang.String customerabbr;

	/**
	 *@generated
	 */
	@Column(name = "custtel", length = 30)
	private java.lang.String custtel;

	/**
	 *@generated
	 */
	@Column(name = "custmobile", length = 30)
	private java.lang.String custmobile;

	/**
	 *@generated
	 */
	@Column(name = "custfax", length = 30)
	private java.lang.String custfax;

	/**
	 *@generated
	 */
	@Column(name = "custcontact", length = 50)
	private java.lang.String custcontact;

	/**
	 *@generated
	 */
	@Column(name = "vessel", length = 50)
	private java.lang.String vessel;

	@Column(name = "vescode", length = 30)
	private java.lang.String vescode;

	/**
	 *@generated
	 */
	@Column(name = "voyage", length = 20)
	private java.lang.String voyage;

	/**
	 *@generated
	 */
	@Column(name = "coupons", length = 100)
	private java.lang.String coupons;

	/**
	 *@generated
	 */
	@Column(name = "polid")
	private java.lang.Long polid;
	
	@Column(name = "potid")
	private java.lang.Long potid;

	/**
	 *@generated
	 */
	@Column(name = "pol", length = 50)
	private java.lang.String pol;
	
	@Column(name = "pot", length = 50)
	private java.lang.String pot;

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

	@Column(name = "paymentitem")
	private java.lang.String paymentitem;

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

	@Column(name = "polcode", length = 20)
	private java.lang.String polcode;
	@Column(name = "podcode", length = 20)
	private java.lang.String podcode;
	@Column(name = "pddcode", length = 20)
	private java.lang.String pddcode;
	
	@Column(name = "potcode", length = 20)
	private java.lang.String potcode;

	@Column(name = "destinationcode", length = 20)
	private java.lang.String destinationcode;

	/**
	 *@generated
	 */
	@Column(name = "poa")
	private java.lang.String poa;

	@Column(name = "poaname")
	private java.lang.String poaname;

	/**
	 *@generated
	 */
	@Column(name = "factoryinfo")
	private java.lang.String factoryinfo;

	@Column(name = "delnote")
	private java.lang.String delnote;

	/**
	 *@generated
	 */
	@Column(name = "carryitem", length = 15)
	private java.lang.String carryitem;

	/**
	 *@generated
	 */
	@Column(name = "carrierid")
	private java.lang.Long carrierid;

	/**
	 *@generated
	 */
	@Column(name = "scheduleid")
	private java.lang.Long scheduleid;

	/**
	 *@generated
	 */
	@Column(name = "agentid")
	private java.lang.Long agentid;

	@Column(name = "hblrpt")
	private java.lang.String hblrpt;

	@Column(name = "mblrpt")
	private java.lang.String mblrpt;

	@Column(name = "orderrpt")
	private java.lang.String orderrpt;

	@Column(name = "bookrpt")
	private java.lang.String bookrpt;

	@Column(name = "isshowship")
	private Boolean isshowship;

	@Column(name = "priceuserconfirm")
	private Boolean priceuserconfirm;

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

	@Column(name = "edt", length = 13)
	private java.util.Date edt;

	@Column(name = "awt", length = 13)
	private java.util.Date awt;

	/**
	 *@generated
	 */
	@Column(name = "atd", length = 13)
	private java.util.Date atd;

	@Column(name = "isput")
	private boolean isput;

	@Column(name = "puter", length = 30)
	private String puter;

	@Column(name = "putdesc")
	private String putdesc;

	@Column(name = "putstatus", updatable = false)
	private String putstatus;

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

	@Column(name = "payplace")
	private String payplace;

	@Column(name = "isdetail")
	private Boolean isdetail;

	@Column(name = "mbltype")
	private String mbltype;

	@Column(name = "piece")
	private String piece;

	@Column(name = "grswgt")
	private String grswgt;

	@Column(name = "cbm")
	private String cbm;

	@Column(name = "loaditem")
	private String loaditem;

	@Column(name = "packer")
	private String packer;

	@Column(name = "marksno")
	private String marksno;

	@Column(name = "goodsdesc")
	private String goodsdesc;

	@Column(name = "totledesc")
	private String totledesc;

	@Column(name = "bltype")
	private String bltype;

	@Column(name = "hblno")
	private String hblno;

	@Column(name = "billcount")
	private String billcount;

	@Column(name = "hbltype")
	private String hbltype;

	@Column(name = "sono")
	private String sono;

	@Column(name = "sodate")
	private java.util.Date sodate;

	@Column(name = "cnortitlembl")
	private String cnortitlembl;

	@Column(name = "cneetitlembl")
	private String cneetitlembl;

	@Column(name = "notifytitlembl")
	private String notifytitlembl;

	@Column(name = "istelrel")
	private java.lang.Boolean istelrel;

	@Column(name = "telreltime", length = 29)
	private java.util.Date telreltime;

	@Column(name = "istelrelback")
	private java.lang.Boolean istelrelback;

	@Column(name = "telrelbacktime", length = 29)
	private java.util.Date telrelbacktime;

	@Column(name = "telrelnos", length = 50)
	private java.lang.String telrelnos;

	@Column(name = "telreler", length = 30)
	private java.lang.String telreler;

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

	@Column(name = "ismakembl")
	private java.lang.Boolean ismakembl;

	@Column(name = "datemakembl", length = 29)
	private java.util.Date datemakembl;

	@Column(name = "usermakembl", length = 30)
	private java.lang.String usermakembl;

	@Column(name = "isadjustmbl")
	private java.lang.Boolean isadjustmbl;

	@Column(name = "dateadjustmbl", length = 29)
	private java.util.Date dateadjustmbl;

	@Column(name = "useradjustmbl", length = 30)
	private java.lang.String useradjustmbl;

	@Column(name = "isconfirmmbl")
	private java.lang.Boolean isconfirmmbl;

	@Column(name = "dateconfirmmbl", length = 29)
	private java.util.Date dateconfirmmbl;

	@Column(name = "userconfirmmbl", length = 30)
	private java.lang.String userconfirmmbl;

	@Column(name = "isprintmbl")
	private java.lang.Boolean isprintmbl;

	@Column(name = "dateprintmbl", length = 29)
	private java.util.Date dateprintmbl;

	@Column(name = "userprintmbl", length = 30)
	private java.lang.String userprintmbl;

	@Column(name = "isgetmbl")
	private java.lang.Boolean isgetmbl;

	@Column(name = "dategetmbl", length = 29)
	private java.util.Date dategetmbl;

	@Column(name = "usergetmbl", length = 30)
	private java.lang.String usergetmbl;

	@Column(name = "isreleasembl")
	private java.lang.Boolean isreleasembl;

	@Column(name = "datereleasembl", length = 29)
	private java.util.Date datereleasembl;

	@Column(name = "userreleasembl", length = 30)
	private java.lang.String userreleasembl;

	@Column(name = "signplacembl")
	private java.lang.String signplacembl;

	@Column(name = "onboarddate")
	private java.util.Date onboarddate;

	@Column(name = "routetyp", length = 1)
	private java.lang.String routetyp;

	@Column(name = "sidate")
	private java.util.Date sidate;

	@Column(name = "vgmdate")
	private java.util.Date vgmdate;

	@Column(name = "remark_booking")
	private String remark_booking;

	@Column(name = "clstime")
	private java.util.Date clstime;

	@Column(name = "priceuserid")
	private java.lang.Long priceuserid;

	/**
	 *@generated
	 */
	@Column(name = "mblterminal", length = 100)
	private java.lang.String mblterminal;

	@Column(name = "mblcyopen")
	private java.util.Date mblcyopen;

	/**
	 *@generated
	 */
	@Column(name = "linecode", length = 100)
	private java.lang.String linecode;

	@Column(name = "trucktype", length = 1)
	private java.lang.String trucktype;

	@Column(name = "custype", length = 1)
	private java.lang.String custype;
	
	@Column(name = "cusclass", length = 1)
	private java.lang.String cusclass;

	@Column(name = "vgmtype", length = 1)
	private java.lang.String vgmtype;

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

	@Column(name = "telreltype ", length = 1)
	private java.lang.String telreltype;

	@Column(name = "telrelrptid")
	private java.lang.Long telrelrptid;

	@Column(name = "agenidmbl")
	private java.lang.Long agenidmbl;

	@Column(name = "agennamembl")
	private String agennamembl;

	@Column(name = "agentitlembl")
	private String agentitlembl;
	
	@Column(name = "solist")
	private java.lang.String solist;
	
	@Column(name = "blremarks")
	private java.lang.String blremarks;
	
	
	@Column(name = "busstatus")
	private java.lang.String busstatus;
	
	
	@Column(name = "isreleasecnt")
	private java.lang.Boolean isreleasecnt;
	
	@Column(name = "releasecnttime", length = 29)
	private java.util.Date releasecnttime;
	
	@Column(name = "ispickcnt")
	private java.lang.Boolean ispickcnt;
	
	@Column(name = "pickcnttime", length = 29)
	private java.util.Date pickcnttime;
	
	@Column(name = "isreturncnt")
	private java.lang.Boolean isreturncnt;
	
	@Column(name = "returncnttime", length = 29)
	private java.util.Date returncnttime;
	
	@Column(name = "channelid")
	private java.lang.Long channelid;
	
	@Column(name = "nameaccount")
	private java.lang.String nameaccount;
	
	public java.lang.Long getPriceuserid() {
		return priceuserid;
	}

	public void setPriceuserid(java.lang.Long priceuserid) {
		this.priceuserid = priceuserid;
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

	public java.util.Date getAwt() {
		return awt;
	}

	public void setAwt(java.util.Date awt) {
		this.awt = awt;
	}

	public java.lang.String getCoupons() {
		return coupons;
	}

	public void setCoupons(java.lang.String coupons) {
		this.coupons = coupons;
	}

	/**
	 *@generated
	 */
	@Column(name = "ata", length = 13)
	private java.util.Date ata;

	public java.lang.String getDelnote() {
		return delnote;
	}

	public void setDelnote(java.lang.String delnote) {
		this.delnote = delnote;
	}

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
	 *@generatede
	 */
	@Column(name = "schedule_week")
	private java.lang.Integer scheduleWeek;

	@Column(name = "orderdate", length = 29)
	private java.util.Date orderdate;

	@Column(name = "cabinetdate", length = 29)
	private java.util.Date cabinetdate;



	/**
	 *@generated
	 */
	@Column(name = "remark1")
	private java.lang.String remark1;
	/**
	 *@generated
	 */
	@Column(name = "remark2")
	private java.lang.String remark2;
	/**
	 *@generated
	 */
	@Column(name = "remark3")
	private java.lang.String remark3;
	/**
	 *@generated
	 */
	@Column(name = "remark4")
	private java.lang.String remark4;
	/**
	 *@generated
	 */
	@Column(name = "remark5")
	private java.lang.String remark5;

	/**
	 *@generated
	 */
	@Column(name = "claim_pre")
	private java.lang.String claimPre;

	/**
	 *@generated
	 */
	@Column(name = "claim_truck")
	private java.lang.String claimTruck;

	/**
	 *@generated
	 */
	@Column(name = "claim_bill")
	private java.lang.String claimBill;

	/**
	 *@generated
	 */
	@Column(name = "claim_clear")
	private java.lang.String claimClear;

	/**
	 *@generated
	 */
	@Column(name = "impexp", length = 1)
	private java.lang.String impexp;

	/**
	 *@generated
	 */
	@Column(name = "ldtype", length = 1)
	private java.lang.String ldtype;

	/**
	 *@generated
	 */
	@Column(name = "coltype", length = 1)
	private java.lang.String coltype;

	/**
	 *@generated
	 */
	@Column(name = "tradetype", length = 1)
	private java.lang.String tradetype;

	/**
	 *@generated
	 */
	@Column(name = "corpid")
	private java.lang.Long corpid;

	/**
	 *@generated
	 */
	@Column(name = "linkid")
	private java.lang.Long linkid;

	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	@Column(name = "isjoin")
	private java.lang.Boolean isjoin;

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
	@Column(name = "warehouseid")
	private java.lang.Long warehouseid;

	// /**
	// *@generated
	// */
	// @Column(name = "nextshipcount")
	// private java.lang.Integer nextshipcount;
	//	
	// /**
	// *@generated
	// */
	// @Column(name = "nextshipremarks")
	// private java.lang.String nextshipremarks;
	//	
	// /**
	// *@generated
	// */
	// @Column(name = "dropcntcount")
	// private java.lang.Integer dropcntcount;
	//	
	// /**
	// *@generated
	// */
	// @Column(name = "dropcntremarks")
	// private java.lang.String dropcntremarks;

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
	@Column(name = "claim_doc")
	private java.lang.String claim_doc;

	/**
	 *@generated
	 */
	@Column(name = "mblno", length = 200)
	private java.lang.String mblno;

	/**
	 *@generated
	 */
	@Column(name = "ismbl")
	private java.lang.Boolean ismbl;

	/**
	 *@generated
	 */
	@Column(name = "ishbl")
	private java.lang.Boolean ishbl;

	@Column(name = "isinsurance")
	private java.lang.Boolean isinsurance;

	@Column(name = "declareval")
	private java.math.BigDecimal declareval;

	@Column(name = "declarepiece")
	private java.lang.Long declarepiece;

	@Column(name = "declarewgt")
	private java.math.BigDecimal declarewgt;

	@Column(name = "declaresign")
	private java.lang.String declaresign;

	/**
	 *@generated
	 */
	@Column(name = "cnortitlemblid")
	private java.lang.Long cnortitlemblid;

	/**
	 *@generated
	 */
	@Column(name = "cneetitlembid")
	private java.lang.Long cneetitlembid;

	/**
	 *@generated
	 */
	@Column(name = "notifytitlemblid")
	private java.lang.Long notifytitlemblid;

	/**
	 *@generated
	 */
	@Column(name = "cnortitlemblname")
	private java.lang.String cnortitlemblname;

	/**
	 *@generated
	 */
	@Column(name = "cneetitlemblname")
	private java.lang.String cneetitlemblname;

	/**
	 *@generated
	 */
	@Column(name = "notifytitlemblname")
	private java.lang.String notifytitlemblname;

	/**
	 *@generated
	 */
	@Column(name = "contractid")
	private java.lang.Long contractid;

	@Column(name = "contractno", length = 30)
	private java.lang.String contractno;

	/**
	 *@generated
	 */
	@Column(name = "isreleasemblwo")
	private java.lang.Boolean isreleasemblwo;

	/**
	 *@generated
	 */
	@Column(name = "isreleasehblwo")
	private java.lang.Boolean isreleasehblwo;

	@Column(name = "ispecialprice", length = 1)
	private java.lang.String ispecialprice;

	@Column(name = "dategatein", length = 13)
	private java.util.Date dategatein;
	
	@Column(name = "carrieresi", length = 1)
	private java.lang.String carrieresi;
	
	@Column(name = "pono")
	private java.lang.String pono;

	@Column(name = "isexternalmatching")
	private java.lang.String isexternalmatching;

	public java.lang.String getIsexternalmatching() {
		return isexternalmatching;
	}

	public void setIsexternalmatching(java.lang.String isexternalmatching) {
		this.isexternalmatching = isexternalmatching;
	}

	public java.lang.Boolean getIsinsurance() {
		return isinsurance;
	}

	public void setIsinsurance(java.lang.Boolean isinsurance) {
		this.isinsurance = isinsurance;
	}

	public java.math.BigDecimal getDeclareval() {
		return declareval;
	}

	public void setDeclareval(java.math.BigDecimal declareval) {
		this.declareval = declareval;
	}

	public java.lang.Long getDeclarepiece() {
		return declarepiece;
	}

	public void setDeclarepiece(java.lang.Long declarepiece) {
		this.declarepiece = declarepiece;
	}

	public java.math.BigDecimal getDeclarewgt() {
		return declarewgt;
	}

	public void setDeclarewgt(java.math.BigDecimal declarewgt) {
		this.declarewgt = declarewgt;
	}

	public java.lang.String getDeclaresign() {
		return declaresign;
	}

	public void setDeclaresign(java.lang.String declaresign) {
		this.declaresign = declaresign;
	}

	/**
	 *@generated
	 */
	@Column(name = "mblcorpid")
	private java.lang.Long mblcorpid;

	/**
	 *@generated
	 */
	@Column(name = "freightitem", length = 4)
	private java.lang.String freightitem;

	@Column(name = "destination")
	private java.lang.String destination;

	@Column(name = "pretrans")
	private java.lang.String pretrans;

	/**
	 *@generated
	 */
	@Column(name = "transtype", length = 1)
	private java.lang.String transtype;

	@Column(name = "paymenterms", length = 1)
	private java.lang.String paymenterms;

	/**
	 *@generated
	 */
	@Column(name = "routecode", length = 20)
	private java.lang.String routecode;

	@Column(name = "vescodembl", length = 30)
	private java.lang.String vescodembl;
	@Column(name = "vesselmbl", length = 50)
	private java.lang.String vesselmbl;
	@Column(name = "voyagembl", length = 20)
	private java.lang.String voyagembl;
	@Column(name = "mblnombl", length = 200)
	private java.lang.String mblnombl;
	@Column(name = "freightitemmbl", length = 4)
	private java.lang.String freightitemmbl;
	@Column(name = "carrieridmbl")
	private java.lang.Long carrieridmbl;
	@Column(name = "carryitemmbl", length = 15)
	private java.lang.String carryitemmbl;
	@Column(name = "mblpolcode", length = 20)
	private java.lang.String mblpolcode;
	@Column(name = "mblpodcode", length = 20)
	private java.lang.String mblpodcode;
	@Column(name = "mblpddcode", length = 20)
	private java.lang.String mblpddcode;
	@Column(name = "mbldestinationcode", length = 20)
	private java.lang.String mbldestinationcode;
	@Column(name = "mblpol", length = 50)
	private java.lang.String mblpol;
	@Column(name = "mblpod", length = 50)
	private java.lang.String mblpod;
	@Column(name = "mblpdd", length = 50)
	private java.lang.String mblpdd;
	@Column(name = "mbldestination")
	private java.lang.String mbldestination;
	@Column(name = "etambl", length = 13)
	private java.util.Date etambl;
	@Column(name = "etdmbl", length = 13)
	private java.util.Date etdmbl;
	@Column(name = "marksnombl")
	private String marksnombl;
	@Column(name = "goodsdescmbl")
	private String goodsdescmbl;
	@Column(name = "bargepol", length = 50)
	private java.lang.String bargepol;
	@Column(name = "bargecls")
	private java.util.Date bargecls;
	@Column(name = "bargeetd")
	private java.util.Date bargeetd;
	@Column(name = "bargeeta")
	private java.util.Date bargeeta;

	@Column(name = "remarksreleasebill")
	private java.lang.String remarksreleasebill;

	@Column(name = "vgmstatus", length = 1)
	private java.lang.String vgmstatus;
	
	@Column(name = "hscode", length = 10)
	private java.lang.String hscode;
	
	@Column(name = "notifyid2")
	private java.lang.Long notifyid2;

	@Column(name = "notifytitle2")
	private java.lang.String notifytitle2;

	@Column(name = "notifyname2")
	private java.lang.String notifyname2;
	
	@Column(name = "waybillno")
	private java.lang.String waybillno;
	
	@Column(name = "isunder")
	private boolean isunder;
	
	@Column(name = "under", length = 30)
	private java.lang.String under;
	
	@Column(name = "undertime")
	private java.util.Date undertime;
	
	
	@Column(name = "wmstype")
	private java.lang.String wmstype;
	
	@Column(name = "wmsid")
	private java.lang.Long wmsid;
	
	
	@Column(name = "other1")
	private java.lang.String other1;
	
	@Column(name = "other2")
	private java.lang.String other2;
	
	@Column(name = "other3")
	private java.lang.String other3;
	
	@Column(name = "other4")
	private java.lang.String other4;
	
	@Column(name = "other5")
	private java.lang.String other5;
	
	@Column(name = "other6")
	private java.lang.String other6;
	
	@Column(name = "other7")
	private java.lang.String other7;
	
	@Column(name = "other8")
	private java.lang.String other8;
	
	@Column(name = "other9")
	private java.lang.String other9;
	
	@Column(name = "other10")
	private java.lang.String other10;
	
	@Column(name = "other11")
	private java.lang.String other11;
	
	@Column(name = "other12")
	private java.lang.String other12;
	
	@Column(name = "shipagent")
	private java.lang.String shipagent;
	
	@Column(name = "shiprefno")
	private java.lang.String shiprefno;
	
	public boolean isIsunder() {
		return isunder;
	}

	public void setIsunder(boolean isunder) {
		this.isunder = isunder;
	}

	public java.lang.String getUnder() {
		return under;
	}

	public void setUnder(java.lang.String under) {
		this.under = under;
	}

	public java.util.Date getUndertime() {
		return undertime;
	}

	public void setUndertime(java.util.Date undertime) {
		this.undertime = undertime;
	}

	public java.lang.String getPaymenterms() {
		return paymenterms;
	}

	public void setPaymenterms(java.lang.String paymenterms) {
		this.paymenterms = paymenterms;
	}

	@Column(name = "agentdesid ")
	private java.lang.Long agentdesid;

	public java.lang.Long getAgentdesid() {
		return agentdesid;
	}

	public void setAgentdesid(java.lang.Long agentdesid) {
		this.agentdesid = agentdesid;
	}

	public java.lang.String getFreightitem() {
		return freightitem;
	}

	public void setFreightitem(java.lang.String freightitem) {
		this.freightitem = freightitem;
	}

	public java.util.Date getEdt() {
		return edt;
	}

	public void setEdt(java.util.Date edt) {
		this.edt = edt;
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
	public java.lang.String getDono() {
		return this.dono;
	}

	/**
	 *@generated
	 */
	public void setDono(java.lang.String value) {
		this.dono = value;
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
	public java.lang.String getRefno() {
		return this.refno;
	}

	/**
	 *@generated
	 */
	public void setRefno(java.lang.String value) {
		this.refno = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getCustomerid() {
		return this.customerid;
	}

	/**
	 *@generated
	 */
	public void setCustomerid(java.lang.Long value) {
		this.customerid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCustomerabbr() {
		return this.customerabbr;
	}

	/**
	 *@generated
	 */
	public void setCustomerabbr(java.lang.String value) {
		this.customerabbr = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCusttel() {
		return this.custtel;
	}

	/**
	 *@generated
	 */
	public void setCusttel(java.lang.String value) {
		this.custtel = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCustmobile() {
		return this.custmobile;
	}

	/**
	 *@generated
	 */
	public void setCustmobile(java.lang.String value) {
		this.custmobile = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCustfax() {
		return this.custfax;
	}

	/**
	 *@generated
	 */
	public void setCustfax(java.lang.String value) {
		this.custfax = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCustcontact() {
		return this.custcontact;
	}

	/**
	 *@generated
	 */
	public void setCustcontact(java.lang.String value) {
		this.custcontact = value;
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

	public java.lang.Long getLinkid() {
		return linkid;
	}

	public void setLinkid(java.lang.Long linkid) {
		this.linkid = linkid;
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
	public java.lang.String getFactoryinfo() {
		return this.factoryinfo;
	}

	/**
	 *@generated
	 */
	public void setFactoryinfo(java.lang.String value) {
		this.factoryinfo = value;
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
	public java.lang.Long getAgentid() {
		return this.agentid;
	}

	/**
	 *@generated
	 */
	public void setAgentid(java.lang.Long value) {
		this.agentid = value;
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
	public java.util.Date getAtd() {
		return this.atd;
	}

	/**
	 *@generated
	 */
	public void setAtd(java.util.Date value) {
		this.atd = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getAta() {
		return this.ata;
	}

	/**
	 *@generated
	 */
	public void setAta(java.util.Date value) {
		this.ata = value;
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
	public java.lang.String getRemark1() {
		return this.remark1;
	}

	/**
	 *@generated
	 */
	public void setRemark1(java.lang.String value) {
		this.remark1 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getRemark2() {
		return this.remark2;
	}

	/**
	 *@generated
	 */
	public void setRemark2(java.lang.String value) {
		this.remark2 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getRemark3() {
		return this.remark3;
	}

	/**
	 *@generated
	 */
	public void setRemark3(java.lang.String value) {
		this.remark3 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getRemark4() {
		return this.remark4;
	}

	/**
	 *@generated
	 */
	public void setRemark4(java.lang.String value) {
		this.remark4 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getRemark5() {
		return this.remark5;
	}

	/**
	 *@generated
	 */
	public void setRemark5(java.lang.String value) {
		this.remark5 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getClaimPre() {
		return this.claimPre;
	}

	/**
	 *@generated
	 */
	public void setClaimPre(java.lang.String value) {
		this.claimPre = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getClaimTruck() {
		return this.claimTruck;
	}

	/**
	 *@generated
	 */
	public void setClaimTruck(java.lang.String value) {
		this.claimTruck = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getClaimBill() {
		return this.claimBill;
	}

	/**
	 *@generated
	 */
	public void setClaimBill(java.lang.String value) {
		this.claimBill = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getClaimClear() {
		return this.claimClear;
	}

	/**
	 *@generated
	 */
	public void setClaimClear(java.lang.String value) {
		this.claimClear = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getImpexp() {
		return this.impexp;
	}

	/**
	 *@generated
	 */
	public void setImpexp(java.lang.String value) {
		this.impexp = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getLdtype() {
		return this.ldtype;
	}

	/**
	 *@generated
	 */
	public void setLdtype(java.lang.String value) {
		this.ldtype = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getColtype() {
		return this.coltype;
	}

	/**
	 *@generated
	 */
	public void setColtype(java.lang.String value) {
		this.coltype = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getTradetype() {
		return this.tradetype;
	}

	/**
	 *@generated
	 */
	public void setTradetype(java.lang.String value) {
		this.tradetype = value;
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

	public java.lang.Boolean getIsjoin() {
		return isjoin;
	}

	public void setIsjoin(java.lang.Boolean isjoin) {
		this.isjoin = isjoin;
	}

	public java.lang.String getSignplace() {
		return signplace1;
	}

	public void setSignplace(java.lang.String signplace) {
		this.signplace1 = signplace;
	}

	public java.lang.String getSignplace1() {
		return signplace1;
	}

	public void setSignplace1(java.lang.String signplace1) {
		this.signplace1 = signplace1;
	}

	public java.lang.Integer getScheduleWeek() {
		return scheduleWeek;
	}

	public void setScheduleWeek(java.lang.Integer scheduleWeek) {
		this.scheduleWeek = scheduleWeek;
	}

	public java.lang.Long getScheduleid() {
		return scheduleid;
	}

	public void setScheduleid(java.lang.Long scheduleid) {
		this.scheduleid = scheduleid;
	}

	/*
	 * public java.lang.Integer getNextshipcount() { return nextshipcount; }
	 * 
	 * public void setNextshipcount(java.lang.Integer nextshipcount) {
	 * this.nextshipcount = nextshipcount; }
	 * 
	 * public java.lang.String getNextshipremarks() { return nextshipremarks; }
	 * 
	 * public void setNextshipremarks(java.lang.String nextshipremarks) {
	 * this.nextshipremarks = nextshipremarks; }
	 * 
	 * public java.lang.Integer getDropcntcount() { return dropcntcount; }
	 * 
	 * public void setDropcntcount(java.lang.Integer dropcntcount) {
	 * this.dropcntcount = dropcntcount; }
	 * 
	 * public java.lang.String getDropcntremarks() { return dropcntremarks; }
	 * 
	 * public void setDropcntremarks(java.lang.String dropcntremarks) {
	 * this.dropcntremarks = dropcntremarks; }
	 */

	public java.lang.Long getCneeid() {
		return cneeid;
	}

	public java.lang.String getCneetitle() {
		return cneetitle;
	}

	public java.lang.String getCneename() {
		return cneename;
	}

	public java.lang.Long getCnorid() {
		return cnorid;
	}

	public java.lang.String getCnortitle() {
		return cnortitle;
	}

	public java.lang.String getCnorname() {
		return cnorname;
	}

	public java.lang.Long getNotifyid() {
		return notifyid;
	}

	public java.lang.String getNotifytitle() {
		return notifytitle;
	}

	public java.lang.String getNotifyname() {
		return notifyname;
	}

	public java.lang.Long getAgenid() {
		return agenid;
	}

	public java.lang.String getAgentitle() {
		return agentitle;
	}

	public java.lang.String getAgenname() {
		return agenname;
	}

	public java.lang.String getClaim_doc() {
		return claim_doc;
	}

	public void setCneeid(java.lang.Long cneeid) {
		this.cneeid = cneeid;
	}

	public void setCneetitle(java.lang.String cneetitle) {
		this.cneetitle = cneetitle;
	}

	public void setCneename(java.lang.String cneename) {
		this.cneename = cneename;
	}

	public void setCnorid(java.lang.Long cnorid) {
		this.cnorid = cnorid;
	}

	public void setCnortitle(java.lang.String cnortitle) {
		this.cnortitle = cnortitle;
	}

	public void setCnorname(java.lang.String cnorname) {
		this.cnorname = cnorname;
	}

	public void setNotifyid(java.lang.Long notifyid) {
		this.notifyid = notifyid;
	}

	public void setNotifytitle(java.lang.String notifytitle) {
		this.notifytitle = notifytitle;
	}

	public void setNotifyname(java.lang.String notifyname) {
		this.notifyname = notifyname;
	}

	public void setAgenid(java.lang.Long agenid) {
		this.agenid = agenid;
	}

	public void setAgentitle(java.lang.String agentitle) {
		this.agentitle = agentitle;
	}

	public void setAgenname(java.lang.String agenname) {
		this.agenname = agenname;
	}

	public void setClaim_doc(java.lang.String claimDoc) {
		claim_doc = claimDoc;
	}

	public java.lang.Long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(java.lang.Long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public java.lang.String getMblno() {
		return mblno;
	}

	public void setMblno(java.lang.String mblno) {
		this.mblno = mblno;
	}

	public java.lang.Boolean getIsmbl() {
		return ismbl;
	}

	public void setIsmbl(java.lang.Boolean ismbl) {
		this.ismbl = ismbl;
	}

	public java.lang.Boolean getIshbl() {
		return ishbl;
	}

	public void setIshbl(java.lang.Boolean ishbl) {
		this.ishbl = ishbl;
	}

	public java.lang.Long getMblcorpid() {
		return mblcorpid;
	}

	public void setMblcorpid(java.lang.Long mblcorpid) {
		this.mblcorpid = mblcorpid;
	}

	public java.lang.String getTranstype() {
		return transtype;
	}

	public void setTranstype(java.lang.String transtype) {
		this.transtype = transtype;
	}

	public Boolean getIsdetail() {
		return isdetail;
	}

	public void setIsdetail(Boolean isdetail) {
		this.isdetail = isdetail;
	}

	public String getMbltype() {
		return mbltype;
	}

	public void setMbltype(String mbltype) {
		this.mbltype = mbltype;
	}

	public String getPiece() {
		return piece;
	}

	public void setPiece(String piece) {
		this.piece = piece;
	}

	public String getGrswgt() {
		return grswgt;
	}

	public void setGrswgt(String grswgt) {
		this.grswgt = grswgt;
	}

	public String getCbm() {
		return cbm;
	}

	public void setCbm(String cbm) {
		this.cbm = cbm;
	}

	public String getLoaditem() {
		return loaditem;
	}

	public void setLoaditem(String loaditem) {
		this.loaditem = loaditem;
	}

	public String getPacker() {
		return packer;
	}

	public void setPacker(String packer) {
		this.packer = packer;
	}

	public String getMarksno() {
		return marksno;
	}

	public void setMarksno(String marksno) {
		this.marksno = marksno;
	}

	public String getGoodsdesc() {
		return goodsdesc;
	}

	public void setGoodsdesc(String goodsdesc) {
		this.goodsdesc = goodsdesc;
	}

	public String getTotledesc() {
		return totledesc;
	}

	public void setTotledesc(String totledesc) {
		this.totledesc = totledesc;
	}

	public String getBltype() {
		return bltype;
	}

	public void setBltype(String bltype) {
		this.bltype = bltype;
	}

	public String getHblno() {
		return hblno;
	}

	public void setHblno(String hblno) {
		this.hblno = hblno;
	}

	public String getBillcount() {
		return billcount;
	}

	public void setBillcount(String billcount) {
		this.billcount = billcount;
	}

	public String getHbltype() {
		return hbltype;
	}

	public void setHbltype(String hbltype) {
		this.hbltype = hbltype;
	}

	public String getSono() {
		return sono;
	}

	public void setSono(String sono) {
		this.sono = sono;
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

	public java.lang.Boolean getIsmakembl() {
		return ismakembl;
	}

	public void setIsmakembl(java.lang.Boolean ismakembl) {
		this.ismakembl = ismakembl;
	}

	public java.util.Date getDatemakembl() {
		return datemakembl;
	}

	public void setDatemakembl(java.util.Date datemakembl) {
		this.datemakembl = datemakembl;
	}

	public java.lang.String getUsermakembl() {
		return usermakembl;
	}

	public void setUsermakembl(java.lang.String usermakembl) {
		this.usermakembl = usermakembl;
	}

	public java.lang.Boolean getIsadjustmbl() {
		return isadjustmbl;
	}

	public void setIsadjustmbl(java.lang.Boolean isadjustmbl) {
		this.isadjustmbl = isadjustmbl;
	}

	public java.util.Date getDateadjustmbl() {
		return dateadjustmbl;
	}

	public void setDateadjustmbl(java.util.Date dateadjustmbl) {
		this.dateadjustmbl = dateadjustmbl;
	}

	public java.lang.String getUseradjustmbl() {
		return useradjustmbl;
	}

	public void setUseradjustmbl(java.lang.String useradjustmbl) {
		this.useradjustmbl = useradjustmbl;
	}

	public java.lang.Boolean getIsconfirmmbl() {
		return isconfirmmbl;
	}

	public void setIsconfirmmbl(java.lang.Boolean isconfirmmbl) {
		this.isconfirmmbl = isconfirmmbl;
	}

	public java.util.Date getDateconfirmmbl() {
		return dateconfirmmbl;
	}

	public void setDateconfirmmbl(java.util.Date dateconfirmmbl) {
		this.dateconfirmmbl = dateconfirmmbl;
	}

	public java.lang.String getUserconfirmmbl() {
		return userconfirmmbl;
	}

	public void setUserconfirmmbl(java.lang.String userconfirmmbl) {
		this.userconfirmmbl = userconfirmmbl;
	}

	public java.lang.Boolean getIsprintmbl() {
		return isprintmbl;
	}

	public void setIsprintmbl(java.lang.Boolean isprintmbl) {
		this.isprintmbl = isprintmbl;
	}

	public java.util.Date getDateprintmbl() {
		return dateprintmbl;
	}

	public void setDateprintmbl(java.util.Date dateprintmbl) {
		this.dateprintmbl = dateprintmbl;
	}

	public java.lang.String getUserprintmbl() {
		return userprintmbl;
	}

	public void setUserprintmbl(java.lang.String userprintmbl) {
		this.userprintmbl = userprintmbl;
	}

	public java.lang.Boolean getIsgetmbl() {
		return isgetmbl;
	}

	public void setIsgetmbl(java.lang.Boolean isgetmbl) {
		this.isgetmbl = isgetmbl;
	}

	public java.util.Date getDategetmbl() {
		return dategetmbl;
	}

	public void setDategetmbl(java.util.Date dategetmbl) {
		this.dategetmbl = dategetmbl;
	}

	public java.lang.String getUsergetmbl() {
		return usergetmbl;
	}

	public void setUsergetmbl(java.lang.String usergetmbl) {
		this.usergetmbl = usergetmbl;
	}

	public java.lang.Boolean getIsreleasembl() {
		return isreleasembl;
	}

	public void setIsreleasembl(java.lang.Boolean isreleasembl) {
		this.isreleasembl = isreleasembl;
	}

	public java.util.Date getDatereleasembl() {
		return datereleasembl;
	}

	public void setDatereleasembl(java.util.Date datereleasembl) {
		this.datereleasembl = datereleasembl;
	}

	public java.lang.String getUserreleasembl() {
		return userreleasembl;
	}

	public void setUserreleasembl(java.lang.String userreleasembl) {
		this.userreleasembl = userreleasembl;
	}

	public java.lang.String getPaymentitem() {
		return paymentitem;
	}

	public void setPaymentitem(java.lang.String paymentitem) {
		this.paymentitem = paymentitem;
	}

	public String getPayplace() {
		return payplace;
	}

	public void setPayplace(String payplace) {
		this.payplace = payplace;
	}

	public String getCnortitlembl() {
		return cnortitlembl;
	}

	public void setCnortitlembl(String cnortitlembl) {
		this.cnortitlembl = cnortitlembl;
	}

	public String getCneetitlembl() {
		return cneetitlembl;
	}




	public void setCneetitlembl(String cneetitlembl) {
		this.cneetitlembl = cneetitlembl;
	}

	public String getNotifytitlembl() {
		return notifytitlembl;
	}

	public void setNotifytitlembl(String notifytitlembl) {
		this.notifytitlembl = notifytitlembl;
	}

	public java.lang.String getHblrpt() {
		return hblrpt;
	}

	public void setHblrpt(java.lang.String hblrpt) {
		this.hblrpt = hblrpt;
	}

	public java.lang.String getMblrpt() {
		return mblrpt;
	}

	public void setMblrpt(java.lang.String mblrpt) {
		this.mblrpt = mblrpt;
	}

	public java.lang.String getOrderrpt() {
		return orderrpt;
	}

	public void setOrderrpt(java.lang.String orderrpt) {
		this.orderrpt = orderrpt;
	}

	public java.lang.String getBookrpt() {
		return bookrpt;
	}

	public void setBookrpt(java.lang.String bookrpt) {
		this.bookrpt = bookrpt;
	}

	public java.lang.String getDestination() {
		return destination;
	}

	public void setDestination(java.lang.String destination) {
		this.destination = destination;
	}

	public java.lang.String getPretrans() {
		return pretrans;
	}

	public void setPretrans(java.lang.String pretrans) {
		this.pretrans = pretrans;
	}

	public Boolean getIsshowship() {
		return isshowship;
	}

	public void setIsshowship(Boolean isshowship) {
		this.isshowship = isshowship;
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

	public java.lang.Boolean getIstelrelback() {
		return istelrelback;
	}

	public void setIstelrelback(java.lang.Boolean istelrelback) {
		this.istelrelback = istelrelback;
	}

	public java.util.Date getTelrelbacktime() {
		return telrelbacktime;
	}

	public void setTelrelbacktime(java.util.Date telrelbacktime) {
		this.telrelbacktime = telrelbacktime;
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

	public java.lang.Long getCnortitlemblid() {
		return cnortitlemblid;
	}

	public void setCnortitlemblid(java.lang.Long cnortitlemblid) {
		this.cnortitlemblid = cnortitlemblid;
	}

	public java.lang.Long getCneetitlembid() {
		return cneetitlembid;
	}

	public void setCneetitlembid(java.lang.Long cneetitlembid) {
		this.cneetitlembid = cneetitlembid;
	}

	public java.lang.Long getNotifytitlemblid() {
		return notifytitlemblid;
	}

	public void setNotifytitlemblid(java.lang.Long notifytitlemblid) {
		this.notifytitlemblid = notifytitlemblid;
	}

	public java.lang.String getCnortitlemblname() {
		return cnortitlemblname;
	}

	public void setCnortitlemblname(java.lang.String cnortitlemblname) {
		this.cnortitlemblname = cnortitlemblname;
	}

	public java.lang.String getCneetitlemblname() {
		return cneetitlemblname;
	}

	public void setCneetitlemblname(java.lang.String cneetitlemblname) {
		this.cneetitlemblname = cneetitlemblname;
	}

	public java.lang.String getNotifytitlemblname() {
		return notifytitlemblname;
	}

	public void setNotifytitlemblname(java.lang.String notifytitlemblname) {
		this.notifytitlemblname = notifytitlemblname;
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

	public java.lang.String getPddcode() {
		return pddcode;
	}

	public void setPddcode(java.lang.String pddcode) {
		this.pddcode = pddcode;
	}

	public java.lang.String getDestinationcode() {
		return destinationcode;
	}

	public void setDestinationcode(java.lang.String destinationcode) {
		this.destinationcode = destinationcode;
	}

	public java.lang.String getVescode() {
		return vescode;
	}

	public void setVescode(java.lang.String vescode) {
		this.vescode = vescode;
	}

	public java.util.Date getSodate() {
		return sodate;
	}

	public void setSodate(java.util.Date sodate) {
		this.sodate = sodate;
	}

	public java.lang.Long getContractid() {
		return contractid;
	}

	public void setContractid(java.lang.Long contractid) {
		this.contractid = contractid;
	}

	public java.lang.String getContractno() {
		return contractno;
	}

	public void setContractno(java.lang.String contractno) {
		this.contractno = contractno;
	}

	public java.lang.String getRoutecode() {
		return routecode;
	}

	public void setRoutecode(java.lang.String routecode) {
		this.routecode = routecode;
	}

	public java.lang.String getVescodembl() {
		return vescodembl;
	}

	public void setVescodembl(java.lang.String vescodembl) {
		this.vescodembl = vescodembl;
	}

	public java.lang.String getVesselmbl() {
		return vesselmbl;
	}

	public void setVesselmbl(java.lang.String vesselmbl) {
		this.vesselmbl = vesselmbl;
	}

	public java.lang.String getVoyagembl() {
		return voyagembl;
	}

	public void setVoyagembl(java.lang.String voyagembl) {
		this.voyagembl = voyagembl;
	}

	public java.lang.String getMblnombl() {
		return mblnombl;
	}

	public void setMblnombl(java.lang.String mblnombl) {
		this.mblnombl = mblnombl;
	}

	public java.lang.String getFreightitemmbl() {
		return freightitemmbl;
	}

	public void setFreightitemmbl(java.lang.String freightitemmbl) {
		this.freightitemmbl = freightitemmbl;
	}

	public java.lang.Long getCarrieridmbl() {
		return carrieridmbl;
	}

	public void setCarrieridmbl(java.lang.Long carrieridmbl) {
		this.carrieridmbl = carrieridmbl;
	}

	public java.lang.String getCarryitemmbl() {
		return carryitemmbl;
	}

	public void setCarryitemmbl(java.lang.String carryitemmbl) {
		this.carryitemmbl = carryitemmbl;
	}

	public java.lang.String getMblpolcode() {
		return mblpolcode;
	}

	public void setMblpolcode(java.lang.String mblpolcode) {
		this.mblpolcode = mblpolcode;
	}

	public java.lang.String getMblpodcode() {
		return mblpodcode;
	}

	public void setMblpodcode(java.lang.String mblpodcode) {
		this.mblpodcode = mblpodcode;
	}

	public java.lang.String getMblpddcode() {
		return mblpddcode;
	}

	public void setMblpddcode(java.lang.String mblpddcode) {
		this.mblpddcode = mblpddcode;
	}

	public java.lang.String getMbldestinationcode() {
		return mbldestinationcode;
	}

	public void setMbldestinationcode(java.lang.String mbldestinationcode) {
		this.mbldestinationcode = mbldestinationcode;
	}

	public java.lang.String getMblpol() {
		return mblpol;
	}

	public void setMblpol(java.lang.String mblpol) {
		this.mblpol = mblpol;
	}

	public java.lang.String getMblpod() {
		return mblpod;
	}

	public void setMblpod(java.lang.String mblpod) {
		this.mblpod = mblpod;
	}

	public java.lang.String getMblpdd() {
		return mblpdd;
	}

	public void setMblpdd(java.lang.String mblpdd) {
		this.mblpdd = mblpdd;
	}

	public java.lang.String getMbldestination() {
		return mbldestination;
	}

	public void setMbldestination(java.lang.String mbldestination) {
		this.mbldestination = mbldestination;
	}

	public java.util.Date getEtambl() {
		return etambl;
	}

	public void setEtambl(java.util.Date etambl) {
		this.etambl = etambl;
	}

	public java.util.Date getEtdmbl() {
		return etdmbl;
	}

	public void setEtdmbl(java.util.Date etdmbl) {
		this.etdmbl = etdmbl;
	}

	public java.lang.String getSignplacembl() {
		return signplacembl;
	}

	public void setSignplacembl(java.lang.String signplacembl) {
		this.signplacembl = signplacembl;
	}

	public java.lang.String getHscode() {
		return hscode;
	}

	public void setHscode(java.lang.String hscode) {
		this.hscode = hscode;
	}

	public java.util.Date getOnboarddate() {
		return onboarddate;
	}

	public void setOnboarddate(java.util.Date onboarddate) {
		this.onboarddate = onboarddate;
	}

	public java.lang.Boolean getIsreleasemblwo() {
		return isreleasemblwo;
	}

	public void setIsreleasemblwo(java.lang.Boolean isreleasemblwo) {
		this.isreleasemblwo = isreleasemblwo;
	}

	public java.lang.Boolean getIsreleasehblwo() {
		return isreleasehblwo;
	}

	public void setIsreleasehblwo(java.lang.Boolean isreleasehblwo) {
		this.isreleasehblwo = isreleasehblwo;
	}

	public String getPutstatus() {
		return putstatus;
	}

	public void setPutstatus(String putstatus) {
		this.putstatus = putstatus;
	}

	public java.lang.String getRoutetyp() {
		return routetyp;
	}

	public void setRoutetyp(java.lang.String routetyp) {
		this.routetyp = routetyp;
	}

	public java.util.Date getSidate() {
		return sidate;
	}

	public void setSidate(java.util.Date sidate) {
		this.sidate = sidate;
	}

	public java.util.Date getVgmdate() {
		return vgmdate;
	}

	public void setVgmdate(java.util.Date vgmdate) {
		this.vgmdate = vgmdate;
	}

	public java.lang.Long getGoodstypeid() {
		return goodstypeid;
	}

	public void setGoodstypeid(java.lang.Long goodstypeid) {
		this.goodstypeid = goodstypeid;
	}

	public String getRemark_booking() {
		return remark_booking;
	}

	public void setRemark_booking(String remarkBooking) {
		this.remark_booking = remarkBooking;
	}

	public java.util.Date getClstime() {
		return clstime;
	}

	public void setClstime(java.util.Date clstime) {
		this.clstime = clstime;
	}

	public String getMarksnombl() {
		return marksnombl;
	}

	public void setMarksnombl(String marksnombl) {
		this.marksnombl = marksnombl;
	}

	public String getGoodsdescmbl() {
		return goodsdescmbl;
	}

	public void setGoodsdescmbl(String goodsdescmbl) {
		this.goodsdescmbl = goodsdescmbl;
	}

	public java.lang.String getMblterminal() {
		return mblterminal;
	}

	public void setMblterminal(java.lang.String mblterminal) {
		this.mblterminal = mblterminal;
	}

	public java.util.Date getMblcyopen() {
		return mblcyopen;
	}

	public void setMblcyopen(java.util.Date mblcyopen) {
		this.mblcyopen = mblcyopen;
	}

	public java.lang.String getLinecode() {
		return linecode;
	}

	public void setLinecode(java.lang.String linecode) {
		this.linecode = linecode;
	}

	public java.lang.String getBargepol() {
		return bargepol;
	}

	public void setBargepol(java.lang.String bargepol) {
		this.bargepol = bargepol;
	}

	public java.util.Date getBargecls() {
		return bargecls;
	}

	public void setBargecls(java.util.Date bargecls) {
		this.bargecls = bargecls;
	}

	public java.util.Date getBargeetd() {
		return bargeetd;
	}

	public void setBargeetd(java.util.Date bargeetd) {
		this.bargeetd = bargeetd;
	}

	public java.util.Date getBargeeta() {
		return bargeeta;
	}

	public void setBargeeta(java.util.Date bargeeta) {
		this.bargeeta = bargeeta;
	}

	public java.lang.String getRemarksreleasebill() {
		return remarksreleasebill;
	}

	public void setRemarksreleasebill(java.lang.String remarksreleasebill) {
		this.remarksreleasebill = remarksreleasebill;
	}

	public java.lang.String getIspecialprice() {
		return ispecialprice;
	}

	public void setIspecialprice(java.lang.String ispecialprice) {
		this.ispecialprice = ispecialprice;
	}

	public java.lang.String getTrucktype() {
		return trucktype;
	}

	public void setTrucktype(java.lang.String trucktype) {
		this.trucktype = trucktype;
	}

	public java.lang.String getCustype() {
		return custype;
	}

	public void setCustype(java.lang.String custype) {
		this.custype = custype;
	}

	public java.lang.String getVgmtype() {
		return vgmtype;
	}

	public void setVgmtype(java.lang.String vgmtype) {
		this.vgmtype = vgmtype;
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

	public java.lang.String getVgmstatus() {
		return vgmstatus;
	}

	public void setVgmstatus(java.lang.String vgmstatus) {
		this.vgmstatus = vgmstatus;
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

	public java.lang.Long getAgenidmbl() {
		return agenidmbl;
	}

	public void setAgenidmbl(java.lang.Long agenidmbl) {
		this.agenidmbl = agenidmbl;
	}

	public String getAgennamembl() {
		return agennamembl;
	}

	public void setAgennamembl(String agennamembl) {
		this.agennamembl = agennamembl;
	}

	public String getAgentitlembl() {
		return agentitlembl;
	}

	public void setAgentitlembl(String agentitlembl) {
		this.agentitlembl = agentitlembl;
	}

	public java.lang.String getPoaname() {
		return poaname;
	}

	public void setPoaname(java.lang.String poaname) {
		this.poaname = poaname;
	}

	public java.util.Date getDategatein() {
		return dategatein;
	}

	public void setDategatein(java.util.Date dategatein) {
		this.dategatein = dategatein;
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

	public java.lang.String getWaybillno() {
		return waybillno;
	}

	public void setWaybillno(java.lang.String waybillno) {
		this.waybillno = waybillno;
	}

	public java.lang.String getCarrieresi() {
		return carrieresi;
	}

	public void setCarrieresi(java.lang.String carrieresi) {
		this.carrieresi = carrieresi;
	}

	public java.lang.Long getPotid() {
		return potid;
	}

	public void setPotid(java.lang.Long potid) {
		this.potid = potid;
	}

	public java.lang.String getPot() {
		return pot;
	}

	public void setPot(java.lang.String pot) {
		this.pot = pot;
	}

	public java.lang.String getPotcode() {
		return potcode;
	}

	public void setPotcode(java.lang.String potcode) {
		this.potcode = potcode;
	}

	public java.lang.String getSolist() {
		return solist;
	}

	public void setSolist(java.lang.String solist) {
		this.solist = solist;
	}

	public java.lang.String getBlremarks() {
		return blremarks;
	}

	public void setBlremarks(java.lang.String blremarks) {
		this.blremarks = blremarks;
	}

	public java.lang.String getWmstype() {
		return wmstype;
	}

	public void setWmstype(java.lang.String wmstype) {
		this.wmstype = wmstype;
	}

	public java.lang.Long getWmsid() {
		return wmsid;
	}

	public void setWmsid(java.lang.Long wmsid) {
		this.wmsid = wmsid;
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

	public java.lang.String getOther4() {
		return other4;
	}

	public void setOther4(java.lang.String other4) {
		this.other4 = other4;
	}

	public java.lang.String getOther5() {
		return other5;
	}

	public void setOther5(java.lang.String other5) {
		this.other5 = other5;
	}

	public java.lang.String getOther6() {
		return other6;
	}

	public void setOther6(java.lang.String other6) {
		this.other6 = other6;
	}

	public java.lang.String getOther7() {
		return other7;
	}

	public void setOther7(java.lang.String other7) {
		this.other7 = other7;
	}

	public java.lang.String getOther8() {
		return other8;
	}

	public void setOther8(java.lang.String other8) {
		this.other8 = other8;
	}

	public java.lang.String getOther9() {
		return other9;
	}

	public void setOther9(java.lang.String other9) {
		this.other9 = other9;
	}

	public java.lang.String getOther10() {
		return other10;
	}

	public void setOther10(java.lang.String other10) {
		this.other10 = other10;
	}

	public java.lang.String getOther11() {
		return other11;
	}

	public void setOther11(java.lang.String other11) {
		this.other11 = other11;
	}

	public java.lang.String getOther12() {
		return other12;
	}

	public void setOther12(java.lang.String other12) {
		this.other12 = other12;
	}

	public java.lang.String getBusstatus() {
		return busstatus;
	}

	public void setBusstatus(java.lang.String busstatus) {
		this.busstatus = busstatus;
	}

	public java.lang.Boolean getIsreleasecnt() {
		return isreleasecnt;
	}

	public void setIsreleasecnt(java.lang.Boolean isreleasecnt) {
		this.isreleasecnt = isreleasecnt;
	}

	public java.util.Date getReleasecnttime() {
		return releasecnttime;
	}

	public void setReleasecnttime(java.util.Date releasecnttime) {
		this.releasecnttime = releasecnttime;
	}

	public java.lang.Boolean getIspickcnt() {
		return ispickcnt;
	}

	public void setIspickcnt(java.lang.Boolean ispickcnt) {
		this.ispickcnt = ispickcnt;
	}

	public java.util.Date getPickcnttime() {
		return pickcnttime;
	}

	public void setPickcnttime(java.util.Date pickcnttime) {
		this.pickcnttime = pickcnttime;
	}
	
	public java.lang.Boolean getIsreturncnt() {
		return isreturncnt;
	}

	public void setIsreturncnt(java.lang.Boolean isreturncnt) {
		this.isreturncnt = isreturncnt;
	}

	public java.util.Date getReturncnttime() {
		return returncnttime;
	}

	public void setReturncnttime(java.util.Date returncnttime) {
		this.returncnttime = returncnttime;
	}

	public java.lang.Long getChannelid() {
		return channelid;
	}

	public void setChannelid(java.lang.Long channelid) {
		this.channelid = channelid;
	}

	public java.lang.String getShipagent() {
		return shipagent;
	}

	public void setShipagent(java.lang.String shipagent) {
		this.shipagent = shipagent;
	}

	public java.lang.String getShiprefno() {
		return shiprefno;
	}

	public void setShiprefno(java.lang.String shiprefno) {
		this.shiprefno = shiprefno;
	}

	public java.lang.String getNameaccount() {
		return nameaccount;
	}

	public void setNameaccount(java.lang.String nameaccount) {
		this.nameaccount = nameaccount;
	}

	public java.lang.String getPono() {
		return pono;
	}

	public void setPono(java.lang.String pono) {
		this.pono = pono;
	}

	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}

	public Date getOrderdate() {
		return orderdate;
	}

	public Date getCabinetdate() {
		return cabinetdate;
	}

	public void setCabinetdate(Date cabinetdate) {
		this.cabinetdate = cabinetdate;
	}

	public Boolean getPriceuserconfirm() {
		return priceuserconfirm;
	}

	public void setPriceuserconfirm(Boolean priceuserconfirm) {
		this.priceuserconfirm = priceuserconfirm;
	}

	public java.lang.String getCusclass() {
		return cusclass;
	}

	public void setCusclass(java.lang.String cusclass) {
		this.cusclass = cusclass;
	}

	@Override
	public String toString() {
		return "BusShipping{" +
				"id=" + id +
				", jobid=" + jobid +
				", nos='" + nos + '\'' +
				", dono='" + dono + '\'' +
				", signplace1='" + signplace1 + '\'' +
				", singtime=" + singtime +
				", refno='" + refno + '\'' +
				", customerid=" + customerid +
				", goodstypeid=" + goodstypeid +
				", customerabbr='" + customerabbr + '\'' +
				", custtel='" + custtel + '\'' +
				", custmobile='" + custmobile + '\'' +
				", custfax='" + custfax + '\'' +
				", custcontact='" + custcontact + '\'' +
				", vessel='" + vessel + '\'' +
				", vescode='" + vescode + '\'' +
				", voyage='" + voyage + '\'' +
				", coupons='" + coupons + '\'' +
				", polid=" + polid +
				", potid=" + potid +
				", pol='" + pol + '\'' +
				", pot='" + pot + '\'' +
				", podid=" + podid +
				", pod='" + pod + '\'' +
				", paymentitem='" + paymentitem + '\'' +
				", pddid=" + pddid +
				", pdd='" + pdd + '\'' +
				", polcode='" + polcode + '\'' +
				", podcode='" + podcode + '\'' +
				", pddcode='" + pddcode + '\'' +
				", potcode='" + potcode + '\'' +
				", destinationcode='" + destinationcode + '\'' +
				", poa='" + poa + '\'' +
				", poaname='" + poaname + '\'' +
				", factoryinfo='" + factoryinfo + '\'' +
				", delnote='" + delnote + '\'' +
				", carryitem='" + carryitem + '\'' +
				", carrierid=" + carrierid +
				", scheduleid=" + scheduleid +
				", agentid=" + agentid +
				", hblrpt='" + hblrpt + '\'' +
				", mblrpt='" + mblrpt + '\'' +
				", orderrpt='" + orderrpt + '\'' +
				", bookrpt='" + bookrpt + '\'' +
				", isshowship=" + isshowship +
				", cls=" + cls +
				", etd=" + etd +
				", eta=" + eta +
				", edt=" + edt +
				", awt=" + awt +
				", atd=" + atd +
				", isput=" + isput +
				", puter='" + puter + '\'' +
				", putdesc='" + putdesc + '\'' +
				", putstatus='" + putstatus + '\'' +
				", ishold=" + ishold +
				", holder='" + holder + '\'' +
				", holddesc='" + holddesc + '\'' +
				", puttime=" + puttime +
				", holdtime=" + holdtime +
				", payplace='" + payplace + '\'' +
				", isdetail=" + isdetail +
				", mbltype='" + mbltype + '\'' +
				", piece='" + piece + '\'' +
				", grswgt='" + grswgt + '\'' +
				", cbm='" + cbm + '\'' +
				", loaditem='" + loaditem + '\'' +
				", packer='" + packer + '\'' +
				", marksno='" + marksno + '\'' +
				", goodsdesc='" + goodsdesc + '\'' +
				", totledesc='" + totledesc + '\'' +
				", bltype='" + bltype + '\'' +
				", hblno='" + hblno + '\'' +
				", billcount='" + billcount + '\'' +
				", hbltype='" + hbltype + '\'' +
				", sono='" + sono + '\'' +
				", sodate=" + sodate +
				", cnortitlembl='" + cnortitlembl + '\'' +
				", cneetitlembl='" + cneetitlembl + '\'' +
				", notifytitlembl='" + notifytitlembl + '\'' +
				", istelrel=" + istelrel +
				", telreltime=" + telreltime +
				", istelrelback=" + istelrelback +
				", telrelbacktime=" + telrelbacktime +
				", telrelnos='" + telrelnos + '\'' +
				", telreler='" + telreler + '\'' +
				", ismake=" + ismake +
				", datemake=" + datemake +
				", usermake='" + usermake + '\'' +
				", isadjust=" + isadjust +
				", dateadjust=" + dateadjust +
				", useradjust='" + useradjust + '\'' +
				", isconfirm=" + isconfirm +
				", dateconfirm=" + dateconfirm +
				", userconfirm='" + userconfirm + '\'' +
				", isprint=" + isprint +
				", dateprint=" + dateprint +
				", userprint='" + userprint + '\'' +
				", issign=" + issign +
				", datesign=" + datesign +
				", usersign='" + usersign + '\'' +
				", iscomplete=" + iscomplete +
				", datecomplete=" + datecomplete +
				", usercomplete='" + usercomplete + '\'' +
				", ismakembl=" + ismakembl +
				", datemakembl=" + datemakembl +
				", usermakembl='" + usermakembl + '\'' +
				", isadjustmbl=" + isadjustmbl +
				", dateadjustmbl=" + dateadjustmbl +
				", useradjustmbl='" + useradjustmbl + '\'' +
				", isconfirmmbl=" + isconfirmmbl +
				", dateconfirmmbl=" + dateconfirmmbl +
				", userconfirmmbl='" + userconfirmmbl + '\'' +
				", isprintmbl=" + isprintmbl +
				", dateprintmbl=" + dateprintmbl +
				", userprintmbl='" + userprintmbl + '\'' +
				", isgetmbl=" + isgetmbl +
				", dategetmbl=" + dategetmbl +
				", usergetmbl='" + usergetmbl + '\'' +
				", isreleasembl=" + isreleasembl +
				", datereleasembl=" + datereleasembl +
				", userreleasembl='" + userreleasembl + '\'' +
				", signplacembl='" + signplacembl + '\'' +
				", onboarddate=" + onboarddate +
				", routetyp='" + routetyp + '\'' +
				", sidate=" + sidate +
				", vgmdate=" + vgmdate +
				", remark_booking='" + remark_booking + '\'' +
				", clstime=" + clstime +
				", priceuserid=" + priceuserid +
				", mblterminal='" + mblterminal + '\'' +
				", mblcyopen=" + mblcyopen +
				", linecode='" + linecode + '\'' +
				", trucktype='" + trucktype + '\'' +
				", custype='" + custype + '\'' +
				", vgmtype='" + vgmtype + '\'' +
				", shcountrycode='" + shcountrycode + '\'' +
				", shenterprisecode='" + shenterprisecode + '\'' +
				", shcontacts='" + shcontacts + '\'' +
				", cocountrycode='" + cocountrycode + '\'' +
				", coenterprisecode='" + coenterprisecode + '\'' +
				", cocontacts='" + cocontacts + '\'' +
				", nocountrycode='" + nocountrycode + '\'' +
				", noenterprisecode='" + noenterprisecode + '\'' +
				", nocontacts='" + nocontacts + '\'' +
				", telreltype='" + telreltype + '\'' +
				", telrelrptid=" + telrelrptid +
				", agenidmbl=" + agenidmbl +
				", agennamembl='" + agennamembl + '\'' +
				", agentitlembl='" + agentitlembl + '\'' +
				", solist='" + solist + '\'' +
				", blremarks='" + blremarks + '\'' +
				", busstatus='" + busstatus + '\'' +
				", isreleasecnt=" + isreleasecnt +
				", releasecnttime=" + releasecnttime +
				", ispickcnt=" + ispickcnt +
				", pickcnttime=" + pickcnttime +
				", channelid=" + channelid +
				", nameaccount='" + nameaccount + '\'' +
				", ata=" + ata +
				", scheduleYear=" + scheduleYear +
				", scheduleMonth=" + scheduleMonth +
				", scheduleWeek=" + scheduleWeek +
				", remark1='" + remark1 + '\'' +
				", remark2='" + remark2 + '\'' +
				", remark3='" + remark3 + '\'' +
				", remark4='" + remark4 + '\'' +
				", remark5='" + remark5 + '\'' +
				", claimPre='" + claimPre + '\'' +
				", claimTruck='" + claimTruck + '\'' +
				", claimBill='" + claimBill + '\'' +
				", claimClear='" + claimClear + '\'' +
				", impexp='" + impexp + '\'' +
				", ldtype='" + ldtype + '\'' +
				", coltype='" + coltype + '\'' +
				", tradetype='" + tradetype + '\'' +
				", corpid=" + corpid +
				", linkid=" + linkid +
				", isdelete=" + isdelete +
				", isjoin=" + isjoin +
				", inputer='" + inputer + '\'' +
				", inputtime=" + inputtime +
				", updater='" + updater + '\'' +
				", updatetime=" + updatetime +
				", warehouseid=" + warehouseid +
				", cneeid=" + cneeid +
				", cneetitle='" + cneetitle + '\'' +
				", cneename='" + cneename + '\'' +
				", cnorid=" + cnorid +
				", cnortitle='" + cnortitle + '\'' +
				", cnorname='" + cnorname + '\'' +
				", notifyid=" + notifyid +
				", notifytitle='" + notifytitle + '\'' +
				", notifyname='" + notifyname + '\'' +
				", agenid=" + agenid +
				", agentitle='" + agentitle + '\'' +
				", agenname='" + agenname + '\'' +
				", claim_doc='" + claim_doc + '\'' +
				", mblno='" + mblno + '\'' +
				", ismbl=" + ismbl +
				", ishbl=" + ishbl +
				", isinsurance=" + isinsurance +
				", declareval=" + declareval +
				", declarepiece=" + declarepiece +
				", declarewgt=" + declarewgt +
				", declaresign='" + declaresign + '\'' +
				", cnortitlemblid=" + cnortitlemblid +
				", cneetitlembid=" + cneetitlembid +
				", notifytitlemblid=" + notifytitlemblid +
				", cnortitlemblname='" + cnortitlemblname + '\'' +
				", cneetitlemblname='" + cneetitlemblname + '\'' +
				", notifytitlemblname='" + notifytitlemblname + '\'' +
				", contractid=" + contractid +
				", contractno='" + contractno + '\'' +
				", isreleasemblwo=" + isreleasemblwo +
				", isreleasehblwo=" + isreleasehblwo +
				", ispecialprice='" + ispecialprice + '\'' +
				", dategatein=" + dategatein +
				", carrieresi='" + carrieresi + '\'' +
				", mblcorpid=" + mblcorpid +
				", freightitem='" + freightitem + '\'' +
				", destination='" + destination + '\'' +
				", pretrans='" + pretrans + '\'' +
				", transtype='" + transtype + '\'' +
				", paymenterms='" + paymenterms + '\'' +
				", routecode='" + routecode + '\'' +
				", vescodembl='" + vescodembl + '\'' +
				", vesselmbl='" + vesselmbl + '\'' +
				", voyagembl='" + voyagembl + '\'' +
				", mblnombl='" + mblnombl + '\'' +
				", freightitemmbl='" + freightitemmbl + '\'' +
				", carrieridmbl=" + carrieridmbl +
				", carryitemmbl='" + carryitemmbl + '\'' +
				", mblpolcode='" + mblpolcode + '\'' +
				", mblpodcode='" + mblpodcode + '\'' +
				", mblpddcode='" + mblpddcode + '\'' +
				", mbldestinationcode='" + mbldestinationcode + '\'' +
				", mblpol='" + mblpol + '\'' +
				", mblpod='" + mblpod + '\'' +
				", mblpdd='" + mblpdd + '\'' +
				", mbldestination='" + mbldestination + '\'' +
				", etambl=" + etambl +
				", etdmbl=" + etdmbl +
				", marksnombl='" + marksnombl + '\'' +
				", goodsdescmbl='" + goodsdescmbl + '\'' +
				", bargepol='" + bargepol + '\'' +
				", bargecls=" + bargecls +
				", bargeetd=" + bargeetd +
				", bargeeta=" + bargeeta +
				", remarksreleasebill='" + remarksreleasebill + '\'' +
				", vgmstatus='" + vgmstatus + '\'' +
				", hscode='" + hscode + '\'' +
				", notifyid2=" + notifyid2 +
				", notifytitle2='" + notifytitle2 + '\'' +
				", notifyname2='" + notifyname2 + '\'' +
				", waybillno='" + waybillno + '\'' +
				", isunder=" + isunder +
				", under='" + under + '\'' +
				", undertime=" + undertime +
				", wmstype='" + wmstype + '\'' +
				", wmsid=" + wmsid +
				", other1='" + other1 + '\'' +
				", other2='" + other2 + '\'' +
				", other3='" + other3 + '\'' +
				", other4='" + other4 + '\'' +
				", other5='" + other5 + '\'' +
				", other6='" + other6 + '\'' +
				", other7='" + other7 + '\'' +
				", other8='" + other8 + '\'' +
				", other9='" + other9 + '\'' +
				", other10='" + other10 + '\'' +
				", other11='" + other11 + '\'' +
				", other12='" + other12 + '\'' +
				", shipagent='" + shipagent + '\'' +
				", shiprefno='" + shiprefno + '\'' +
				", pono='" + pono + '\'' +
				", agentdesid=" + agentdesid +
				'}';
	}
}