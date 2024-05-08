package com.scp.model.del;

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
@Table(name = "del_load")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class DelLoad implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "corpid")
	private Long corpid;
	
	@Column(name = "warehouseid")
	private Long warehouseid;

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
	
	@Column(name = "refno", length = 30)
	private java.lang.String refno;
	
	/**
	 *@generated
	 */
	@Column(name = "loadtype")
	private java.lang.String loadtype;
	
	@Column(name = "cntno")
	private java.lang.String cntno;

	public java.lang.String getLoadtype() {
		return loadtype;
	}

	public void setLoadtype(java.lang.String loadtype) {
		this.loadtype = loadtype;
	}

	/**
	 *@generated
	 */
	@Column(name = "singtime", length = 29)
	private java.util.Date singtime;

	/**
	 *@generated
	 */
	@Column(name = "carid")
	private java.lang.Long carid;
	
	/**
	 *@generated
	 */
	@Column(name = "cntypeid")
	private java.lang.Long cntypeid;

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
	@Column(name = "checkter", length = 30)
	private java.lang.String checkter;

	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	/**
	 *@generated
	 */
	@Column(name = "remark")
	private java.lang.String remark;

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
	@Column(name = "lcltype")
	private java.lang.String lcltype;
	
	@Column(name = "sealno", length = 50)
	private java.lang.String sealno;
	
	/**
	 *@generated
	 */
	@Column(name = "loadtime")
	private java.util.Date loadtime;
	
	@Column(name = "cutloadtime")
	private java.util.Date cutloadtime;
	
	@Column(name = "pol", length = 50)
	private java.lang.String pol;
	
	@Column(name = "destination", length = 50)
	private java.lang.String destination;
	
	@Column(name = "pod", length = 50)
	private java.lang.String pod;
	
	@Column(name = "pdd", length = 50)
	private java.lang.String pdd;
	
	@Column(name = "traindate", length = 29)
	private java.util.Date traindate;
	
	@Column(name = "trainno", length = 50)
	private java.lang.String trainno;
	
	@Column(name = "export")
	private java.lang.String export;
	
	@Column(name = "fclreleaseno")
	private java.lang.String fclreleaseno;
	
	@Column(name = "releaseagent")
	private java.lang.String releaseagent;
	
	

	public java.lang.String getSealno() {
		return sealno;
	}

	public void setSealno(java.lang.String sealno) {
		this.sealno = sealno;
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
	public java.lang.Long getCarid() {
		return this.carid;
	}

	/**
	 *@generated
	 */
	public void setCarid(java.lang.Long value) {
		this.carid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIsubmit() {
		return this.isubmit;
	}

	/**
	 *@generated
	 */
	public void setIsubmit(java.lang.Boolean value) {
		this.isubmit = value;
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
	public java.lang.Boolean getIscheck() {
		return this.ischeck;
	}

	/**
	 *@generated
	 */
	public void setIscheck(java.lang.Boolean value) {
		this.ischeck = value;
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

	public java.lang.Long getCntypeid() {
		return cntypeid;
	}

	public void setCntypeid(java.lang.Long cntypeid) {
		this.cntypeid = cntypeid;
	}

	public java.lang.String getCntno() {
		return cntno;
	}

	public Long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(Long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public void setCntno(java.lang.String cntno) {
		this.cntno = cntno;
	}

	public java.lang.String getRefno() {
		return refno;
	}

	public void setRefno(java.lang.String refno) {
		this.refno = refno;
	}

	public java.lang.String getLcltype() {
		return lcltype;
	}

	public void setLcltype(java.lang.String lcltype) {
		this.lcltype = lcltype;
	}

	public java.util.Date getLoadtime() {
		return loadtime;
	}

	public void setLoadtime(java.util.Date loadtime) {
		this.loadtime = loadtime;
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

	public java.lang.String getExport() {
		return export;
	}

	public void setExport(java.lang.String export) {
		this.export = export;
	}

	public java.lang.String getFclreleaseno() {
		return fclreleaseno;
	}

	public void setFclreleaseno(java.lang.String fclreleaseno) {
		this.fclreleaseno = fclreleaseno;
	}

	public java.lang.String getReleaseagent() {
		return releaseagent;
	}

	public void setReleaseagent(java.lang.String releaseagent) {
		this.releaseagent = releaseagent;
	}

	public java.lang.String getPod() {
		return pod;
	}

	public void setPod(java.lang.String pod) {
		this.pod = pod;
	}

	public java.util.Date getCutloadtime() {
		return cutloadtime;
	}

	public void setCutloadtime(java.util.Date cutloadtime) {
		this.cutloadtime = cutloadtime;
	}
	
	
	
}