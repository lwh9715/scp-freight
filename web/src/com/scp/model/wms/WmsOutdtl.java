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
@Table(name = "wms_outdtl")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class WmsOutdtl implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;

	@Column(name = "corpid")
	private Long corpid;

	public Long getCorpid() {
		return corpid;
	}

	public void setCorpid(Long corpid) {
		this.corpid = corpid;
	}
	
	
	@Column(name="reftbl ")
	private String reftbl ;

	
	public String getReftbl() {
		return reftbl;
	}

	public void setReftbl(String reftbl) {
		this.reftbl = reftbl;
	}


	/**
	 *@generated
	 */
	@Column(name = "outid")
	private java.lang.Long outid;

	
	@Column(name = "refid")
	private java.lang.Long refid;

	/**
	 *@generated
	 */
	@Column(name = "goodscode")
	private java.lang.String goodscode;
	

	/**
	 *@generated
	 */
	@Column(name = "goodsnamec")
	private java.lang.String goodsnamec;
	

	/**
	 *@generated
	 */
	@Column(name = "goodsnamee")
	private java.lang.String goodsnamee;
	
	

	/**
	 *@generated
	 */
	@Column(name = "goodssize")
	private java.lang.String goodssize;
	
	

	/**
	 *@generated
	 */
	@Column(name = "goodscolor")
	private java.lang.String goodscolor;
	
	

	/**
	 *@generated
	 */
	@Column(name = "markno")
	private java.lang.String markno;
	
	
	/**
	 *@generated
	 */
	@Column(name = "gdswgt")
	private java.math.BigDecimal gdswgt;
	
	
	
	/**
	 *@generated
	 */
	@Column(name = "gdscbm")
	private java.math.BigDecimal gdscbm;
	
	
	/**
	 *@generated
	 */
	@Column(name = "pieceinbox")
	private java.math.BigDecimal pieceinbox;
	
	

	public java.lang.String getGoodscode() {
		return goodscode;
	}

	public void setGoodscode(java.lang.String goodscode) {
		this.goodscode = goodscode;
	}

	/**
	 *@generated
	 */
	@Column(name = "piece")
	private java.lang.Integer piece;

	
	/**
	 *@generated
	 */
	@Column(name = "remark")
	private java.lang.String remark;
	

	/**
	 *@generated
	 */
	@Column(name = "locdesc")
	private java.lang.String locdesc;
	
	
	
	

	public java.lang.String getLocdesc() {
		return locdesc;
	}

	public void setLocdesc(java.lang.String locdesc) {
		this.locdesc = locdesc;
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
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

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
	public java.lang.Long getOutid() {
		return this.outid;
	}

	/**
	 *@generated
	 */
	public void setOutid(java.lang.Long value) {
		this.outid = value;
	}

	

	/**
	 *@generated
	 */
	public java.lang.Integer getPiece() {
		return this.piece;
	}

	/**
	 *@generated
	 */
	public void setPiece(java.lang.Integer value) {
		this.piece = value;
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
	public java.lang.Boolean getIsdelete() {
		return this.isdelete;
	}

	/**
	 *@generated
	 */
	public void setIsdelete(java.lang.Boolean value) {
		this.isdelete = value;
	}

	public java.lang.String getGoodsnamec() {
		return goodsnamec;
	}

	public void setGoodsnamec(java.lang.String goodsnamec) {
		this.goodsnamec = goodsnamec;
	}

	public java.lang.String getGoodsnamee() {
		return goodsnamee;
	}

	public void setGoodsnamee(java.lang.String goodsnamee) {
		this.goodsnamee = goodsnamee;
	}

	public java.lang.String getGoodssize() {
		return goodssize;
	}

	public void setGoodssize(java.lang.String goodssize) {
		this.goodssize = goodssize;
	}

	public java.lang.String getGoodscolor() {
		return goodscolor;
	}

	public void setGoodscolor(java.lang.String goodscolor) {
		this.goodscolor = goodscolor;
	}

	public java.lang.String getMarkno() {
		return markno;
	}

	public void setMarkno(java.lang.String markno) {
		this.markno = markno;
	}

	public java.math.BigDecimal getGdswgt() {
		return gdswgt;
	}

	public void setGdswgt(java.math.BigDecimal gdswgt) {
		this.gdswgt = gdswgt;
	}

	public java.math.BigDecimal getGdscbm() {
		return gdscbm;
	}

	public void setGdscbm(java.math.BigDecimal gdscbm) {
		this.gdscbm = gdscbm;
	}

	public java.math.BigDecimal getPieceinbox() {
		return pieceinbox;
	}

	public void setPieceinbox(java.math.BigDecimal pieceinbox) {
		this.pieceinbox = pieceinbox;
	}

	public java.lang.Long getRefid() {
		return refid;
	}

	public void setRefid(java.lang.Long refid) {
		this.refid = refid;
	}
	
	
}