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
@Table(name = "price_air")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class PriceAir implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	@Column(name = "line", length = 30)
	private java.lang.String line;
	
	@Column(name = "linecode", length = 30)
	private java.lang.String linecode;
	
	@Column(name = "pol", length = 30)
	private java.lang.String pol;
	
	@Column(name = "pod", length = 30)
	private java.lang.String pod;
	
	@Column(name = "via", length = 30)
	private java.lang.String via;
	
	@Column(name = "bkagentid")
	private java.lang.Long bkagentid;
	
	@Column(name = "carrierid")
	private java.lang.Long carrierid;
	
	@Column(name = "isrelease")
	private java.lang.Boolean isrelease;
	
	@Column(name = "daterelease", length = 35)
	private java.util.Date daterelease;
	
	@Column(name = "datefm", length = 35)
	private java.util.Date datefm;
	
	@Column(name = "dateto", length = 35)
	private java.util.Date dateto;
	
	@Column(name = "costkeji", length = 100)
	private java.lang.String costkeji;
	
	@Column(name = "costhuoji", length = 100)
	private java.lang.String costhuoji;
	
	@Column(name = "costpaohuo", length = 100)
	private java.lang.String costpaohuo;
	
	@Column(name = "costbiaoyun", length = 100)
	private java.lang.String costbiaoyun;
	
	@Column(name = "costbiaoyunp", length = 100)
	private java.lang.String costbiaoyunp;
	
	@Column(name = "costkuaiyun", length = 100)
	private java.lang.String costkuaiyun;
	
	@Column(name = "cost1300", length = 100)
	private java.lang.String cost1300;
	
	@Column(name = "cost1500", length = 100)
	private java.lang.String cost1500;
	
	@Column(name = "m")
	private java.math.BigDecimal m;
	
	@Column(name = "n")
	private java.math.BigDecimal n;
	
	@Column(name = "ry")
	private java.math.BigDecimal ry;
	
	@Column(name = "zx")
	private java.math.BigDecimal zx;
	
	@Column(name = "gzf")
	private java.math.BigDecimal gzf;
	
	@Column(name = "bt")
	private java.math.BigDecimal bt;
	
	@Column(name = "truck")
	private java.math.BigDecimal truck;
	
	@Column(name = "doc")
	private java.math.BigDecimal doc;
	
	@Column(name = "bgf")
	private java.math.BigDecimal bgf;
	
	@Column(name = "rcf")
	private java.math.BigDecimal rcf;
	
	@Column(name = "amsens")
	private java.math.BigDecimal amsens;
	
	@Column(name = "byf")
	private java.math.BigDecimal byf;
	
	@Column(name = "other")
	private java.math.BigDecimal other;
	
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
	
	@Column(name = "airflight", length = 20)
	private java.lang.String airflight;

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

	public java.lang.String getLinecode() {
		return linecode;
	}

	public void setLinecode(java.lang.String linecode) {
		this.linecode = linecode;
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

	public java.lang.String getVia() {
		return via;
	}

	public void setVia(java.lang.String via) {
		this.via = via;
	}

	public java.lang.Long getBkagentid() {
		return bkagentid;
	}

	public void setBkagentid(java.lang.Long bkagentid) {
		this.bkagentid = bkagentid;
	}

	public java.lang.Boolean getIsrelease() {
		return isrelease;
	}

	public void setIsrelease(java.lang.Boolean isrelease) {
		this.isrelease = isrelease;
	}

	public java.util.Date getDaterelease() {
		return daterelease;
	}

	public void setDaterelease(java.util.Date daterelease) {
		this.daterelease = daterelease;
	}

	public java.util.Date getDatefm() {
		return datefm;
	}

	public void setDatefm(java.util.Date datefm) {
		this.datefm = datefm;
	}

	public java.util.Date getDateto() {
		return dateto;
	}

	public void setDateto(java.util.Date dateto) {
		this.dateto = dateto;
	}

	public java.lang.String getCostkeji() {
		return costkeji;
	}

	public void setCostkeji(java.lang.String costkeji) {
		this.costkeji = costkeji;
	}

	public java.lang.String getCosthuoji() {
		return costhuoji;
	}

	public void setCosthuoji(java.lang.String costhuoji) {
		this.costhuoji = costhuoji;
	}

	public java.lang.String getCostpaohuo() {
		return costpaohuo;
	}

	public void setCostpaohuo(java.lang.String costpaohuo) {
		this.costpaohuo = costpaohuo;
	}

	public java.lang.String getCostbiaoyun() {
		return costbiaoyun;
	}

	public void setCostbiaoyun(java.lang.String costbiaoyun) {
		this.costbiaoyun = costbiaoyun;
	}

	public java.lang.String getCostbiaoyunp() {
		return costbiaoyunp;
	}

	public void setCostbiaoyunp(java.lang.String costbiaoyunp) {
		this.costbiaoyunp = costbiaoyunp;
	}

	public java.lang.String getCostkuaiyun() {
		return costkuaiyun;
	}

	public void setCostkuaiyun(java.lang.String costkuaiyun) {
		this.costkuaiyun = costkuaiyun;
	}

	public java.lang.String getCost1300() {
		return cost1300;
	}

	public void setCost1300(java.lang.String cost1300) {
		this.cost1300 = cost1300;
	}

	public java.lang.String getCost1500() {
		return cost1500;
	}

	public void setCost1500(java.lang.String cost1500) {
		this.cost1500 = cost1500;
	}

	public java.math.BigDecimal getM() {
		return m;
	}

	public void setM(java.math.BigDecimal m) {
		this.m = m;
	}

	public java.math.BigDecimal getN() {
		return n;
	}

	public void setN(java.math.BigDecimal n) {
		this.n = n;
	}

	public java.math.BigDecimal getRy() {
		return ry;
	}

	public void setRy(java.math.BigDecimal ry) {
		this.ry = ry;
	}

	public java.math.BigDecimal getZx() {
		return zx;
	}

	public void setZx(java.math.BigDecimal zx) {
		this.zx = zx;
	}

	public java.math.BigDecimal getGzf() {
		return gzf;
	}

	public void setGzf(java.math.BigDecimal gzf) {
		this.gzf = gzf;
	}

	public java.math.BigDecimal getBt() {
		return bt;
	}

	public void setBt(java.math.BigDecimal bt) {
		this.bt = bt;
	}

	public java.math.BigDecimal getTruck() {
		return truck;
	}

	public void setTruck(java.math.BigDecimal truck) {
		this.truck = truck;
	}

	public java.math.BigDecimal getDoc() {
		return doc;
	}

	public void setDoc(java.math.BigDecimal doc) {
		this.doc = doc;
	}

	public java.math.BigDecimal getBgf() {
		return bgf;
	}

	public void setBgf(java.math.BigDecimal bgf) {
		this.bgf = bgf;
	}

	public java.math.BigDecimal getRcf() {
		return rcf;
	}

	public void setRcf(java.math.BigDecimal rcf) {
		this.rcf = rcf;
	}

	public java.math.BigDecimal getAmsens() {
		return amsens;
	}

	public void setAmsens(java.math.BigDecimal amsens) {
		this.amsens = amsens;
	}

	public java.math.BigDecimal getByf() {
		return byf;
	}

	public void setByf(java.math.BigDecimal byf) {
		this.byf = byf;
	}

	public java.math.BigDecimal getOther() {
		return other;
	}

	public void setOther(java.math.BigDecimal other) {
		this.other = other;
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

	public java.lang.Long getCarrierid() {
		return carrierid;
	}

	public void setCarrierid(java.lang.Long carrierid) {
		this.carrierid = carrierid;
	}

	public java.lang.String getAirflight() {
		return airflight;
	}

	public void setAirflight(java.lang.String airflight) {
		this.airflight = airflight;
	}
	
}