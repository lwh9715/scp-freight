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
@Table(name = "del_deliverydtl")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class DelDeliverydtl implements java.io.Serializable {

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
	@Column(name = "customerid")
	private java.lang.Long customerid;

	/**
	 *@generated
	 */
	@Column(name = "customerabbr", length = 50)
	private java.lang.String customerabbr;

	/**
	 *@generated
	 */
	@Column(name = "itemno")
	private java.lang.Short itemno;

	/**
	 *@generated
	 */
	@Column(name = "truckid")
	private java.lang.Long truckid;

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

	/**
	 *@generated
	 */
	@Column(name = "sendaddress")
	private java.lang.String sendaddress;

	/**
	 *@generated
	 */
	@Column(name = "issign")
	private java.lang.Boolean issign;

	/**
	 *@generated
	 */
	@Column(name = "signtime", length = 29)
	private java.util.Date signtime;

	/**
	 *@generated
	 */
	@Column(name = "signer", length = 100)
	private java.lang.String signer;

	/**
	 *@generated
	 */
	@Column(name = "isreturn")
	private java.lang.Boolean isreturn;

	/**
	 *@generated
	 */
	@Column(name = "sendremarks")
	private java.lang.String sendremarks;

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
	public java.lang.Long getCustomerid() {
		return this.customerid;
	}

	/**
	 *@generated
	 */
	public void setCustomerid(java.lang.Long value) {
		this.customerid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCustomerabbr() {
		return this.customerabbr;
	}

	/**
	 *@generated
	 */
	public void setCustomerabbr(java.lang.String value) {
		this.customerabbr = value;
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
	public java.lang.Long getTruckid() {
		return this.truckid;
	}

	/**
	 *@generated
	 */
	public void setTruckid(java.lang.Long value) {
		this.truckid = value;
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
	public java.math.BigDecimal getGdswgt() {
		return this.gdswgt;
	}

	/**
	 *@generated
	 */
	public void setGdswgt(java.math.BigDecimal value) {
		this.gdswgt = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGdscbm() {
		return this.gdscbm;
	}

	/**
	 *@generated
	 */
	public void setGdscbm(java.math.BigDecimal value) {
		this.gdscbm = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getPieceinbox() {
		return this.pieceinbox;
	}

	/**
	 *@generated
	 */
	public void setPieceinbox(java.math.BigDecimal value) {
		this.pieceinbox = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getSendaddress() {
		return this.sendaddress;
	}

	/**
	 *@generated
	 */
	public void setSendaddress(java.lang.String value) {
		this.sendaddress = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIssign() {
		return this.issign;
	}

	/**
	 *@generated
	 */
	public void setIssign(java.lang.Boolean value) {
		this.issign = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getSigntime() {
		return this.signtime;
	}

	/**
	 *@generated
	 */
	public void setSigntime(java.util.Date value) {
		this.signtime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getSigner() {
		return this.signer;
	}

	/**
	 *@generated
	 */
	public void setSigner(java.lang.String value) {
		this.signer = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIsreturn() {
		return this.isreturn;
	}

	/**
	 *@generated
	 */
	public void setIsreturn(java.lang.Boolean value) {
		this.isreturn = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getSendremarks() {
		return this.sendremarks;
	}

	/**
	 *@generated
	 */
	public void setSendremarks(java.lang.String value) {
		this.sendremarks = value;
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
}