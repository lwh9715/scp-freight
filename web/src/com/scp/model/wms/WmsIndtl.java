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
@Table(name = "wms_indtl")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class WmsIndtl implements java.io.Serializable {

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
	@Column(name = "inid")
	private java.lang.Long inid;
	
	


	/**
	 *@generated
	 */
	@Column(name = "iscompleted")
	private java.lang.Boolean iscompleted;
	public java.lang.Boolean getIscompleted() {
		return iscompleted;
	}

	public void setIscompleted(java.lang.Boolean iscompleted) {
		this.iscompleted = iscompleted;
	}

	/**
	 *@generated
	 */
	@Column(name = "goodsid")
	private java.lang.Long goodsid;

	/**
	 *@generated
	 */
	@Column(name = "goodscode", length = 50)
	private java.lang.String goodscode;

	/**
	 *@generated
	 */
	@Column(name = "goodsnamec", length = 100)
	private java.lang.String goodsnamec;

	/**
	 *@generated
	 */
	@Column(name = "goodsnamee", length = 100)
	private java.lang.String goodsnamee;

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

	/**
	 *@generated
	 */
	@Column(name = "goodsl")
	private java.math.BigDecimal goodsl;

	/**
	 *@generated
	 */
	@Column(name = "goodsw")
	private java.math.BigDecimal goodsw;

	/**
	 *@generated
	 */
	@Column(name = "goodsh")
	private java.math.BigDecimal goodsh;

	/**
	 *@generated
	 */
	@Column(name = "goodsv")
	private java.math.BigDecimal goodsv;
	
	/**
	 *@generated
	 */
	@Column(name = "gdswgt")
	private java.math.BigDecimal gdswgt;
	
	
	/**
	 *@generated
	 */
	@Column(name = "gdswgtdesc")
	private java.lang.String gdswgtdesc;
	
	@Column(name = "cbmtotle")
	private java.math.BigDecimal cbmtotle;
	
	@Column(name = "wgttotle")
	private java.math.BigDecimal wgttotle;
	/**
	 *@generated
	 */
	@Column(name = "gdscbm")
	private java.math.BigDecimal gdscbm;
	
	@Column(name = "price")
	private java.math.BigDecimal price;
	
	@Column(name = "chargeweight")
	private java.math.BigDecimal chargeweight;
	
	@Column(name = "volwgt")
	private java.math.BigDecimal volwgt;
	
	@Column(name="hscode")
	private String hscode;
	
	
	public String getHscode() {
		return hscode;
	}

	public void setHscode(String hscode) {
		this.hscode = hscode;
	}

	
	public java.math.BigDecimal getChargeweight() {
		return chargeweight;
	}

	public void setChargeweight(java.math.BigDecimal chargeweight) {
		this.chargeweight = chargeweight;
	}

	public java.math.BigDecimal getVolwgt() {
		return volwgt;
	}

	public void setVolwgt(java.math.BigDecimal volwgt) {
		this.volwgt = volwgt;
	}

	public java.math.BigDecimal getGdswgt() {
		return gdswgt;
	}

	public void setGdswgt(java.math.BigDecimal gdswgt) {
		this.gdswgt = gdswgt;
	}

	public java.lang.String getGdswgtdesc() {
		return gdswgtdesc;
	}

	public void setGdswgtdesc(java.lang.String gdswgtdesc) {
		this.gdswgtdesc = gdswgtdesc;
	}

	public java.math.BigDecimal getGdscbm() {
		return gdscbm;
	}

	public void setGdscbm(java.math.BigDecimal gdscbm) {
		this.gdscbm = gdscbm;
	}

	public java.lang.String getGdscbmdesc() {
		return gdscbmdesc;
	}

	public void setGdscbmdesc(java.lang.String gdscbmdesc) {
		this.gdscbmdesc = gdscbmdesc;
	}

	/**
	 *@generated
	 */
	@Column(name = "gdscbmdesc")
	private java.lang.String gdscbmdesc;

	/**
	 *@generated
	 */
	@Column(name = "markno")
	private java.lang.String markno;

	/**
	 *@generated
	 */
	@Column(name = "currency", length = 3)
	private java.lang.String currency;

	/**
	 *@generated
	 */
	@Column(name = "goodsvalue")
	private java.math.BigDecimal goodsvalue;

	/**
	 *@generated
	 */
	@Column(name = "itemno")
	private java.lang.Short itemno;

	/**
	 *@generated
	 */
	@Column(name = "locid")
	private java.lang.Long locid;

	/**
	 *@generated
	 */
	@Column(name = "refid")
	private java.lang.Long refid;

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
	@Column(name = "packagee")
	private java.lang.String packagee;


	public java.lang.String getPackagee() {
		return packagee;
	}

	public void setPackagee(java.lang.String packagee) {
		this.packagee = packagee;
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
	@Column(name = "calctimestart", length = 29)
	private java.util.Date calctimestart;
	
	
	public java.util.Date getCalctimestart() {
		return calctimestart;
	}

	public void setCalctimestart(java.util.Date calctimestart) {
		this.calctimestart = calctimestart;
	}

	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	/**
	 *@generated
	 */
	@Column(name = "box")
	private Integer box;
	
	@Column(name = "pieceinbox")
	private java.math.BigDecimal pieceinbox;
	
	
	public Integer getBox() {
		return box;
	}

	public void setBox(Integer box) {
		this.box = box;
	}

	

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
	public java.lang.Long getInid() {
		return this.inid;
	}

	/**
	 *@generated
	 */
	public void setInid(java.lang.Long value) {
		this.inid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getGoodsid() {
		return this.goodsid;
	}

	/**
	 *@generated
	 */
	public void setGoodsid(java.lang.Long value) {
		this.goodsid = value;
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
	public java.lang.String getGoodsnamec() {
		return this.goodsnamec;
	}

	/**
	 *@generated
	 */
	public void setGoodsnamec(java.lang.String value) {
		this.goodsnamec = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getGoodsnamee() {
		return this.goodsnamee;
	}

	/**
	 *@generated
	 */
	public void setGoodsnamee(java.lang.String value) {
		this.goodsnamee = value;
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
	public java.math.BigDecimal getGoodsl() {
		return this.goodsl;
	}

	/**
	 *@generated
	 */
	public void setGoodsl(java.math.BigDecimal value) {
		this.goodsl = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGoodsw() {
		return this.goodsw;
	}

	/**
	 *@generated
	 */
	public void setGoodsw(java.math.BigDecimal value) {
		this.goodsw = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGoodsh() {
		return this.goodsh;
	}

	/**
	 *@generated
	 */
	public void setGoodsh(java.math.BigDecimal value) {
		this.goodsh = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGoodsv() {
		return this.goodsv;
	}

	/**
	 *@generated
	 */
	public void setGoodsv(java.math.BigDecimal value) {
		this.goodsv = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getMarkno() {
		return this.markno;
	}

	/**
	 *@generated
	 */
	public void setMarkno(java.lang.String value) {
		this.markno = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCurrency() {
		return this.currency;
	}

	/**
	 *@generated
	 */
	public void setCurrency(java.lang.String value) {
		this.currency = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGoodsvalue() {
		return this.goodsvalue;
	}

	/**
	 *@generated
	 */
	public void setGoodsvalue(java.math.BigDecimal value) {
		this.goodsvalue = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getItemno() {
		return this.itemno;
	}

	/**
	 *@generated
	 */
	public void setItemno(java.lang.Short value) {
		this.itemno = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getLocid() {
		return this.locid;
	}

	/**
	 *@generated
	 */
	public void setLocid(java.lang.Long value) {
		this.locid = value;
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

	public java.math.BigDecimal getPieceinbox() {
		return pieceinbox;
	}

	public void setPieceinbox(java.math.BigDecimal pieceinbox) {
		this.pieceinbox = pieceinbox;
	}

	/**
	 *@generated
	 */
	public void setIsdelete(java.lang.Boolean value) {
		this.isdelete = value;
	}

	public java.math.BigDecimal getPrice() {
		return price;
	}

	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}

	public java.math.BigDecimal getCbmtotle() {
		return cbmtotle;
	}

	public void setCbmtotle(java.math.BigDecimal cbmtotle) {
		this.cbmtotle = cbmtotle;
	}

	public java.math.BigDecimal getWgttotle() {
		return wgttotle;
	}

	public void setWgttotle(java.math.BigDecimal wgttotle) {
		this.wgttotle = wgttotle;
	}
	
}
