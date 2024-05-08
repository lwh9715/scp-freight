package com.scp.model.price;

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
@Table(name = "price_train")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class PriceTrain implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	@Column(name = "line", length = 30)
	private java.lang.String line;
	
	@Column(name = "pol", length = 30)
	private java.lang.String pol;
	
	@Column(name = "pod", length = 30)
	private java.lang.String pod;
	
	@Column(name = "isrelease")
	private java.lang.Boolean isrelease;
	
	@Column(name = "daterrelease", length = 35)
	private java.util.Date daterrelease;
	
	@Column(name = "datefm", length = 35)
	private java.util.Date datefm;
	
	@Column(name = "dateto", length = 35)
	private java.util.Date dateto;
	
	@Column(name = "remark_in")
	private java.lang.String remark_in;
	
	@Column(name = "remark_out")
	private java.lang.String remark_out;
	
	@Column(name = "corpid")
	private java.lang.Long corpid;
	
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	@Column(name = "inputer", length = 30)
	private java.lang.String inputer;

	@Column(name = "inputtime", length = 35)
	private java.util.Date inputtime;

	@Column(name = "updater", length = 30)
	private java.lang.String updater;

	@Column(name = "updatetime", length = 35)
	private java.util.Date updatetime;
	
	@Column(name = "poa")
	private java.lang.String poa;
	
	@Column(name = "currency", length = 35 )
	private java.lang.String currency;
	
	@Column(name = "costprice")
	private java.math.BigDecimal costprice;
	
	@Column(name = "baseprice")
	private java.math.BigDecimal baseprice;
	
	
	
	@Column(name = "feefreight")
	private java.math.BigDecimal feefreight;
	
	@Column(name = "feecnt")
	private java.math.BigDecimal feecnt;
	
	@Column(name = "feeldyf")
	private java.math.BigDecimal feeldyf;
	
	@Column(name = "cnttype")
	private String cnttype;
	
	@Column(name = "goodstype")
	private String goodstype;
	
	@Column(name = "vbmwgt")
	private java.math.BigDecimal vbmwgt;
	
	@Column(name = "wmsid")
	private Long wmsid;

	@Column(name = "feelz")
	private java.math.BigDecimal feelz;
	
	@Column(name = "feetruck")
	private java.math.BigDecimal feetruck;
	
	@Column(name = "feetlyf")
	private java.math.BigDecimal feetlyf;
	
	@Column(name = "feezc")
	private java.math.BigDecimal feezc;
	
	@Column(name = "feebg")
	private java.math.BigDecimal feebg;
	
	@Column(name = "outprice")
	private java.math.BigDecimal outprice;

	
	@Column(name = "tt")
	private java.lang.String tt;
	
	@Column(name = "llt")
	private java.lang.String llt;
	
	@Column(name = "pdd")
	private java.lang.String pdd;
	
	@Column(name = "transitem")
	private java.lang.String transitem;
	
	@Column(name = "transtype")
	private java.lang.String transtype;
   //slc ADD
	@Column(name="cyid_feefreight")
	private java.lang.String cyidfeefreight;
	@Column(name="cyid_feeldyf")
	private java.lang.String cyidfeeldyf;
	@Column(name="cyid_feelz")
	private java.lang.String cyidfeelz;
	@Column(name="cyid_feetruck")
	private java.lang.String cyidfeetruck;
	@Column(name="cyid_feetlyf")
	private java.lang.String cyidfeetlyf;
	@Column(name="cyid_feezc")
	private java.lang.String cyidfeezc;
	@Column(name="cyid_feebg")
	private java.lang.String cyidfeebg;
	@Column(name="cyid_feecnt")
	private java.lang.String cyidfeecnt;
	
	
	public java.lang.String getCyidfeecnt() {
		return cyidfeecnt;
	}

	public void setCyidfeecnt(java.lang.String cyidfeecnt) {
		this.cyidfeecnt = cyidfeecnt;
	}

	public java.lang.String getCyidfeefreight() {
		return cyidfeefreight;
	}

	public void setCyidfeefreight(java.lang.String cyidfeefreight) {
		this.cyidfeefreight = cyidfeefreight;
	}

	public java.lang.String getCyidfeeldyf() {
		return cyidfeeldyf;
	}

	public void setCyidfeeldyf(java.lang.String cyidfeeldyf) {
		this.cyidfeeldyf = cyidfeeldyf;
	}

	public java.lang.String getCyidfeelz() {
		return cyidfeelz;
	}

	public void setCyidfeelz(java.lang.String cyidfeelz) {
		this.cyidfeelz = cyidfeelz;
	}

	public java.lang.String getCyidfeetruck() {
		return cyidfeetruck;
	}

	public void setCyidfeetruck(java.lang.String cyidfeetruck) {
		this.cyidfeetruck = cyidfeetruck;
	}

	public java.lang.String getCyidfeetlyf() {
		return cyidfeetlyf;
	}

	public void setCyidfeetlyf(java.lang.String cyidfeetlyf) {
		this.cyidfeetlyf = cyidfeetlyf;
	}

	public java.lang.String getCyidfeezc() {
		return cyidfeezc;
	}

	public void setCyidfeezc(java.lang.String cyidfeezc) {
		this.cyidfeezc = cyidfeezc;
	}

	public java.lang.String getCyidfeebg() {
		return cyidfeebg;
	}

	public void setCyidfeebg(java.lang.String cyidfeebg) {
		this.cyidfeebg = cyidfeebg;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getLine() {
		return line;
	}

	public void setLine(java.lang.String line) {
		this.line = line;
	}

	public java.lang.String getPol() {
		return pol;
	}

	public void setPol(java.lang.String pol) {
		this.pol = pol;
	}

	public java.lang.String getPod() {
		return pod;
	}

	public void setPod(java.lang.String pod) {
		this.pod = pod;
	}

	public java.lang.Boolean getIsrelease() {
		return isrelease;
	}

	public void setIsrelease(java.lang.Boolean isrelease) {
		this.isrelease = isrelease;
	}

	public java.util.Date getDaterrelease() {
		return daterrelease;
	}

	public void setDaterrelease(java.util.Date daterrelease) {
		this.daterrelease = daterrelease;
	}

	public java.util.Date getDatefm() {
		return datefm;
	}

	public void setDatefm(java.util.Date datefm) {
		this.datefm = datefm;
	}

	public java.lang.String getRemark_in() {
		return remark_in;
	}

	public void setRemark_in(java.lang.String remarkIn) {
		remark_in = remarkIn;
	}

	public java.lang.String getRemark_out() {
		return remark_out;
	}

	public void setRemark_out(java.lang.String remarkOut) {
		remark_out = remarkOut;
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

	public java.util.Date getDateto() {
		return dateto;
	}

	public void setDateto(java.util.Date dateto) {
		this.dateto = dateto;
	}

	public java.lang.String getPoa() {
		return poa;
	}

	public void setPoa(java.lang.String poa) {
		this.poa = poa;
	}

	public java.lang.String getCurrency() {
		return currency;
	}

	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}

	public java.math.BigDecimal getCostprice() {
		return costprice;
	}

	public void setCostprice(java.math.BigDecimal costprice) {
		this.costprice = costprice;
	}

	public java.math.BigDecimal getBaseprice() {
		return baseprice;
	}

	public void setBaseprice(java.math.BigDecimal baseprice) {
		this.baseprice = baseprice;
	}

	public java.lang.String getTt() {
		return tt;
	}

	public void setTt(java.lang.String tt) {
		this.tt = tt;
	}

	public java.lang.String getLlt() {
		return llt;
	}

	public void setLlt(java.lang.String llt) {
		this.llt = llt;
	}

	public java.lang.String getPdd() {
		return pdd;
	}

	public void setPdd(java.lang.String pdd) {
		this.pdd = pdd;
	}

	public java.lang.String getTransitem() {
		return transitem;
	}

	public void setTransitem(java.lang.String transitem) {
		this.transitem = transitem;
	}

	public java.lang.String getTranstype() {
		return transtype;
	}

	public void setTranstype(java.lang.String transtype) {
		this.transtype = transtype;
	}

	public java.math.BigDecimal getFeefreight() {
		return feefreight;
	}

	public void setFeefreight(java.math.BigDecimal feefreight) {
		this.feefreight = feefreight;
	}

	public java.math.BigDecimal getFeecnt() {
		return feecnt;
	}

	public void setFeecnt(java.math.BigDecimal feecnt) {
		this.feecnt = feecnt;
	}

	public java.math.BigDecimal getFeeldyf() {
		return feeldyf;
	}

	public void setFeeldyf(java.math.BigDecimal feeldyf) {
		this.feeldyf = feeldyf;
	}

	public String getGoodstype() {
		return goodstype;
	}

	public void setGoodstype(String goodstype) {
		this.goodstype = goodstype;
	}

	public java.math.BigDecimal getVbmwgt() {
		return vbmwgt;
	}

	public void setVbmwgt(java.math.BigDecimal vbmwgt) {
		this.vbmwgt = vbmwgt;
	}

	public Long getWmsid() {
		return wmsid;
	}

	public void setWmsid(Long wmsid) {
		this.wmsid = wmsid;
	}

	public java.math.BigDecimal getFeelz() {
		return feelz;
	}

	public void setFeelz(java.math.BigDecimal feelz) {
		this.feelz = feelz;
	}

	public java.math.BigDecimal getFeetruck() {
		return feetruck;
	}

	public void setFeetruck(java.math.BigDecimal feetruck) {
		this.feetruck = feetruck;
	}

	public java.math.BigDecimal getFeetlyf() {
		return feetlyf;
	}

	public void setFeetlyf(java.math.BigDecimal feetlyf) {
		this.feetlyf = feetlyf;
	}

	public java.math.BigDecimal getFeezc() {
		return feezc;
	}

	public void setFeezc(java.math.BigDecimal feezc) {
		this.feezc = feezc;
	}

	public java.math.BigDecimal getFeebg() {
		return feebg;
	}

	public void setFeebg(java.math.BigDecimal feebg) {
		this.feebg = feebg;
	}

	public java.math.BigDecimal getOutprice() {
		return outprice;
	}

	public void setOutprice(java.math.BigDecimal outprice) {
		this.outprice = outprice;
	}

	public String getCnttype() {
		return cnttype;
	}

	public void setCnttype(String cnttype) {
		this.cnttype = cnttype;
	}
	
	
	
}