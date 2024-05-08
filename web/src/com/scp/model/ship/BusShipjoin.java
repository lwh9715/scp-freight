package com.scp.model.ship;

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
@Table(name = "bus_shipjoin")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusShipjoin implements java.io.Serializable {

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
	@Column(name = "jobid")
	private java.lang.Long jobid;
	
	
	@Column(name = "scheduleid")
	private java.lang.Long scheduleid;

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
	@Column(name = "mblno", length = 30)
	private java.lang.String mblno;

	/**
	 *@generated
	 */
	@Column(name = "mbltype", nullable = false, length = 1)
	private java.lang.String mbltype;

	
	/**
	 *@generated
	 */
	@Column(name = "manifeststat", nullable = false, length = 1)
	private java.lang.String manifeststat;

	/**
	 *@generated
	 */
	@Column(name = "nocno", length = 30)
	private java.lang.String nocno;
	
	
	/**
	 *@generated
	 */
	@Column(name = "freeday")
	private java.lang.Integer freeday;
	
	/**
	 *@generated
	 */
	@Column(name = "rotation", length = 30)
	private java.lang.String rotation;
	
	
	@Column(name = "isemailsend")
	private java.lang.Boolean isemailsend;
	

	public java.lang.String getRotation() {
		return rotation;
	}

	public void setRotation(java.lang.String rotation) {
		this.rotation = rotation;
	}

	public java.lang.Integer getFreeday() {
		return freeday;
	}

	public void setFreeday(java.lang.Integer freeday) {
		this.freeday = freeday;
	}

	/**
	 *@generated
	 */
	@Column(name = "texrelno", length = 30)
	private java.lang.String texrelno;

	/**
	 *@generated
	 */
	@Column(name = "texreldate", length = 13)
	private java.util.Date texreldate;

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
	@Column(name = "poa")
	private java.lang.String poa;

	/**
	 *@generated
	 */
	@Column(name = "carrierid")
	private java.lang.Long carrierid;

	/**
	 *@generated
	 */
	@Column(name = "freightitem", length = 2)
	private java.lang.String freightitem;

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
	@Column(name = "atd", length = 13)
	private java.util.Date atd;

	/**
	 *@generated
	 */
	@Column(name = "ata", length = 13)
	private java.util.Date ata;

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
	@Column(name = "isarrive")
	private java.lang.Boolean isarrive;
	
	/**
	 *@generated
	 */
	@Column(name = "ischeck")
	private java.lang.Boolean ischeck;
	
	/**
	 *@generated
	 */
	@Column(name = "checkter")
	private java.lang.String checkter;
	
	/**
	 *@generated
	 */
	@Column(name = "checktime")
	private java.util.Date checktime;
	
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
	public java.lang.String getMblno() {
		return this.mblno;
	}

	/**
	 *@generated
	 */
	public void setMblno(java.lang.String value) {
		this.mblno = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getMbltype() {
		return this.mbltype;
	}

	/**
	 *@generated
	 */
	public void setMbltype(java.lang.String value) {
		this.mbltype = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getManifeststat() {
		return this.manifeststat;
	}

	/**
	 *@generated
	 */
	public void setManifeststat(java.lang.String value) {
		this.manifeststat = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getNocno() {
		return this.nocno;
	}

	/**
	 *@generated
	 */
	public void setNocno(java.lang.String value) {
		this.nocno = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getTexrelno() {
		return this.texrelno;
	}

	/**
	 *@generated
	 */
	public void setTexrelno(java.lang.String value) {
		this.texrelno = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getTexreldate() {
		return this.texreldate;
	}

	/**
	 *@generated
	 */
	public void setTexreldate(java.util.Date value) {
		this.texreldate = value;
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

	public java.lang.Long getScheduleid() {
		return scheduleid;
	}

	public void setScheduleid(java.lang.Long scheduleid) {
		this.scheduleid = scheduleid;
	}

	public java.lang.Boolean getIsarrive() {
		return isarrive;
	}

	public void setIsarrive(java.lang.Boolean isarrive) {
		this.isarrive = isarrive;
	}

	public java.lang.Boolean getIscheck() {
		return ischeck;
	}

	public void setIscheck(java.lang.Boolean ischeck) {
		this.ischeck = ischeck;
	}

	public java.lang.String getCheckter() {
		return checkter;
	}

	public void setCheckter(java.lang.String checkter) {
		this.checkter = checkter;
	}

	public java.util.Date getChecktime() {
		return checktime;
	}

	public void setChecktime(java.util.Date checktime) {
		this.checktime = checktime;
	}

	public java.lang.Boolean getIsemailsend() {
		return isemailsend;
	}

	public void setIsemailsend(java.lang.Boolean isemailsend) {
		this.isemailsend = isemailsend;
	}
}