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
@Table(name = "price_fcl_userule")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class PriceFclUserule implements java.io.Serializable {
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	@Column(name = "name", length = 100)
	private java.lang.String name;
	
	@Column(name = "userid")
	private java.lang.Long userid;
	
	@Column(name = "cost20")
	private java.math.BigDecimal cost20;

	@Column(name = "cost40gp")
	private java.math.BigDecimal cost40gp;

	@Column(name = "cost40hq")
	private java.math.BigDecimal cost40hq;
	
	@Column(name = "cost45hq")
	private java.math.BigDecimal cost45hq;
	
	@Column(name = "datestart", length = 13)
	private java.util.Date datestart;
	
	@Column(name = "datend", length = 13)
	private java.util.Date datend;
	
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	@Column(name = "inputer", length = 30)
	private java.lang.String inputer;

	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;

	@Column(name = "updater", length = 30)
	private java.lang.String updater;

	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;
	
	@Column(name = "lines")
	private java.lang.String lines;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.Long getUserid() {
		return userid;
	}

	public void setUserid(java.lang.Long userid) {
		this.userid = userid;
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

	public java.math.BigDecimal getCost45hq() {
		return cost45hq;
	}

	public void setCost45hq(java.math.BigDecimal cost45hq) {
		this.cost45hq = cost45hq;
	}

	public java.util.Date getDatestart() {
		return datestart;
	}

	public void setDatestart(java.util.Date datestart) {
		this.datestart = datestart;
	}

	public java.util.Date getDatend() {
		return datend;
	}

	public void setDatend(java.util.Date datend) {
		this.datend = datend;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
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

	public java.lang.String getLines() {
		return lines;
	}

	public void setLines(java.lang.String lines) {
		this.lines = lines;
	}
	
}
