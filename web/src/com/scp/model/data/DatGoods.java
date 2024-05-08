package com.scp.model.data;

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
@Table(name = "dat_goods")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class DatGoods implements java.io.Serializable {

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
	@Column(name = "code", length = 50)
	private java.lang.String code;

	/**
	 *@generated
	 */
	@Column(name = "namec", length = 100)
	private java.lang.String namec;

	/**
	 *@generated
	 */
	@Column(name = "namee", length = 100)
	private java.lang.String namee;

	/**
	 *@generated
	 */
	@Column(name = "supplierid")
	private java.lang.Long supplierid;

	/**
	 *@generated
	 */
	@Column(name = "goodssize", length = 10)
	private java.lang.String goodssize;

	/**
	 *@generated
	 */
	@Column(name = "l")
	private java.math.BigDecimal l;

	/**
	 *@generated
	 */
	@Column(name = "w")
	private java.math.BigDecimal w;

	/**
	 *@generated
	 */
	@Column(name = "h")
	private java.math.BigDecimal h;

	/**
	 *@generated
	 */
	@Column(name = "v")
	private java.math.BigDecimal v;

	/**
	 *@generated
	 */
	@Column(name = "goodscolor", length = 10)
	private java.lang.String goodscolor;

	/**
	 *@generated
	 */
	@Column(name = "netweight")
	private java.math.BigDecimal netweight;

	/**
	 *@generated
	 */
	@Column(name = "grossweight")
	private java.math.BigDecimal grossweight;

	/**
	 *@generated
	 */
	@Column(name = "goodsunit", length = 10)
	private java.lang.String goodsunit;

	/**
	 *@generated
	 */
	@Column(name = "minstkpiece")
	private java.lang.Integer minstkpiece;

	/**
	 *@generated
	 */
	@Column(name = "maxstkpiece")
	private java.lang.Integer maxstkpiece;

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
	@Column(name = "barcode", length = 30)
	private java.lang.String barcode;

	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	/**
	 *@generated
	 */
	@Column(name = "tracery", length = 20)
	private java.lang.String tracery;
	
	
	/**
	 *@generated
	 */
	@Column(name = "goodstypeid")
	private java.lang.Long goodstypeid;
	
	@Column(name = "hscode", length = 100)
	private java.lang.String hscode;
	
	@Column(name = "customerid")
	private java.lang.Long customerid;

	/**
	 *@generated
	 */
	
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
	public java.lang.String getCode() {
		return this.code;
	}

	/**
	 *@generated
	 */
	public void setCode(java.lang.String value) {
		this.code = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getNamec() {
		return this.namec;
	}

	/**
	 *@generated
	 */
	public void setNamec(java.lang.String value) {
		this.namec = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getNamee() {
		return this.namee;
	}

	/**
	 *@generated
	 */
	public void setNamee(java.lang.String value) {
		this.namee = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getSupplierid() {
		return this.supplierid;
	}

	/**
	 *@generated
	 */
	public void setSupplierid(java.lang.Long value) {
		this.supplierid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getGoodssize() {
		return this.goodssize;
	}

	/**
	 *@generated
	 */
	public void setGoodssize(java.lang.String value) {
		this.goodssize = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getL() {
		return this.l;
	}

	/**
	 *@generated
	 */
	public void setL(java.math.BigDecimal value) {
		this.l = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getW() {
		return this.w;
	}

	/**
	 *@generated
	 */
	public void setW(java.math.BigDecimal value) {
		this.w = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getH() {
		return this.h;
	}

	/**
	 *@generated
	 */
	public void setH(java.math.BigDecimal value) {
		this.h = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getV() {
		return this.v;
	}

	/**
	 *@generated
	 */
	public void setV(java.math.BigDecimal value) {
		this.v = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getGoodscolor() {
		return this.goodscolor;
	}

	/**
	 *@generated
	 */
	public void setGoodscolor(java.lang.String value) {
		this.goodscolor = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getNetweight() {
		return this.netweight;
	}

	/**
	 *@generated
	 */
	public void setNetweight(java.math.BigDecimal value) {
		this.netweight = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGrossweight() {
		return this.grossweight;
	}

	/**
	 *@generated
	 */
	public void setGrossweight(java.math.BigDecimal value) {
		this.grossweight = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getGoodsunit() {
		return this.goodsunit;
	}

	/**
	 *@generated
	 */
	public void setGoodsunit(java.lang.String value) {
		this.goodsunit = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Integer getMinstkpiece() {
		return this.minstkpiece;
	}

	/**
	 *@generated
	 */
	public void setMinstkpiece(java.lang.Integer value) {
		this.minstkpiece = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Integer getMaxstkpiece() {
		return this.maxstkpiece;
	}

	/**
	 *@generated
	 */
	public void setMaxstkpiece(java.lang.Integer value) {
		this.maxstkpiece = value;
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
	public java.lang.String getBarcode() {
		return this.barcode;
	}

	/**
	 *@generated
	 */
	public void setBarcode(java.lang.String value) {
		this.barcode = value;
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

	public java.lang.Long getGoodstypeid() {
		return goodstypeid;
	}

	public void setGoodstypeid(java.lang.Long goodstypeid) {
		this.goodstypeid = goodstypeid;
	}

	/**
	 *@generated
	 */
	public java.lang.String getTracery() {
		return this.tracery;
	}

	/**
	 *@generated
	 */
	public void setTracery(java.lang.String value) {
		this.tracery = value;
	}

	public java.lang.String getHscode() {
		return hscode;
	}

	public void setHscode(java.lang.String hscode) {
		this.hscode = hscode;
	}

	public java.lang.Long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(java.lang.Long customerid) {
		this.customerid = customerid;
	}
	
}