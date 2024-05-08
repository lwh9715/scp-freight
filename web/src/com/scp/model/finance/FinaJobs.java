package com.scp.model.finance;

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
@Table(name = "fina_jobs")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id")}) 
public class FinaJobs implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	 
	@Column(name = "customerid")
	private Long customerid;
	
	 
	@Column(name = "customerabbr")
	private java.lang.String customerabbr;
	
	
	@Column(name = "jobtype")
	private java.lang.String jobtype;
	
	 
	@Column(name = "refno")
	private java.lang.String refno;
	
	@Column(name = "refno2")
	private java.lang.String refno2;
	
	 
	@Column(name = "deptid")
	private Long deptid;
	
	
	@Column(name = "isconfirm_pp" , updatable = false)
	private Boolean isconfirm_pp;
	
	@Column(name = "confirm_pp_time" , updatable = false)
	private java.util.Date confirm_pp_time ;

	/**
	 *@generated
	 */
	@Column(name = "confirm_pp_user" , updatable = false)
	private java.lang.String confirm_pp_user ;
	
	
	
	@Column(name = "isconfirm_cc" , updatable = false)
	private Boolean isconfirm_cc;
	
	@Column(name = "confirm_cc_time" , updatable = false)
	private java.util.Date confirm_cc_time ;

	/**
	 *@generated
	 */
	@Column(name = "confirm_cc_user" , updatable = false)
	private java.lang.String confirm_cc_user ;
	
	
	
	
	@Column(name = "isconfirm2_pp" , updatable = false)
	private Boolean isconfirm2_pp;
	
	@Column(name = "confirm2_pp_time" , updatable = false)
	private java.util.Date confirm2_pp_time ;

	/**
	 *@generated
	 */
	@Column(name = "confirm2_pp_user" , updatable = false)
	private java.lang.String confirm2_pp_user ;
	
	
	
	@Column(name = "isconfirm2_cc" , updatable = false)
	private Boolean isconfirm2_cc;
	
	@Column(name = "confirm2_cc_time" , updatable = false)
	private java.util.Date confirm22_cc_time ;

	/**
	 *@generated
	 */
	@Column(name = "confirm2_cc_user" , updatable = false)
	private java.lang.String confirm2_cc_user ;
	
	
	@Column(name = "iscomplete_ar" , updatable = false)
	private Boolean iscomplete_ar ;
	
	@Column(name = "complete_ar_time" , updatable = false)
	private java.util.Date complete_ar_time ;

	/**
	 *@generated
	 */
	@Column(name = "complete_ar_user" , updatable = false)
	private java.lang.String complete_ar_user ;
	
	
	@Column(name = "iscomplete_ap" , updatable = false)
	private Boolean iscomplete_ap ;
	
	@Column(name = "complete_ap_time" , updatable = false)
	private java.util.Date complete_ap_time ;

	/**
	 *@generated
	 */
	@Column(name = "complete_ap_user" , updatable = false)
	private java.lang.String complete_ap_user ;
	
	@Column(name = "isconfirm_bus" , updatable = false)
	private Boolean isconfirm_bus;
	 
	@Column(name = "confirm_bus_time" , updatable = false)
	private java.util.Date confirm_bus_time ;
	
	@Column(name = "confirm_bus_user" , updatable = false)
	private java.lang.String confirm_bus_user ;
	
	@Column(name = "parentid")
	private Long parentid;
	
	@Column(name = "fmspkid")
	private java.lang.String fmspkid ;
	
	
	
	@Column(name = "ldtype")
	private java.lang.String ldtype ;
	
	
	@Column(name = "ldtype2")
	private java.lang.String ldtype2 ;
	 
	
	
	@Column(name = "corpid")
	private Long corpid;
	
	@Column(name = "corpidop")
	private Long corpidop;
	
	@Column(name = "corpidop2")
	private Long corpidop2;
	
	@Column(name = "deptop")
	private Long deptop;
	
	
	/**
	 *@generated
	 */
	@Column(name = "isubmit")
	private java.lang.Boolean isubmit;
	
	/**
	 *@generated
	 */
	@Column(name = "ischangebill")
	private java.lang.Boolean ischangebill;

	/**
	 *@generated
	 */
	@Column(name = "submitime", length = 29)
	private java.util.Date submitime;

	/**
	 *@generated
	 */
	@Column(name = "submiter", length = 30)
	private java.lang.String submiter;

	
	/**
	 *@generated
	 */
	@Column(name = "checktime", length = 29)
	private java.util.Date checktime;

	/**
	 *@generated
	 */
	@Column(name = "checkter", length = 30)
	private java.lang.String checkter;
	
	
	
	
	@Column(name = "iscomplete_amend" , updatable = false)
	private Boolean iscomplete_amend ;
	
	@Column(name = "iscomplete_loss" , updatable = false)
	private Boolean iscomplete_loss ;
	
	@Column(name = "complete_amend_time" , updatable = false)
	private java.util.Date complete_amend_time ;

	@Column(name = "complete_amend_user" , updatable = false)
	private java.lang.String complete_amend_user ;
	
	
	@Column(name = "completedate")
	private java.util.Date completedate ;
	
	
	
	public String getTradeway() {
		return tradeway;
	}

	public void setTradeway(String tradeway) {
		this.tradeway = tradeway;
	}

	@Column(name = "tradeway", length = 1)
	private java.lang.String tradeway;
	
	/**
	 *@generated
	 */
	@Column(name = "islock")
	private java.lang.Boolean islock;
	
	public Long getCorpid() {
		return corpid;
	}

	public void setCorpid(Long corpid) {
		this.corpid = corpid;
	}

	/**
	 *@generated
	 */
	@Column(name = "nos", nullable = false, length = 30)
	private java.lang.String nos;

	/**
	 *@generated
	 */
	@Column(name = "jobdate", length = 29)
	private java.util.Date jobdate;

	/**
	 *@generated
	 */
	@Column(name = "saleid")
	private java.lang.Long saleid;

	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String remarks;

	/**
	 *@generated
	 */
	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;
	

	/**
	 *@generated
	 */
	@Column(name = "sales", length = 100)
	private java.lang.String sales;
	
	/**
	 *@generated
	 */
	@Column(name = "apstatus", length = 10)
	private java.lang.String apstatus;
	
	/**
	 *@generated
	 */
	@Column(name = "arstatus", length = 10)
	private java.lang.String arstatus;
	
	/**
	 *@generated
	 */
	@Column(name = "invstatus", length = 10)
	private java.lang.String invstatus;

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
	@Column(name = "isshipping")
	private java.lang.Boolean isshipping;
	
	/**
	 *@generated
	 */
	@Column(name = "isair")
	private java.lang.Boolean isair ;
	
	
	
	/**
	 *@generated
	 */
	@Column(name = "istruck")
	private java.lang.Boolean istruck;
	
	/**
	 *@generated
	 */
	@Column(name = "island")
	private java.lang.Boolean island;
	
	
	/**
	 *@generated
	 */
	@Column(name = "havetrian")
	private java.lang.Boolean havetrian;
	
	
	/**
	 *@generated
	 */
	@Column(name = "isexp")
	private java.lang.Boolean isexp;
	
	/**
	 *@generated
	 */
	@Column(name = "istruckagent")
	private java.lang.Boolean istruckagent;
	
	/**
	 *@generated
	 */
	@Column(name = "iscus")
	private java.lang.Boolean iscus;
	
	/**
	 *@generated
	 */
	@Column(name = "isclc")
	private java.lang.Boolean isclc;
	
	/**
	 *@generated
	 */
	@Column(name = "isclose")
	private java.lang.Boolean isclose;
	
	/**
	 *@generated
	 */
	@Column(name = "ischeck")
	private java.lang.Boolean ischeck;
	
	/**
	 *@generated
	 */
	@Column(name = "iscomplete")
	private java.lang.Boolean iscomplete;
	
	/**
	 *@generated
	 */
	@Column(name = "jobstate ", length = 2)
	private java.lang.String jobstate ;
	
	/**
	 *@generated
	 */
	@Column(name = "isconfirmrpt")
	private java.lang.Boolean isconfirmrpt;
	
	/**
	 *@generated
	 */
	@Column(name = "genjobstype", length = 1)
	private java.lang.String genjobstype;

	@Column(name = "orderid")
	private java.lang.Long orderid;
	
	/**
	 *@generated
	 */
	@Column(name = "impexp ", length = 1)
	private java.lang.String impexp ;
	
	/**
	 *@generated
	 */
	@Column(name = "cusales ", length = 30)
	private java.lang.String cusales ;
	
	@Column(name = "isinvoice")
	private java.lang.Boolean isinvoice;
	
	@Column(name = "istaxable")
	private java.lang.Boolean istaxable;
	
	@Column(name = "isdelete" , updatable = false)
	private Boolean isdelete;
	
	@Column(name = "contact")
	private java.lang.String contact;
	
	@Column(name = "tel")
	private java.lang.String tel;

	@Column(name = "cargotype")
	private java.lang.String cargotype;
	
	@Column(name = "cusalesid")
	private java.lang.Long cusalesid;
	
	@Column(name = "cusalesconfirm")
	private Boolean cusalesconfirm;
	

	public String getCargotype() {
		return cargotype;
	}

	public void setCargotype(String cargotype) {
		this.cargotype = cargotype;
	}

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
	public java.util.Date getJobdate() {
		return this.jobdate;
	}

	/**
	 *@generated
	 */
	public void setJobdate(java.util.Date value) {
		this.jobdate = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getSaleid() {
		return this.saleid;
	}

	/**
	 *@generated
	 */
	public void setSaleid(java.lang.Long value) {
		this.saleid = value;
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
	public java.lang.String getInputer() {
		return this.inputer;
	}
	
	public java.lang.String getSales() {
		return this.sales;
	}
	
	public void setSales(java.lang.String value) {
		this.sales = value;
	}
	
	public java.lang.String getApstatus() {
		return this.apstatus;
	}
	
	public void setApstatus(java.lang.String value) {
		this.apstatus = value;
	}
	
	public java.lang.String getArstatus() {
		return this.arstatus;
	}
	
	public void setArstatus(java.lang.String value) {
		this.arstatus = value;
	}
	
	public java.lang.String getInvstatus() {
		return this.invstatus;
	}
	
	public void setInvstatus(java.lang.String value) {
		this.invstatus = value;
	}
	
	public java.lang.Boolean getIschangebill() {
		return ischangebill;
	}

	public void setIschangebill(java.lang.Boolean ischangebill) {
		this.ischangebill = ischangebill;
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

	public Long getCustomerid() {
		return customerid;
	}

	public java.lang.String getCustomerabbr() {
		return customerabbr;
	}

	public java.lang.String getRefno() {
		return refno;
	}

	public Long getDeptid() {
		return deptid;
	}

	public Boolean getIsconfirm_pp() {
		return isconfirm_pp;
	}

	public java.util.Date getConfirm_pp_time() {
		return confirm_pp_time;
	}

	public java.lang.String getConfirm_pp_user() {
		return confirm_pp_user;
	}

	public Boolean getIsconfirm_cc() {
		return isconfirm_cc;
	}

	public java.util.Date getConfirm_cc_time() {
		return confirm_cc_time;
	}

	public java.lang.String getConfirm_cc_user() {
		return confirm_cc_user;
	}

	public Boolean getIscomplete_ar() {
		return iscomplete_ar;
	}

	public java.util.Date getComplete_ar_time() {
		return complete_ar_time;
	}

	public java.lang.String getComplete_ar_user() {
		return complete_ar_user;
	}

	public Boolean getIscomplete_ap() {
		return iscomplete_ap;
	}

	public java.util.Date getComplete_ap_time() {
		return complete_ap_time;
	}

	public java.lang.String getComplete_ap_user() {
		return complete_ap_user;
	}

	public Long getParentid() {
		return parentid;
	}

	public void setCustomerid(Long customerid) {
		this.customerid = customerid;
	}

	public void setCustomerabbr(java.lang.String customerabbr) {
		this.customerabbr = customerabbr;
	}

	public void setRefno(java.lang.String refno) {
		this.refno = refno;
	}

	public void setDeptid(Long deptid) {
		this.deptid = deptid;
	}

	public void setIsconfirm_pp(Boolean isconfirmPp) {
		isconfirm_pp = isconfirmPp;
	}

	public void setConfirm_pp_time(java.util.Date confirmPpTime) {
		confirm_pp_time = confirmPpTime;
	}

	public void setConfirm_pp_user(java.lang.String confirmPpUser) {
		confirm_pp_user = confirmPpUser;
	}

	public void setIsconfirm_cc(Boolean isconfirmCc) {
		isconfirm_cc = isconfirmCc;
	}

	public void setConfirm_cc_time(java.util.Date confirmCcTime) {
		confirm_cc_time = confirmCcTime;
	}

	public void setConfirm_cc_user(java.lang.String confirmCcUser) {
		confirm_cc_user = confirmCcUser;
	}

	public void setIscomplete_ar(Boolean iscompleteAr) {
		iscomplete_ar = iscompleteAr;
	}

	public void setComplete_ar_time(java.util.Date completeArTime) {
		complete_ar_time = completeArTime;
	}

	public void setComplete_ar_user(java.lang.String completeArUser) {
		complete_ar_user = completeArUser;
	}

	public void setIscomplete_ap(Boolean iscompleteAp) {
		iscomplete_ap = iscompleteAp;
	}

	public void setComplete_ap_time(java.util.Date completeApTime) {
		complete_ap_time = completeApTime;
	}

	public void setComplete_ap_user(java.lang.String completeApUser) {
		complete_ap_user = completeApUser;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	public Boolean getIsconfirm2_pp() {
		return isconfirm2_pp;
	}

	public java.util.Date getConfirm2_pp_time() {
		return confirm2_pp_time;
	}

	public java.lang.String getConfirm2_pp_user() {
		return confirm2_pp_user;
	}

	public Boolean getIsconfirm2_cc() {
		return isconfirm2_cc;
	}

	public java.util.Date getConfirm22_cc_time() {
		return confirm22_cc_time;
	}

	
	public java.lang.String getFmspkid() {
		return fmspkid;
	}

	public void setIsconfirm2_pp(Boolean isconfirm2Pp) {
		isconfirm2_pp = isconfirm2Pp;
	}

	public void setConfirm2_pp_time(java.util.Date confirm2PpTime) {
		confirm2_pp_time = confirm2PpTime;
	}

	public void setConfirm2_pp_user(java.lang.String confirm2PpUser) {
		confirm2_pp_user = confirm2PpUser;
	}

	public void setIsconfirm2_cc(Boolean isconfirm2Cc) {
		isconfirm2_cc = isconfirm2Cc;
	}

	public void setConfirm22_cc_time(java.util.Date confirm22CcTime) {
		confirm22_cc_time = confirm22CcTime;
	}

	

	public void setFmspkid(java.lang.String fmspkid) {
		this.fmspkid = fmspkid;
	}

	public java.lang.String getConfirm2_cc_user() {
		return confirm2_cc_user;
	}

	public void setConfirm2_cc_user(java.lang.String confirm2CcUser) {
		confirm2_cc_user = confirm2CcUser;
	}

	public java.lang.String getJobtype() {
		return jobtype;
	}

	public void setJobtype(java.lang.String jobtype) {
		this.jobtype = jobtype;
	}

	public java.lang.Boolean getIsshipping() {
		return isshipping;
	}

	public java.lang.Boolean getIsair() {
		return isair;
	}

	public java.lang.Boolean getIstruck() {
		return istruck;
	}

	public java.lang.Boolean getIstruckagent() {
		return istruckagent;
	}

	public java.lang.Boolean getIscus() {
		return iscus;
	}

	public java.lang.Boolean getIsclc() {
		return isclc;
	}

	public void setIsshipping(java.lang.Boolean isshipping) {
		this.isshipping = isshipping;
	}

	public void setIsair(java.lang.Boolean isair) {
		this.isair = isair;
	}

	public void setIstruck(java.lang.Boolean istruck) {
		this.istruck = istruck;
	}

	public void setIstruckagent(java.lang.Boolean istruckagent) {
		this.istruckagent = istruckagent;
	}

	public void setIscus(java.lang.Boolean iscus) {
		this.iscus = iscus;
	}

	public void setIsclc(java.lang.Boolean isclc) {
		this.isclc = isclc;
	}

	public java.lang.Boolean getIsclose() {
		return isclose;
	}

	public void setIsclose(java.lang.Boolean isclose) {
		this.isclose = isclose;
	}

	public java.lang.Boolean getIscheck() {
		return ischeck;
	}

	public void setIscheck(java.lang.Boolean ischeck) {
		this.ischeck = ischeck;
	}

	public java.lang.Boolean getIscomplete() {
		return iscomplete;
	}

	public void setIscomplete(java.lang.Boolean iscomplete) {
		this.iscomplete = iscomplete;
	}

	public java.lang.String getLdtype() {
		return ldtype;
	}

	public void setLdtype(java.lang.String ldtype) {
		this.ldtype = ldtype;
	}

	public java.lang.String getLdtype2() {
		return ldtype2;
	}

	public void setLdtype2(java.lang.String ldtype2) {
		this.ldtype2 = ldtype2;
	}

	public java.lang.Boolean getIsland() {
		return island;
	}

	public java.lang.Boolean getIsexp() {
		return isexp;
	}

	public void setIsland(java.lang.Boolean island) {
		this.island = island;
	}

	public void setIsexp(java.lang.Boolean isexp) {
		this.isexp = isexp;
	}

	public java.lang.Boolean getIsubmit() {
		return isubmit;
	}

	public java.util.Date getSubmitime() {
		return submitime;
	}

	public java.lang.String getSubmiter() {
		return submiter;
	}

	public java.util.Date getChecktime() {
		return checktime;
	}

	public java.lang.String getCheckter() {
		return checkter;
	}

	

	public void setIsubmit(java.lang.Boolean isubmit) {
		this.isubmit = isubmit;
	}

	public void setSubmitime(java.util.Date submitime) {
		this.submitime = submitime;
	}

	public void setSubmiter(java.lang.String submiter) {
		this.submiter = submiter;
	}

	public void setChecktime(java.util.Date checktime) {
		this.checktime = checktime;
	}

	public void setCheckter(java.lang.String checkter) {
		this.checkter = checkter;
	}

	public java.lang.String getJobstate() {
		return jobstate;
	}

	public void setJobstate(java.lang.String jobstate) {
		this.jobstate = jobstate;
	}

	public java.lang.Boolean getIslock() {
		return islock;
	}

	public void setIslock(java.lang.Boolean islock) {
		this.islock = islock;
	}

	public java.lang.Boolean getIsconfirmrpt() {
		return isconfirmrpt;
	}

	public void setIsconfirmrpt(java.lang.Boolean isconfirmrpt) {
		this.isconfirmrpt = isconfirmrpt;
	}

	public java.lang.String getGenjobstype() {
		return genjobstype;
	}

	public void setGenjobstype(java.lang.String genjobstype) {
		this.genjobstype = genjobstype;
	}

	public java.lang.String getRefno2() {
		return refno2;
	}

	public void setRefno2(java.lang.String refno2) {
		this.refno2 = refno2;
	}

	public Boolean getIscomplete_amend() {
		return iscomplete_amend;
	}

	public void setIscomplete_amend(Boolean iscompleteAmend) {
		iscomplete_amend = iscompleteAmend;
	}

	public java.util.Date getComplete_amend_time() {
		return complete_amend_time;
	}

	public void setComplete_amend_time(java.util.Date completeAmendTime) {
		complete_amend_time = completeAmendTime;
	}

	public java.lang.String getComplete_amend_user() {
		return complete_amend_user;
	}

	public void setComplete_amend_user(java.lang.String completeAmendUser) {
		complete_amend_user = completeAmendUser;
	}

	public Long getCorpidop() {
		return corpidop;
	}

	public void setCorpidop(Long corpidop) {
		this.corpidop = corpidop;
	}
	
	public Long getCorpidop2() {
		return corpidop2;
	}

	public void setCorpidop2(Long corpidop2) {
		this.corpidop2 = corpidop2;
	}

	public java.lang.Long getOrderid() {
		return orderid;
	}

	public void setOrderid(java.lang.Long orderid) {
		this.orderid = orderid;
	}

	public java.lang.String getImpexp() {
		return impexp;
	}

	public void setImpexp(java.lang.String impexp) {
		this.impexp = impexp;
	}

	public java.lang.String getCusales() {
		return cusales;
	}

	public void setCusales(java.lang.String cusales) {
		this.cusales = cusales;
	}

	public java.lang.Boolean getIsinvoice() {
		return isinvoice;
	}

	public void setIsinvoice(java.lang.Boolean isinvoice) {
		this.isinvoice = isinvoice;
	}

	public Boolean getIsconfirm_bus() {
		return isconfirm_bus;
	}

	public void setIsconfirm_bus(Boolean isconfirmBus) {
		isconfirm_bus = isconfirmBus;
	}

	public java.util.Date getConfirm_bus_time() {
		return confirm_bus_time;
	}

	public void setConfirm_bus_time(java.util.Date confirmBusTime) {
		confirm_bus_time = confirmBusTime;
	}

	public java.lang.String getConfirm_bus_user() {
		return confirm_bus_user;
	}

	public void setConfirm_bus_user(java.lang.String confirmBusUser) {
		confirm_bus_user = confirmBusUser;
	}

	public java.util.Date getCompletedate() {
		return completedate;
	}

	public void setCompletedate(java.util.Date completedate) {
		this.completedate = completedate;
	}

	public Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(Boolean isdelete) {
		this.isdelete = isdelete;
	}

	public java.lang.String getContact() {
		return contact;
	}

	public void setContact(java.lang.String contact) {
		this.contact = contact;
	}

	public java.lang.String getTel() {
		return tel;
	}

	public void setTel(java.lang.String tel) {
		this.tel = tel;
	}

	public java.lang.Boolean getHavetrian() {
		return havetrian;
	}

	public void setHavetrian(java.lang.Boolean havetrian) {
		this.havetrian = havetrian;
	}

	public Long getDeptop() {
		return deptop;
	}

	public void setDeptop(Long deptop) {
		this.deptop = deptop;
	}

	public java.lang.Boolean getIstaxable() {
		return istaxable;
	}

	public void setIstaxable(java.lang.Boolean istaxable) {
		this.istaxable = istaxable;
	}

	public Boolean getIscomplete_loss() {
		return iscomplete_loss;
	}

	public void setIscomplete_loss(Boolean iscompleteLoss) {
		iscomplete_loss = iscompleteLoss;
	}

	public java.lang.Long getCusalesid() {
		return cusalesid;
	}

	public void setCusalesid(java.lang.Long cusalesid) {
		this.cusalesid = cusalesid;
	}

	public Boolean getCusalesconfirm() {
		return cusalesconfirm;
	}

	public void setCusalesconfirm(Boolean cusalesconfirm) {
		this.cusalesconfirm = cusalesconfirm;
	}

}