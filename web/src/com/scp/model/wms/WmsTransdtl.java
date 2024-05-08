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
@Table(name = "wms_transdtl")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class WmsTransdtl implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;

	@Column(name = "transid")
	private java.lang.Long transid;
	
	@Column(name = "piece")
	private java.lang.Integer piece;
	
	@Column(name = "goodscode")
	private java.lang.String goodscode;
	
	@Column(name = "goodsnamec")
	private java.lang.String goodsnamec;
	
	@Column(name = "goodsnamee")
	private java.lang.String goodsnamee;
	
	@Column(name = "goodssize")
	private java.lang.String goodssize;
	
	@Column(name = "goodscolor")
	private java.lang.String goodscolor;
	
	@Column(name = "markno")
	private java.lang.String markno;
	
	@Column(name = "gdswgt")
	private java.math.BigDecimal gdswgt;
	
	@Column(name = "gdscbm")
	private java.math.BigDecimal gdscbm;
	
	@Column(name = "pieceinbox")
	private java.math.BigDecimal pieceinbox;
	
	@Column(name = "remark")
	private java.lang.String remark;
	
	@Column(name = "locdesc")
	private java.lang.String locdesc;
	
	@Column(name = "corpid")
	private java.lang.Long corpid;
	
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;

	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;

	@Column(name = "updater", length = 100)
	private java.lang.String updater;

	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;
	
	@Column(name = "customerid")
	private java.lang.Long customerid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.Long getTransid() {
		return transid;
	}

	public void setTransid(java.lang.Long transid) {
		this.transid = transid;
	}

	public java.lang.Integer getPiece() {
		return piece;
	}

	public void setPiece(java.lang.Integer piece) {
		this.piece = piece;
	}

	public java.lang.String getGoodscode() {
		return goodscode;
	}

	public void setGoodscode(java.lang.String goodscode) {
		this.goodscode = goodscode;
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

	public java.lang.String getRemark() {
		return remark;
	}

	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}

	public java.lang.String getLocdesc() {
		return locdesc;
	}

	public void setLocdesc(java.lang.String locdesc) {
		this.locdesc = locdesc;
	}

	public java.lang.Long getCorpid() {
		return corpid;
	}

	public void setCorpid(java.lang.Long corpid) {
		this.corpid = corpid;
	}

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
	}

	public java.lang.String getInputer() {
		return inputer;
	}

	public void setInputer(java.lang.String inputer) {
		this.inputer = inputer;
	}

	public java.util.Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(java.util.Date inputtime) {
		this.inputtime = inputtime;
	}

	public java.lang.String getUpdater() {
		return updater;
	}

	public void setUpdater(java.lang.String updater) {
		this.updater = updater;
	}

	public java.util.Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(java.util.Date updatetime) {
		this.updatetime = updatetime;
	}

	public java.lang.Long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(java.lang.Long customerid) {
		this.customerid = customerid;
	}
	
	
}