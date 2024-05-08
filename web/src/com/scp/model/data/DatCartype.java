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
@Table(name = "dat_cartype")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class DatCartype implements java.io.Serializable {

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
	@Column(name = "cartype")
	private java.lang.String cartype;

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
	@Column(name = "carriedwgt")
	private java.math.BigDecimal carriedwgt;

	/**
	 *@generated
	 */
	@Column(name = "oilwear")
	private java.math.BigDecimal oilwear;

	/**
	 *@generated
	 */
	@Column(name = "carspeed")
	private java.lang.Short carspeed;

	/**
	 *@generated
	 */
	@Column(name = "carpower")
	private java.lang.Short carpower;

	/**
	 *@generated
	 */
	@Column(name = "usage")
	private java.lang.Short usage;

	/**
	 *@generated
	 */
	@Column(name = "oiltype", length = 10)
	private java.lang.String oiltype;

	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String remarks;

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
	public java.lang.String getCartype() {
		return this.cartype;
	}

	/**
	 *@generated
	 */
	public void setCartype(java.lang.String value) {
		this.cartype = value;
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
	public java.math.BigDecimal getCarriedwgt() {
		return this.carriedwgt;
	}

	/**
	 *@generated
	 */
	public void setCarriedwgt(java.math.BigDecimal value) {
		this.carriedwgt = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getOilwear() {
		return this.oilwear;
	}

	/**
	 *@generated
	 */
	public void setOilwear(java.math.BigDecimal value) {
		this.oilwear = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getCarspeed() {
		return this.carspeed;
	}

	/**
	 *@generated
	 */
	public void setCarspeed(java.lang.Short value) {
		this.carspeed = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getCarpower() {
		return this.carpower;
	}

	/**
	 *@generated
	 */
	public void setCarpower(java.lang.Short value) {
		this.carpower = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Short getUsage() {
		return this.usage;
	}

	/**
	 *@generated
	 */
	public void setUsage(java.lang.Short value) {
		this.usage = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getOiltype() {
		return this.oiltype;
	}

	/**
	 *@generated
	 */
	public void setOiltype(java.lang.String value) {
		this.oiltype = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getRemarks() {
		return this.remarks;
	}

	/**
	 *@generated
	 */
	public void setRemarks(java.lang.String value) {
		this.remarks = value;
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