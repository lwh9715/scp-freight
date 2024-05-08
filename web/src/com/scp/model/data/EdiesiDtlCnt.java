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

@Table(name = "edi_esidtlcnt")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class EdiesiDtlCnt implements java.io.Serializable{
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	
	@Column(name = "ediesiid")
	private long ediesiid;

	@Column(name = "cntid")
	private long cntid;

	@Column(name = "cntypeid")
	private long cntypeid;

	@Column(name = "cntno")
	private String cntno;

	@Column(name = "sealno")
	private String sealno;

	@Column(name = "piece")
	private String piece;

	@Column(name = "packagee")
	private String packagee;

	@Column(name = "grswgt")
	private String grswgt;

	@Column(name = "cbm")
	private String cbm;

	@Column(name = "goodsnamee")
	private String goodsnamee;

	@Column(name = "markno")
	private String markno;

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


	public String getGoodsnamee() {
		return goodsnamee;
	}

	public void setGoodsnamee(String goodsnamee) {
		this.goodsnamee = goodsnamee;
	}

	public String getMarkno() {
		return markno;
	}

	public void setMarkno(String markno) {
		this.markno = markno;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEdiesiid() {
		return ediesiid;
	}

	public void setEdiesiid(long ediesiid) {
		this.ediesiid = ediesiid;
	}

	public long getCntid() {
		return cntid;
	}

	public void setCntid(long cntid) {
		this.cntid = cntid;
	}

	public long getCntypeid() {
		return cntypeid;
	}

	public void setCntypeid(long cntypeid) {
		this.cntypeid = cntypeid;
	}

	public String getCntno() {
		return cntno;
	}

	public void setCntno(String cntno) {
		this.cntno = cntno;
	}

	public String getSealno() {
		return sealno;
	}

	public void setSealno(String sealno) {
		this.sealno = sealno;
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
}
