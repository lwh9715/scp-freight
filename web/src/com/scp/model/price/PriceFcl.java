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
 * Date:2016-6-20 FCL运价主表：price_fcl
 * 
 * @author bruce
 */
@Table(name = "price_fcl")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class PriceFcl implements java.io.Serializable {
	
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

	@Column(name = "country", length = 30)
	private java.lang.String country;

	@Column(name = "shipping", length = 30)
	private java.lang.String shipping;

	@Column(name = "schedule", length = 30)
	private java.lang.String schedule;

	@Column(name = "tt", length = 30)
	private java.lang.String tt;

	@Column(name = "cls", length = 13)
	private java.util.Date cls;

	@Column(name = "etd", length = 13)
	private java.util.Date etd;
	
	@Column(name = "isrelease")
	private java.lang.Boolean isrelease;

	@Column(name = "daterelease", length = 13)
	private java.util.Date daterelease;

	@Column(name = "datefm", length = 13)
	private java.util.Date datefm;

	@Column(name = "dateto", length = 13)
	private java.util.Date dateto;

	@Column(name = "cost20")
	private java.math.BigDecimal cost20;

	@Column(name = "cost40gp")
	private java.math.BigDecimal cost40gp;

	@Column(name = "cost40hq")
	private java.math.BigDecimal cost40hq;

	@Column(name = "remark_ship")
	private java.lang.String remark_ship;

	@Column(name = "remark_fee")
	private java.lang.String remark_fee;

	@Column(name = "contacter")
	private java.lang.String contacter;

	@Column(name = "remark_in")
	private java.lang.String remark_in;

	@Column(name = "remark_out")
	private java.lang.String remark_out;

	@Column(name = "inputer", length = 30)
	private java.lang.String inputer;

	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;

	@Column(name = "updater", length = 30)
	private java.lang.String updater;

	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;
	
	@Column(name = "shipline")
	private java.lang.String shipline;
	
	@Column(name = "cntypeothercode")
	private java.lang.String cntypeothercode;
	
	@Column(name = "pieceother")
	private java.lang.Short pieceother;
	
	@Column(name = "contractno", length = 30)
	private java.lang.String contractno;
	
	@Column(name = "priceuserid")
	private java.lang.Long priceuserid;
	
	@Column(name = "typestart", length = 5)
	private java.lang.String typestart;
	
	@Column(name = "typeend", length = 5)
	private java.lang.String typeend;
	
	@Column(name = "currency", length = 3)
	private java.lang.String currency;
	
	@Column(name = "cost45hq")
	private java.math.BigDecimal cost45hq;
	
	@Column(name = "bargedatefm", length = 13)
	private java.util.Date bargedatefm;

	@Column(name = "bargedateto", length = 13)
	private java.util.Date bargedateto;
	
	@Column(name = "bargetype", length = 10)
	private java.lang.String bargetype;
	
	@Column(name = "bargetypend", length = 10)
	private java.lang.String bargetypend;
	
	@Column(name = "isinvalid")
	private java.lang.Boolean isinvalid;
	
	@Column(name = "bargepod", length = 30)
	private java.lang.String bargepod;
	
	@Column(name = "pricename")
	private java.lang.String pricename;
	
	@Column(name = "corpid")
	private java.lang.Long corpid;
	
	@Column(name = "istop")
	private java.lang.Boolean istop;
	
	@Column(name = "typestart2", length = 5)
	private java.lang.String typestart2;
	
	@Column(name = "typeend2", length = 5)
	private java.lang.String typeend2;
	
	@Column(name = "bartypestart2", length = 5)
	private java.lang.String bartypestart2;
	
	@Column(name = "bartypeend2", length = 5)
	private java.lang.String bartypeend2;
	
	@Column(name = "bargetypestr2", length = 5)
	private java.lang.String bargetypestr2;
	
	@Column(name = "bargetypend2", length = 5)
	private java.lang.String bargetypend2;
	
	@Column(name = "baronend", length = 5)
	private java.lang.String baronend;
	
	@Column(name = "carrier", length = 30)
	private java.lang.String carrier;
	
	@Column(name = "pollink")
	private java.lang.String pollink;
	
	@Column(name = "base20")
	private java.math.BigDecimal base20;
	
	@Column(name = "base40gp")
	private java.math.BigDecimal base40gp;
	
	@Column(name = "base40hq")
	private java.math.BigDecimal base40hq;
	
	@Column(name = "base45hq")
	private java.math.BigDecimal base45hq;
	
	@Column(name = "baseother")
	private java.math.BigDecimal baseother;
	
	@Column(name = "pricetype")
	private java.lang.String pricetype;
	
	@Column(name = "freetime")
	private java.lang.String freetime;
	
	@Column(name = "closingtime", length = 13)
	private java.util.Date closingtime;
	
	@Column(name = "vessel")
	private java.lang.String vessel;
	
	@Column(name = "ispush")
	private java.lang.Boolean ispush;
	
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

	public java.lang.String getVia() {
		return via;
	}

	public void setVia(java.lang.String via) {
		this.via = via;
	}

	public java.lang.String getCountry() {
		return country;
	}

	public void setCountry(java.lang.String country) {
		this.country = country;
	}

	public java.lang.String getShipping() {
		return shipping;
	}

	public void setShipping(java.lang.String shipping) {
		this.shipping = shipping;
	}

	public java.lang.String getSchedule() {
		return schedule;
	}

	public void setSchedule(java.lang.String schedule) {
		this.schedule = schedule;
	}

	public java.lang.String getTt() {
		return tt;
	}

	public void setTt(java.lang.String tt) {
		this.tt = tt;
	}

	public java.util.Date getCls() {
		return cls;
	}

	public void setCls(java.util.Date cls) {
		this.cls = cls;
	}

	public java.util.Date getEtd() {
		return etd;
	}

	public void setEtd(java.util.Date etd) {
		this.etd = etd;
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

	public java.math.BigDecimal getCost20() {
		return cost20;
	}

	public void setCost20(java.math.BigDecimal cost20) {
		this.cost20 = cost20;
	}

	public java.math.BigDecimal getCost40gp() {
		return cost40gp;
	}

	public void setCost40gp(java.math.BigDecimal cost40gp) {
		this.cost40gp = cost40gp;
	}

	public java.math.BigDecimal getCost40hq() {
		return cost40hq;
	}

	public void setCost40hq(java.math.BigDecimal cost40hq) {
		this.cost40hq = cost40hq;
	}

	public java.lang.String getRemark_ship() {
		return remark_ship;
	}

	public void setRemark_ship(java.lang.String remarkShip) {
		remark_ship = remarkShip;
	}

	public java.lang.String getRemark_fee() {
		return remark_fee;
	}

	public void setRemark_fee(java.lang.String remarkFee) {
		remark_fee = remarkFee;
	}

	public java.lang.String getContacter() {
		return contacter;
	}

	public void setContacter(java.lang.String contacter) {
		this.contacter = contacter;
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

	public java.lang.String getLinecode() {
		return linecode;
	}

	public void setLinecode(java.lang.String linecode) {
		this.linecode = linecode;
	}

	public java.lang.String getShipline() {
		return shipline;
	}

	public void setShipline(java.lang.String shipline) {
		this.shipline = shipline;
	}

	public java.lang.String getCntypeothercode() {
		return cntypeothercode;
	}

	public void setCntypeothercode(java.lang.String cntypeothercode) {
		this.cntypeothercode = cntypeothercode;
	}

	public java.lang.Short getPieceother() {
		return pieceother;
	}

	public void setPieceother(java.lang.Short pieceother) {
		this.pieceother = pieceother;
	}

	public java.lang.String getContractno() {
		return contractno;
	}

	public void setContractno(java.lang.String contractno) {
		this.contractno = contractno;
	}

	public java.lang.Long getPriceuserid() {
		return priceuserid;
	}

	public void setPriceuserid(java.lang.Long priceuserid) {
		this.priceuserid = priceuserid;
	}

	public java.lang.String getTypestart() {
		return typestart;
	}

	public void setTypestart(java.lang.String typestart) {
		this.typestart = typestart;
	}

	public java.lang.String getTypeend() {
		return typeend;
	}

	public void setTypeend(java.lang.String typeend) {
		this.typeend = typeend;
	}

	public java.lang.String getCurrency() {
		return currency;
	}

	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}

	public java.math.BigDecimal getCost45hq() {
		return cost45hq;
	}

	public void setCost45hq(java.math.BigDecimal cost45hq) {
		this.cost45hq = cost45hq;
	}

	public java.util.Date getBargedatefm() {
		return bargedatefm;
	}

	public void setBargedatefm(java.util.Date bargedatefm) {
		this.bargedatefm = bargedatefm;
	}

	public java.util.Date getBargedateto() {
		return bargedateto;
	}

	public void setBargedateto(java.util.Date bargedateto) {
		this.bargedateto = bargedateto;
	}

	public java.lang.String getBargetype() {
		return bargetype;
	}

	public void setBargetype(java.lang.String bargetype) {
		this.bargetype = bargetype;
	}

	public java.lang.String getBargetypend() {
		return bargetypend;
	}

	public void setBargetypend(java.lang.String bargetypend) {
		this.bargetypend = bargetypend;
	}

	public java.lang.Boolean getIsinvalid() {
		return isinvalid;
	}

	public void setIsinvalid(java.lang.Boolean isinvalid) {
		this.isinvalid = isinvalid;
	}

	public java.lang.String getBargepod() {
		return bargepod;
	}

	public void setBargepod(java.lang.String bargepod) {
		this.bargepod = bargepod;
	}

	public java.lang.String getPricename() {
		return pricename;
	}

	public void setPricename(java.lang.String pricename) {
		this.pricename = pricename;
	}

	public java.lang.Long getCorpid() {
		return corpid;
	}

	public void setCorpid(java.lang.Long corpid) {
		this.corpid = corpid;
	}

	public java.lang.Boolean getIstop() {
		return istop;
	}

	public void setIstop(java.lang.Boolean istop) {
		this.istop = istop;
	}

	public java.lang.String getTypestart2() {
		return typestart2;
	}

	public void setTypestart2(java.lang.String typestart2) {
		this.typestart2 = typestart2;
	}

	public java.lang.String getTypeend2() {
		return typeend2;
	}

	public void setTypeend2(java.lang.String typeend2) {
		this.typeend2 = typeend2;
	}

	public java.lang.String getBartypestart2() {
		return bartypestart2;
	}

	public void setBartypestart2(java.lang.String bartypestart2) {
		this.bartypestart2 = bartypestart2;
	}

	public java.lang.String getBartypeend2() {
		return bartypeend2;
	}

	public void setBartypeend2(java.lang.String bartypeend2) {
		this.bartypeend2 = bartypeend2;
	}

	public java.lang.String getBargetypestr2() {
		return bargetypestr2;
	}

	public void setBargetypestr2(java.lang.String bargetypestr2) {
		this.bargetypestr2 = bargetypestr2;
	}

	public java.lang.String getBargetypend2() {
		return bargetypend2;
	}

	public void setBargetypend2(java.lang.String bargetypend2) {
		this.bargetypend2 = bargetypend2;
	}

	public java.lang.String getBaronend() {
		return baronend;
	}

	public void setBaronend(java.lang.String baronend) {
		this.baronend = baronend;
	}

	public java.lang.String getCarrier() {
		return carrier;
	}

	public void setCarrier(java.lang.String carrier) {
		this.carrier = carrier;
	}

	public java.lang.String getPollink() {
		return pollink;
	}

	public void setPollink(java.lang.String pollink) {
		this.pollink = pollink;
	}

	public java.math.BigDecimal getBase20() {
		return base20;
	}

	public void setBase20(java.math.BigDecimal base20) {
		this.base20 = base20;
	}

	public java.math.BigDecimal getBase40gp() {
		return base40gp;
	}

	public void setBase40gp(java.math.BigDecimal base40gp) {
		this.base40gp = base40gp;
	}

	public java.math.BigDecimal getBase40hq() {
		return base40hq;
	}

	public void setBase40hq(java.math.BigDecimal base40hq) {
		this.base40hq = base40hq;
	}

	public java.math.BigDecimal getBaseother() {
		return baseother;
	}

	public void setBaseother(java.math.BigDecimal baseother) {
		this.baseother = baseother;
	}

	public java.math.BigDecimal getBase45hq() {
		return base45hq;
	}

	public void setBase45hq(java.math.BigDecimal base45hq) {
		this.base45hq = base45hq;
	}

	public java.lang.String getPricetype() {
		return pricetype;
	}

	public void setPricetype(java.lang.String pricetype) {
		this.pricetype = pricetype;
	}

	public java.lang.String getFreetime() {
		return freetime;
	}

	public void setFreetime(java.lang.String freetime) {
		this.freetime = freetime;
	}

	public java.util.Date getClosingtime() {
		return closingtime;
	}

	public void setClosingtime(java.util.Date closingtime) {
		this.closingtime = closingtime;
	}

	public java.lang.String getVessel() {
		return vessel;
	}

	public void setVessel(java.lang.String vessel) {
		this.vessel = vessel;
	}

	public java.lang.Boolean getIspush() {
		return ispush;
	}

	public void setIspush(java.lang.Boolean ispush) {
		this.ispush = ispush;
	}
}
