package com.scp.model.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "edi_esi")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class Ediesi implements java.io.Serializable{
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;


	@Column(name = "esitype")
	private String esitype;


	@Column(name = "nos")
	private String nos;
	
	@Column(name = "sono")
	private String sono;
	
	@Column(name = "mblno")
	private String mblno;

	public String getMblno() {
		return mblno;
	}

	public void setMblno(String mblno) {
		this.mblno = mblno;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "shipper")
	private String shipper;

	@Column(name = "consignee")
	private String consignee;

	@Column(name = "notifier1")
	private String notifier1;

	@Column(name = "vessel")
	private String vessel;

	@Column(name = "voyage")
	private String voyage;

	@Column(name = "poacode")
	private String poacode;

	@Column(name = "poaname")
	private String poaname;

	@Column(name = "polcode")
	private String polcode;

	@Column(name = "polname")
	private String polname;

	@Column(name = "pddcode")
	private String pddcode;

	@Column(name = "pddname")
	private String pddname;

	@Column(name = "destinationcode")
	private String destinationcode;

	@Column(name = "destination")
	private String destination;

	@Column(name = "carryitem")
	private String carryitem;

	@Column(name = "blremarks")
	private String blremarks;

	@Column(name = "freightitem")
	private String freightitem;

	@Column(name = "piece")
	private String piece;

	@Column(name = "packagee")
	private String packagee;

	@Column(name = "grswgt")
	private String grswgt;

	@Column(name = "cbm")
	private String cbm;

	@Column(name = "bltype")
	private String bltype;


	@Column(name = "isdelete")
	private Boolean isdelete;

	@Column(name = "inputer")
	private String inputer;
	
	@Column(name = "updater")
	private String updater;
	
	@Column(name = "inputtime")
	private Date inputtime;
	
	@Column(name = "updatetime")
	private Date updatetime;


	@Column(name = "carrier")
	private String carrier;

	@Column(name = "scac")
	private String scac;
	
	@Column(name = "si")
	private String si;
	
	@Column(name = "ct")
	private String ct;
	
	@Column(name = "fi")
	private String fi;
	
	@Column(name = "issuedate")
	private String issuedate;
	
	@Column(name = "onboarddate")
	private String onboarddate;

	@Column(name = "billnum1")
	private String billnum1;

	@Column(name = "billnum2")
	private String billnum2;
	
	
	@Column(name = "blremarksother")
	private String blremarksother;

	@Column(name = "seafeestatus")
	private String seafeestatus;


	@Column(name = "filetype")
	private String filetype;

	@Column(name = "contacter")
	private String contacter;

	@Column(name = "contactel")
	private String contactel;

	@Column(name = "contactemail")
	private String contactemail;
	
	@Column(name = "signplace")
	private String signplace;

	@Column(name = "signtime")
	private Date signtime;
	
	@Column(name = "agent")
	private String agent;
	
	@Column(name = "imo")
	private String imo;
	
	@Column(name = "issubmit")
	private Boolean issubmit;
	
	@Column(name = "notifier2")
	private String notifier2;
	
	@Column(name = "submittime")
	private Date submittime;
	
	@Column(name = "submiter")
	private String submiter;
	
	@Column(name = "sendercode")
	private String sendercode;
	
	@Column(name = "acceptcode")
	private String acceptcode;
	
	@Column(name = "siresp")
	private String siresp;
	

	public String getImo() {
		return imo;
	}

	public void setImo(String imo) {
		this.imo = imo;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getContacter() {
		return contacter;
	}

	public void setContacter(String contacter) {
		this.contacter = contacter;
	}

	public String getContactel() {
		return contactel;
	}

	public String getSignplace() {
		return signplace;
	}

	public void setSignplace(String signplace) {
		this.signplace = signplace;
	}

	public Date getSigntime() {
		return signtime;
	}

	public void setSigntime(Date signtime) {
		this.signtime = signtime;
	}

	public void setContactel(String contactel) {
		this.contactel = contactel;
	}

	public String getContactemail() {
		return contactemail;
	}

	public void setContactemail(String contactemail) {
		this.contactemail = contactemail;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getBlremarksother() {
		return blremarksother;
	}

	public void setBlremarksother(String blremarksother) {
		this.blremarksother = blremarksother;
	}

	public String getSeafeestatus() {
		return seafeestatus;
	}

	public void setSeafeestatus(String seafeestatus) {
		this.seafeestatus = seafeestatus;
	}

	public String getBillnum1() {
		return billnum1;
	}

	public void setBillnum1(String billnum1) {
		this.billnum1 = billnum1;
	}

	public String getBillnum2() {
		return billnum2;
	}

	public void setBillnum2(String billnum2) {
		this.billnum2 = billnum2;
	}

	public String getIssuedate() {
		return issuedate;
	}

	public void setIssuedate(String issuedate) {
		this.issuedate = issuedate;
	}

	public String getOnboarddate() {
		return onboarddate;
	}

	public void setOnboarddate(String onboarddate) {
		this.onboarddate = onboarddate;
	}

	public String getSi() {
		return si;
	}

	public void setSi(String si) {
		this.si = si;
	}

	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}

	public String getFi() {
		return fi;
	}

	public void setFi(String fi) {
		this.fi = fi;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getScac() {
		return scac;
	}

	public void setScac(String scac) {
		this.scac = scac;
	}

	public Long getId() {

		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEsitype() {
		return esitype;
	}

	public void setEsitype(String esitype) {
		this.esitype = esitype;
	}

	public String getNos() {
		return nos;
	}

	public void setNos(String nos) {
		this.nos = nos;
	}

	public String getShipper() {
		return shipper;
	}

	public void setShipper(String shipper) {
		this.shipper = shipper;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getNotifier1() {
		return notifier1;
	}

	public void setNotifier1(String notifier1) {
		this.notifier1 = notifier1;
	}

	public String getVessel() {
		return vessel;
	}

	public void setVessel(String vessel) {
		this.vessel = vessel;
	}

	public String getVoyage() {
		return voyage;
	}

	public void setVoyage(String voyage) {
		this.voyage = voyage;
	}

	public String getPoacode() {
		return poacode;
	}

	public void setPoacode(String poacode) {
		this.poacode = poacode;
	}

	public String getPoaname() {
		return poaname;
	}

	public void setPoaname(String poaname) {
		this.poaname = poaname;
	}

	public String getPolcode() {
		return polcode;
	}

	public void setPolcode(String polcode) {
		this.polcode = polcode;
	}

	public String getPolname() {
		return polname;
	}

	public void setPolname(String polname) {
		this.polname = polname;
	}

	public String getPddcode() {
		return pddcode;
	}

	public void setPddcode(String pddcode) {
		this.pddcode = pddcode;
	}

	public String getPddname() {
		return pddname;
	}

	public void setPddname(String pddname) {
		this.pddname = pddname;
	}

	public String getDestinationcode() {
		return destinationcode;
	}

	public void setDestinationcode(String destinationcode) {
		this.destinationcode = destinationcode;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getCarryitem() {
		return carryitem;
	}

	public void setCarryitem(String carryitem) {
		this.carryitem = carryitem;
	}

	public String getBlremarks() {
		return blremarks;
	}

	public void setBlremarks(String blremarks) {
		this.blremarks = blremarks;
	}

	public String getFreightitem() {
		return freightitem;
	}

	public void setFreightitem(String freightitem) {
		this.freightitem = freightitem;
	}

	public String getPiece() {
		return piece;
	}

	public void setPiece(String piece) {
		this.piece = piece;
	}

	public String getPackagee() {
		return packagee;
	}

	public void setPackagee(String packagee) {
		this.packagee = packagee;
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

	public String getBltype() {
		return bltype;
	}

	public void setBltype(String bltype) {
		this.bltype = bltype;
	}

	public String getSono() {
		return sono;
	}

	public void setSono(String sono) {
		this.sono = sono;
	}

	public Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(Boolean isdelete) {
		this.isdelete = isdelete;
	}

	public String getInputer() {
		return inputer;
	}

	public void setInputer(String inputer) {
		this.inputer = inputer;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public Boolean getIssubmit() {
		return issubmit;
	}

	public void setIssubmit(Boolean issubmit) {
		this.issubmit = issubmit;
	}

	public String getNotifier2() {
		return notifier2;
	}

	public void setNotifier2(String notifier2) {
		this.notifier2 = notifier2;
	}

	public Date getSubmittime() {
		return submittime;
	}

	public void setSubmittime(Date submittime) {
		this.submittime = submittime;
	}

	public String getSubmiter() {
		return submiter;
	}

	public void setSubmiter(String submiter) {
		this.submiter = submiter;
	}

	public String getSendercode() {
		return sendercode;
	}

	public void setSendercode(String sendercode) {
		this.sendercode = sendercode;
	}

	public String getAcceptcode() {
		return acceptcode;
	}

	public void setAcceptcode(String acceptcode) {
		this.acceptcode = acceptcode;
	}

	public String getSiresp() {
		return siresp;
	}

	public void setSiresp(String siresp) {
		this.siresp = siresp;
	}
	
	
}
