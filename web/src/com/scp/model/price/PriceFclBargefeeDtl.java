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
 * Date:2018-10-11 驳船费明细
 * 
 * @author hunk
 */
@Table(name = "price_fcl_bargefeedtl")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class PriceFclBargefeeDtl implements java.io.Serializable {
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	@Column(name = "bargefeeid")
	private java.lang.Long bargefeeid;
	
	@Column(name = "polnamee", length = 30)
	private java.lang.String polnamee;
	
	@Column(name = "polnamec", length = 30)
	private java.lang.String polnamec;
	
	@Column(name = "podnamee", length = 30)
	private java.lang.String podnamee;
	
	@Column(name = "podnamec", length = 30)
	private java.lang.String podnamec;
	
	@Column(name = "cost20")
	private java.math.BigDecimal cost20;

	@Column(name = "cost40gp")
	private java.math.BigDecimal cost40gp;

	@Column(name = "cost40hq")
	private java.math.BigDecimal cost40hq;
	
	@Column(name = "cost202")
	private java.math.BigDecimal cost202;

	@Column(name = "cost40gp2")
	private java.math.BigDecimal cost40gp2;

	@Column(name = "cost40hq2")
	private java.math.BigDecimal cost40hq2;
	
	@Column(name = "area", length = 30)
	private java.lang.String area;
	
	@Column(name = "line")
	private java.lang.String line;
	
	@Column(name = "remark")
	private java.lang.String remark;
	
	@Column(name = "currency", length = 10)
	private java.lang.String currency;
	
	@Column(name = "isinvalid")
	private java.lang.Boolean isinvalid;
	
	@Column(name = "datefm")
	private java.util.Date datefm;
	
	@Column(name = "dateto")
	private java.util.Date dateto;
	
	@Column(name = "conditiontype", length = 10)
	private java.lang.String conditiontype;
	
	@Column(name = "condition", length = 10)
	private java.lang.String condition;
	
	@Column(name = "conditionvalue")
	private java.lang.String conditionvalue;
	
	@Column(name = "conditiontype2", length = 10)
	private java.lang.String conditiontype2;
	
	@Column(name = "condition2", length = 10)
	private java.lang.String condition2;
	
	@Column(name = "conditionvalue2")
	private java.lang.String conditionvalue2;
	
	@Column(name = "corpid")
	private java.lang.Long corpid;
	
	@Column(name = "cost45hq")
	private java.math.BigDecimal cost45hq;

	@Column(name = "cost45hq2")
	private java.math.BigDecimal cost45hq2;
	
	@Column(name = "isrelease")
	private java.lang.Boolean isrelease;
	
	@Column(name = "istop")
	private java.lang.Boolean istop;
	
	@Column(name = "schedule", length = 30)
	private java.lang.String schedule;
	
	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;

	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;
	
	@Column(name = "updater", length = 30)
	private java.lang.String updater;

	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;


	@Column(name = "shipcarrier")
	private java.lang.String shipcarrier;


	public String getShipcarrier() {
		return shipcarrier;
	}

	public void setShipcarrier(String shipcarrier) {
		this.shipcarrier = shipcarrier;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.Long getBargefeeid() {
		return bargefeeid;
	}

	public void setBargefeeid(java.lang.Long bargefeeid) {
		this.bargefeeid = bargefeeid;
	}

	public java.lang.String getPolnamee() {
		return polnamee;
	}

	public void setPolnamee(java.lang.String polnamee) {
		this.polnamee = polnamee;
	}

	public java.lang.String getPolnamec() {
		return polnamec;
	}

	public void setPolnamec(java.lang.String polnamec) {
		this.polnamec = polnamec;
	}

	public java.lang.String getPodnamee() {
		return podnamee;
	}

	public void setPodnamee(java.lang.String podnamee) {
		this.podnamee = podnamee;
	}

	public java.lang.String getPodnamec() {
		return podnamec;
	}

	public void setPodnamec(java.lang.String podnamec) {
		this.podnamec = podnamec;
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

	public java.math.BigDecimal getCost202() {
		return cost202;
	}

	public void setCost202(java.math.BigDecimal cost202) {
		this.cost202 = cost202;
	}

	public java.math.BigDecimal getCost40gp2() {
		return cost40gp2;
	}

	public void setCost40gp2(java.math.BigDecimal cost40gp2) {
		this.cost40gp2 = cost40gp2;
	}

	public java.math.BigDecimal getCost40hq2() {
		return cost40hq2;
	}

	public void setCost40hq2(java.math.BigDecimal cost40hq2) {
		this.cost40hq2 = cost40hq2;
	}

	public java.lang.String getArea() {
		return area;
	}

	public void setArea(java.lang.String area) {
		this.area = area;
	}

	public java.lang.String getLine() {
		return line;
	}

	public void setLine(java.lang.String line) {
		this.line = line;
	}

	public java.lang.String getRemark() {
		return remark;
	}

	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}

	public java.lang.String getCurrency() {
		return currency;
	}

	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}

	public java.lang.Boolean getIsinvalid() {
		return isinvalid;
	}

	public void setIsinvalid(java.lang.Boolean isinvalid) {
		this.isinvalid = isinvalid;
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

	public java.lang.String getConditiontype() {
		return conditiontype;
	}

	public void setConditiontype(java.lang.String conditiontype) {
		this.conditiontype = conditiontype;
	}

	public java.lang.String getCondition() {
		return condition;
	}

	public void setCondition(java.lang.String condition) {
		this.condition = condition;
	}

	public java.lang.String getConditionvalue() {
		return conditionvalue;
	}

	public void setConditionvalue(java.lang.String conditionvalue) {
		this.conditionvalue = conditionvalue;
	}

	public java.lang.Long getCorpid() {
		return corpid;
	}

	public void setCorpid(java.lang.Long corpid) {
		this.corpid = corpid;
	}

	public java.math.BigDecimal getCost45hq() {
		return cost45hq;
	}

	public void setCost45hq(java.math.BigDecimal cost45hq) {
		this.cost45hq = cost45hq;
	}

	public java.math.BigDecimal getCost45hq2() {
		return cost45hq2;
	}

	public void setCost45hq2(java.math.BigDecimal cost45hq2) {
		this.cost45hq2 = cost45hq2;
	}

	public java.lang.Boolean getIsrelease() {
		return isrelease;
	}

	public void setIsrelease(java.lang.Boolean isrelease) {
		this.isrelease = isrelease;
	}

	public java.lang.Boolean getIstop() {
		return istop;
	}

	public void setIstop(java.lang.Boolean istop) {
		this.istop = istop;
	}

	public java.lang.String getSchedule() {
		return schedule;
	}

	public void setSchedule(java.lang.String schedule) {
		this.schedule = schedule;
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

	public java.lang.String getConditiontype2() {
		return conditiontype2;
	}

	public void setConditiontype2(java.lang.String conditiontype2) {
		this.conditiontype2 = conditiontype2;
	}

	public java.lang.String getCondition2() {
		return condition2;
	}

	public void setCondition2(java.lang.String condition2) {
		this.condition2 = condition2;
	}

	public java.lang.String getConditionvalue2() {
		return conditionvalue2;
	}

	public void setConditionvalue2(java.lang.String conditionvalue2) {
		this.conditionvalue2 = conditionvalue2;
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

}
