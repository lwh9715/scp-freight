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
@Table(name = "wms_dispatchdtl")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class WmsDispatchdtl implements java.io.Serializable {

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

	/**
	 *@generated
	 */
	@Column(name = "dispatchid")
	private java.lang.Long dispatchid;

	/**
	 *@generated
	 */
	@Column(name = "goodscode", length = 50)
	private java.lang.String goodscode;
	
	
	/**
	 *@generated
	 */
	@Column(name = "goodssize", length = 50)
	private java.lang.String goodssize;
	
	/**
	 *@generated
	 */
	@Column(name = "goodscolor", length = 50)
	private java.lang.String goodscolor;
	
	
	@Column(name = "goodsnamec", length = 50)
	private java.lang.String goodsnamec;
	
	@Column(name = "pieceinbox")
	private java.math.BigDecimal pieceinbox;
	
	/**
	 *@generated
	 */
	@Column(name = "goodscbmall")
	private java.math.BigDecimal goodscbmall;
	
	
	/**
	 *@generated
	 */
	@Column(name = "markno", length = 50)
	private java.lang.String markno;

	/**
	 *@generated
	 */
	@Column(name = "piecestock")
	private java.lang.Integer piecestock;

	/**
	 *@generated
	 */
	@Column(name = "pieceadd")
	private java.lang.Integer pieceadd;

	public java.lang.String getGoodsnamec() {
		return goodsnamec;
	}

	public void setGoodsnamec(java.lang.String goodsnamec) {
		this.goodsnamec = goodsnamec;
	}

	public java.math.BigDecimal getPieceinbox() {
		return pieceinbox;
	}

	public void setPieceinbox(java.math.BigDecimal pieceinbox) {
		this.pieceinbox = pieceinbox;
	}

	/**
	 *@generated
	 */
	@Column(name = "piececut")
	private java.lang.Integer piececut;

	/**
	 *@generated
	 */
	@Column(name = "remark")
	private java.lang.String remark;

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
	@Column(name = "locdesc ")
	private java.lang.String locdesc ;

	public java.lang.String getLocdesc() {
		return locdesc;
	}

	public void setLocdesc(java.lang.String locdesc) {
		this.locdesc = locdesc;
	}

	/**
	 *@generated
	 */
	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;

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
	public java.lang.Long getDispatchid() {
		return this.dispatchid;
	}

	/**
	 *@generated
	 */
	public void setDispatchid(java.lang.Long value) {
		this.dispatchid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getGoodscode() {
		return this.goodscode;
	}

	/**
	 *@generated
	 */
	public void setGoodscode(java.lang.String value) {
		this.goodscode = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Integer getPiecestock() {
		return this.piecestock;
	}

	/**
	 *@generated
	 */
	public void setPiecestock(java.lang.Integer value) {
		this.piecestock = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Integer getPieceadd() {
		return this.pieceadd;
	}

	/**
	 *@generated
	 */
	public void setPieceadd(java.lang.Integer value) {
		this.pieceadd = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Integer getPiececut() {
		return this.piececut;
	}

	/**
	 *@generated
	 */
	public void setPiececut(java.lang.Integer value) {
		this.piececut = value;
	}

	public java.math.BigDecimal getGoodscbmall() {
		return goodscbmall;
	}

	public void setGoodscbmall(java.math.BigDecimal goodscbmall) {
		this.goodscbmall = goodscbmall;
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
}