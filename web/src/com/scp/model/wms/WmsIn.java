package com.scp.model.wms;

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
@Table(name = "wms_in")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class WmsIn implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	
	@Column(name = "corpid")
	private Long corpid;
	
	@Column(name = "saleid")
	private Long saleid;
	
	@Column(name="customvotes",nullable = true)
	private Integer customvotes;
	@Column(name="linktbl")
	private String linktbl;
	
	@Column(name="driverinfo")
	private String driverinfo;
	
	@Column(name="otherfiles")
	private String otherfiles;
	
	
	public String getOtherfiles() {
		return otherfiles;
	}

	public void setOtherfiles(String otherfiles) {
		this.otherfiles = otherfiles;
	}

	public String getDriverinfo() {
		return driverinfo;
	}

	public void setDriverinfo(String driverinfo) {
		this.driverinfo = driverinfo;
	}

	@Column(name="channelid")
	private Long channelid;
	
	@Column(name = "istaxret")
	private java.lang.Boolean istaxret;
	
	@Column(name="remarkunusual")
	private String remarkunusual;
	
	
	public Long getChannelid() {
		return channelid;
	}

	public void setChannelid(Long channelid) {
		this.channelid = channelid;
	}

	public String getLinktbl() {
		return linktbl;
	}

	public void setLinktbl(String linktbl) {
		this.linktbl = linktbl;
	}

	public Long getCorpid() {
		return corpid;
	}

	public void setCorpid(Long corpid) {
		this.corpid = corpid;
	}

	/**
	 *@generated
	 */
	@Column(name = "nos", length = 30)
	private java.lang.String nos;

	/**
	 *@generated
	 */
	@Column(name = "singtime", length = 29)
	private java.util.Date singtime;

	/**
	 *@generated
	 */
	@Column(name = "customerid")
	private java.lang.Long customerid;

	
	
	@Column(name = "customerabbr", length = 50)
	private java.lang.String customerabbr;
	
	/**
	 *@generated
	 */
	@Column(name = "isubmit")
	private java.lang.Boolean isubmit;

	/**
	 *@generated
	 */
	@Column(name = "submitime", length = 29)
	private java.util.Date submitime;
	
	


	public java.lang.Boolean getIstaxret() {
		return istaxret;
	}

	public void setIstaxret(java.lang.Boolean istaxret) {
		this.istaxret = istaxret;
	}

	/**
	 *@generated
	 */
	@Column(name = "submiter", length = 30)
	private java.lang.String submiter;

	/**
	 *@generated
	 */
	@Column(name = "ischeck")
	private java.lang.Boolean ischeck;

	/**
	 *@generated
	 */
	@Column(name = "checktime", length = 29)
	private java.util.Date checktime;

	/**
	 *@generated
	 */
	@Column(name = "customerno", length = 100)
	private java.lang.String customerno;
	
	@Column(name = "customtype", length = 1)
	private java.lang.String customtype;
	
	@Column(name = "checkter", length = 30)
	private java.lang.String checkter;
	
	@Column(name = "isfreecharge")
	private java.lang.Boolean isfreecharge;
	
	@Column(name = "cntno", length = 20)
	private java.lang.String cntno;
	
	@Column(name = "intime", length = 29)
	private java.util.Date intime;
	
	
	@Column(name = "nationaldate", length = 29)
	private java.util.Date nationaldate;
	
	@Column(name = "lclwmsdate", length = 29)
	private java.util.Date lclwmsdate;
	
	
	@Column(name = "lines")
	private java.lang.String lines;
	
	@Column(name = "isreturn")
	private java.lang.Boolean isreturn;
	
	public java.lang.Boolean getIsfreecharge() {
		return isfreecharge;
	}

	public void setIsfreecharge(java.lang.Boolean isfreecharge) {
		this.isfreecharge = isfreecharge;
	}

	/**
	 *@generated
	 */
	@Column(name = "remark")
	private java.lang.String remark;

	public java.lang.Boolean getIsreturn() {
		return isreturn;
	}

	public void setIsreturn(java.lang.Boolean isreturn) {
		this.isreturn = isreturn;
	}

	/**
	 *@generated
	 */
	@Column(name = "wmstype", length = 1)
	private java.lang.String wmstype;
	
	@Column(name = "noticetime", length = 29)
	private java.util.Date noticetime;
	
	@Column(name = "traindate", length = 29)
	private java.util.Date traindate;
	
	@Column(name = "trainno", length = 50)
	private java.lang.String trainno;
	
	@Column(name = "pol", length = 50)
	private java.lang.String pol;
	
	@Column(name = "destination", length = 50)
	private java.lang.String destination;
	
	@Column(name = "pdd", length = 50)
	private java.lang.String pdd;
	
	@Column(name = "loadgoodsrequire")
	private java.lang.String loadgoodsrequire;
	
	@Column(name = "gdsintype", length = 1)
	private java.lang.String gdsintype;
	
	@Column(name = "pickaddress")
	private java.lang.String pickaddress;
	
	@Column(name = "polwarehouseid")
	private java.lang.Long polwarehouseid;
	
	@Column(name = "pod", length = 50)
	private java.lang.String pod;
	
	@Column(name = "custominfo", length = 1)
	private java.lang.String custominfo;
	@Column(name = "op", length = 1)
	private java.lang.String op;
	@Column(name = "contactinfo", length = 1)
	private java.lang.String contactinfo;
	
	
	public Integer getCustomvotes() {
		return customvotes;
	}

	public void setCustomvotes(Integer customvotes) {
		this.customvotes = customvotes;
	}

	public java.lang.String getOp() {
		return op;
	}

	public void setOp(java.lang.String op) {
		this.op = op;
	}

	public java.lang.String getContactinfo() {
		return contactinfo;
	}

	public void setContactinfo(java.lang.String contactinfo) {
		this.contactinfo = contactinfo;
	}

	public java.lang.String getCustominfo() {
		return custominfo;
	}

	public void setCustominfo(java.lang.String custominfo) {
		this.custominfo = custominfo;
	}

	public java.lang.String getWmstype() {
		return wmstype;
	}

	public void setWmstype(java.lang.String wmstype) {
		this.wmstype = wmstype;
	}

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
	@Column(name = "jobid")
	private java.lang.Long jobid;

	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	public java.lang.String getCustomerno() {
		return customerno;
	}

	public void setCustomerno(java.lang.String customerno) {
		this.customerno = customerno;
	}

	public java.lang.String getCustomtype() {
		return customtype;
	}

	public void setCustomtype(java.lang.String customtype) {
		this.customtype = customtype;
	}

	/**
	 *@generated
	 */
	@Column(name = "warehouseid")
	private java.lang.Long warehouseid;

	/**
	 *@generated
	 */
	@Column(name = "refid")
	private java.lang.Long refid;
	
	/**
	 *@generated
	 */
	@Column(name = "refno", length = 30)
	private java.lang.String refno;
	
	/**
	 *@generated
	 */
	@Column(name = "factory")
	private java.lang.String factory;
	
	/**
	 *@generated
	 */
	@Column(name = "consignee")
	private java.lang.String consignee;
	
	@Column(name = "sono", length = 50)
	private java.lang.String sono;
	
	@Column(name = "sodate", length = 29)
	private java.util.Date sodate;

	@Column(name = "sovol")
	private java.math.BigDecimal sovol;
	
	@Column(name = "ewd", length = 29)
	private java.util.Date ewd;
	
	@Column(name = "agentname", length = 50)
	private java.lang.String agentname;
	
	@Column(name = "agentcnt")
	private java.lang.String agentcnt;
	
	@Column(name = "export")
	private java.lang.String export;
	
	@Column(name = "cntid", length = 50)
	private java.lang.String cntid;
	
	@Column(name = "poa", length = 50)
	private java.lang.String poa;
	
	@Column(name = "closingtime", length = 29)
	private java.util.Date closingtime;
	
	@Column(name = "nationwidetime", length = 29)
	private java.util.Date nationwidetime;
	
	@Column(name = "customtitle")
	private java.lang.String customtitle;
	
	/**
	 *@generated
	 */
	public long getId() {
		return this.id;
	}

	public java.lang.String getCustomtitle() {
		return customtitle;
	}

	public void setCustomtitle(java.lang.String customtitle) {
		this.customtitle = customtitle;
	}

	/**
	 *@generated
	 */
	public void setId(long value) {
		this.id = value;
	}

	public Long getSaleid() {
		return saleid;
	}

	public void setSaleid(Long saleid) {
		this.saleid = saleid;
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
	public java.util.Date getSubmitime() {
		return this.submitime;
	}

	/**
	 *@generated
	 */
	public void setSubmitime(java.util.Date value) {
		this.submitime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getSubmiter() {
		return this.submiter;
	}

	/**
	 *@generated
	 */
	public void setSubmiter(java.lang.String value) {
		this.submiter = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getChecktime() {
		return this.checktime;
	}

	/**
	 *@generated
	 */
	public void setChecktime(java.util.Date value) {
		this.checktime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCheckter() {
		return this.checkter;
	}

	/**
	 *@generated
	 */
	public void setCheckter(java.lang.String value) {
		this.checkter = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getRemark() {
		return this.remark;
	}

	/**
	 *@generated
	 */
	public void setRemark(java.lang.String value) {
		this.remark = value;
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
	public java.lang.Long getWarehouseid() {
		return this.warehouseid;
	}

	/**
	 *@generated
	 */
	public void setWarehouseid(java.lang.Long value) {
		this.warehouseid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getRefid() {
		return this.refid;
	}

	/**
	 *@generated
	 */
	public void setRefid(java.lang.Long value) {
		this.refid = value;
	}

	public java.lang.Boolean getIsubmit() {
		return isubmit;
	}

	public void setIsubmit(java.lang.Boolean isubmit) {
		this.isubmit = isubmit;
	}

	public java.lang.Boolean getIscheck() {
		return ischeck;
	}

	public void setIscheck(java.lang.Boolean ischeck) {
		this.ischeck = ischeck;
	}

	public java.lang.String getCustomerabbr() {
		return customerabbr;
	}

	public void setCustomerabbr(java.lang.String customerabbr) {
		this.customerabbr = customerabbr;
	}

	public java.lang.String getRefno() {
		return refno;
	}

	public void setRefno(java.lang.String refno) {
		this.refno = refno;
	}

	public java.lang.String getFactory() {
		return factory;
	}

	public void setFactory(java.lang.String factory) {
		this.factory = factory;
	}

	public java.lang.String getConsignee() {
		return consignee;
	}

	public void setConsignee(java.lang.String consignee) {
		this.consignee = consignee;
	}

	public java.lang.String getCntno() {
		return cntno;
	}

	public void setCntno(java.lang.String cntno) {
		this.cntno = cntno;
	}

	public java.util.Date getIntime() {
		return intime;
	}

	public void setIntime(java.util.Date intime) {
		this.intime = intime;
	}

	public java.util.Date getNoticetime() {
		return noticetime;
	}

	public void setNoticetime(java.util.Date noticetime) {
		this.noticetime = noticetime;
	}

	public java.util.Date getTraindate() {
		return traindate;
	}

	public void setTraindate(java.util.Date traindate) {
		this.traindate = traindate;
	}

	public java.lang.String getTrainno() {
		return trainno;
	}

	public void setTrainno(java.lang.String trainno) {
		this.trainno = trainno;
	}

	public java.lang.String getPol() {
		return pol;
	}

	public void setPol(java.lang.String pol) {
		this.pol = pol;
	}

	public java.lang.String getDestination() {
		return destination;
	}

	public void setDestination(java.lang.String destination) {
		this.destination = destination;
	}

	public java.lang.String getPdd() {
		return pdd;
	}

	public void setPdd(java.lang.String pdd) {
		this.pdd = pdd;
	}

	public java.lang.String getSono() {
		return sono;
	}

	public void setSono(java.lang.String sono) {
		this.sono = sono;
	}

	public java.util.Date getSodate() {
		return sodate;
	}

	public void setSodate(java.util.Date sodate) {
		this.sodate = sodate;
	}

	public java.math.BigDecimal getSovol() {
		return sovol;
	}

	public void setSovol(java.math.BigDecimal sovol) {
		this.sovol = sovol;
	}

	public java.util.Date getEwd() {
		return ewd;
	}

	public void setEwd(java.util.Date ewd) {
		this.ewd = ewd;
	}

	public java.lang.String getAgentname() {
		return agentname;
	}

	public void setAgentname(java.lang.String agentname) {
		this.agentname = agentname;
	}

	public java.lang.String getCntid() {
		return cntid;
	}

	public void setCntid(java.lang.String cntid) {
		this.cntid = cntid;
	}

	public java.lang.String getPoa() {
		return poa;
	}

	public void setPoa(java.lang.String poa) {
		this.poa = poa;
	}

	public java.util.Date getNationaldate() {
		return nationaldate;
	}

	public void setNationaldate(java.util.Date nationaldate) {
		this.nationaldate = nationaldate;
	}

	public java.util.Date getLclwmsdate() {
		return lclwmsdate;
	}

	public void setLclwmsdate(java.util.Date lclwmsdate) {
		this.lclwmsdate = lclwmsdate;
	}

	public java.lang.String getLines() {
		return lines;
	}

	public void setLines(java.lang.String lines) {
		this.lines = lines;
	}

	public java.lang.String getLoadgoodsrequire() {
		return loadgoodsrequire;
	}

	public void setLoadgoodsrequire(java.lang.String loadgoodsrequire) {
		this.loadgoodsrequire = loadgoodsrequire;
	}

	public java.lang.String getGdsintype() {
		return gdsintype;
	}

	public void setGdsintype(java.lang.String gdsintype) {
		this.gdsintype = gdsintype;
	}

	public java.lang.String getPickaddress() {
		return pickaddress;
	}

	public void setPickaddress(java.lang.String pickaddress) {
		this.pickaddress = pickaddress;
	}

	public java.lang.Long getPolwarehouseid() {
		return polwarehouseid;
	}

	public void setPolwarehouseid(java.lang.Long polwarehouseid) {
		this.polwarehouseid = polwarehouseid;
	}

	public java.util.Date getClosingtime() {
		return closingtime;
	}

	public void setClosingtime(java.util.Date closingtime) {
		this.closingtime = closingtime;
	}

	public java.util.Date getNationwidetime() {
		return nationwidetime;
	}

	public void setNationwidetime(java.util.Date nationwidetime) {
		this.nationwidetime = nationwidetime;
	}

	public java.lang.String getAgentcnt() {
		return agentcnt;
	}

	public void setAgentcnt(java.lang.String agentcnt) {
		this.agentcnt = agentcnt;
	}

	public java.lang.String getExport() {
		return export;
	}

	public void setExport(java.lang.String export) {
		this.export = export;
	}

	public java.lang.String getPod() {
		return pod;
	}

	public void setPod(java.lang.String pod) {
		this.pod = pod;
	}

	public String getRemarkunusual() {
		return remarkunusual;
	}

	public void setRemarkunusual(String remarkunusual) {
		this.remarkunusual = remarkunusual;
	}
	
}