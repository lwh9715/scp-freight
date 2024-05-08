package com.scp.model.bus;

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
@Table(name = "bus_goods")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusGoods implements java.io.Serializable {

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
	@Column(name = "goodscode")
	private java.lang.String goodscode;
	
	@Column(name = "goodsname")
	private java.lang.String goodsname;
	
	
	@Column(name = "piece")
	private java.lang.Integer piece;
	
	@Column(name = "piece2")
	private java.lang.Integer piece2;
	
	public java.lang.Integer getPiece2() {
		return piece2;
	}

	public void setPiece2(java.lang.Integer piece2) {
		this.piece2 = piece2;
	}

	@Column(name = "grswgt")
	private java.math.BigDecimal grswgt;
	
	@Column(name = "netwgt")
	private java.math.BigDecimal netwgt;
	
	@Column(name = "cbm")
	private java.math.BigDecimal cbm;
	
	@Column(name = "markno")
	private java.lang.String markno;
	
	@Column(name = "packid")
	private java.lang.Long packid;
	
	@Column(name = "gdsprice")
	private java.math.BigDecimal gdsprice;
	
	@Column(name = "gdsvalue")
	private java.math.BigDecimal gdsvalue;
	
	/**
	 *@generated
	 */
	@Column(name = "currency", length = 3)
	private java.lang.String currency;
	
	/**
	 *@generated
	 */
	@Column(name = "linkid")
	private java.lang.Long linkid;
	
	/**
	 *@generated
	 */
	@Column(name = "linktbl")
	private java.lang.String linktbl;
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
	
	@Column(name = "hscode", length = 30)
	private java.lang.String hscode;
	
	@Column(name = "factory")
	private java.lang.String factory;
	
	@Column(name = "material", length = 100)
	private java.lang.String material;
	
	@Column(name = "goodscode2", length = 100)
	private java.lang.String goodscode2;
	
	@Column(name = "l")
	private java.math.BigDecimal l;
	
	@Column(name = "w")
	private java.math.BigDecimal w;
	
	@Column(name = "h")
	private java.math.BigDecimal h;
	
	@Column(name = "volumeweight")
	private java.math.BigDecimal volumeweight;
	
	@Column(name = "chargeweight")
	private java.math.BigDecimal chargeweight;
	
	@Column(name = "packagee")
	private java.lang.String packagee;
	

	public java.lang.String getGoodscode2() {
		return goodscode2;
	}

	public void setGoodscode2(java.lang.String goodscode2) {
		this.goodscode2 = goodscode2;
	}

	public long getId() {
		return id;
	}

	public java.lang.String getGoodscode() {
		return goodscode;
	}

	public java.lang.String getGoodsname() {
		return goodsname;
	}

	public java.lang.Integer getPiece() {
		return piece;
	}

	public java.math.BigDecimal getGrswgt() {
		return grswgt;
	}

	public java.math.BigDecimal getNetwgt() {
		return netwgt;
	}

	public java.math.BigDecimal getCbm() {
		return cbm;
	}

	public java.lang.String getMarkno() {
		return markno;
	}

	public java.lang.Long getPackid() {
		return packid;
	}

	public java.math.BigDecimal getGdsprice() {
		return gdsprice;
	}

	public java.math.BigDecimal getGdsvalue() {
		return gdsvalue;
	}

	public java.lang.String getCurrency() {
		return currency;
	}

	public java.lang.Long getLinkid() {
		return linkid;
	}

	public java.lang.String getLinktbl() {
		return linktbl;
	}

	public java.lang.Long getCorpid() {
		return corpid;
	}

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public java.lang.String getInputer() {
		return inputer;
	}

	public java.util.Date getInputtime() {
		return inputtime;
	}

	public java.lang.String getUpdater() {
		return updater;
	}

	public java.util.Date getUpdatetime() {
		return updatetime;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setGoodscode(java.lang.String goodscode) {
		this.goodscode = goodscode;
	}

	public void setGoodsname(java.lang.String goodsname) {
		this.goodsname = goodsname;
	}

	public void setPiece(java.lang.Integer piece) {
		this.piece = piece;
	}

	public void setGrswgt(java.math.BigDecimal grswgt) {
		this.grswgt = grswgt;
	}

	public void setNetwgt(java.math.BigDecimal netwgt) {
		this.netwgt = netwgt;
	}

	public void setCbm(java.math.BigDecimal cbm) {
		this.cbm = cbm;
	}

	public void setMarkno(java.lang.String markno) {
		this.markno = markno;
	}

	public void setPackid(java.lang.Long packid) {
		this.packid = packid;
	}

	public void setGdsprice(java.math.BigDecimal gdsprice) {
		this.gdsprice = gdsprice;
	}

	public void setGdsvalue(java.math.BigDecimal gdsvalue) {
		this.gdsvalue = gdsvalue;
	}

	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}

	public void setLinkid(java.lang.Long linkid) {
		this.linkid = linkid;
	}

	public void setLinktbl(java.lang.String linktbl) {
		this.linktbl = linktbl;
	}

	public void setCorpid(java.lang.Long corpid) {
		this.corpid = corpid;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
	}

	public void setInputer(java.lang.String inputer) {
		this.inputer = inputer;
	}

	public void setInputtime(java.util.Date inputtime) {
		this.inputtime = inputtime;
	}

	public void setUpdater(java.lang.String updater) {
		this.updater = updater;
	}

	public void setUpdatetime(java.util.Date updatetime) {
		this.updatetime = updatetime;
	}

	public java.lang.String getHscode() {
		return hscode;
	}

	public void setHscode(java.lang.String hscode) {
		this.hscode = hscode;
	}

	public java.lang.String getFactory() {
		return factory;
	}

	public void setFactory(java.lang.String factory) {
		this.factory = factory;
	}

	public java.lang.String getMaterial() {
		return material;
	}

	public void setMaterial(java.lang.String material) {
		this.material = material;
	}

	public java.math.BigDecimal getL() {
		return l;
	}

	public void setL(java.math.BigDecimal l) {
		this.l = l;
	}

	public java.math.BigDecimal getW() {
		return w;
	}

	public void setW(java.math.BigDecimal w) {
		this.w = w;
	}

	public java.math.BigDecimal getH() {
		return h;
	}

	public void setH(java.math.BigDecimal h) {
		this.h = h;
	}

	public java.math.BigDecimal getVolumeweight() {
		return volumeweight;
	}

	public void setVolumeweight(java.math.BigDecimal volumeweight) {
		this.volumeweight = volumeweight;
	}

	public java.math.BigDecimal getChargeweight() {
		return chargeweight;
	}

	public void setChargeweight(java.math.BigDecimal chargeweight) {
		this.chargeweight = chargeweight;
	}

	public java.lang.String getPackagee() {
		return packagee;
	}

	public void setPackagee(java.lang.String packagee) {
		this.packagee = packagee;
	}
	
}