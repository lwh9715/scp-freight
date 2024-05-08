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
@Table(name = "dat_car")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class DatCar implements java.io.Serializable {

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
	@Column(name = "code", nullable = false, length = 20)
	private java.lang.String code;
	
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
	@Column(name = "carno1", length = 10)
	private java.lang.String carno1;

	/**
	 *@generated
	 */
	@Column(name = "carno2", length = 10)
	private java.lang.String carno2;

	/**
	 *@generated
	 */
	@Column(name = "carcolor1", length = 10)
	private java.lang.String carcolor1;

	/**
	 *@generated
	 */
	@Column(name = "carcolor2", length = 10)
	private java.lang.String carcolor2;

	/**
	 *@generated
	 */
	@Column(name = "frameno", length = 20)
	private java.lang.String frameno;

	/**
	 *@generated
	 */
	@Column(name = "framewgt")
	private java.math.BigDecimal framewgt;

	/**
	 *@generated
	 */
	@Column(name = "cartypeid")
	private java.lang.Long cartypeid;

	/**
	 *@generated
	 */
	@Column(name = "cartype", length = 1)
	private java.lang.String cartype;

	/**
	 *@generated
	 */
	@Column(name = "buydate", length = 13)
	private java.util.Date buydate;

	/**
	 *@generated
	 */
	@Column(name = "headcolor", length = 10)
	private java.lang.String headcolor;

	/**
	 *@generated
	 */
	@Column(name = "headprice")
	private java.math.BigDecimal headprice;

	/**
	 *@generated
	 */
	@Column(name = "frameprice")
	private java.math.BigDecimal frameprice;

	/**
	 *@generated
	 */
	@Column(name = "licdate", length = 13)
	private java.util.Date licdate;

	/**
	 *@generated
	 */
	@Column(name = "registerdate", length = 13)
	private java.util.Date registerdate;

	/**
	 *@generated
	 */
	@Column(name = "engineno", length = 20)
	private java.lang.String engineno;

	/**
	 *@generated
	 */
	@Column(name = "markno1", length = 20)
	private java.lang.String markno1;

	/**
	 *@generated
	 */
	@Column(name = "markno2", length = 20)
	private java.lang.String markno2;

	/**
	 *@generated
	 */
	@Column(name = "certificateno", length = 20)
	private java.lang.String certificateno;

	/**
	 *@generated
	 */
	@Column(name = "driverid")
	private java.lang.Long driverid;

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
	public java.lang.String getCarno1() {
		return this.carno1;
	}

	/**
	 *@generated
	 */
	public void setCarno1(java.lang.String value) {
		this.carno1 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCarno2() {
		return this.carno2;
	}

	/**
	 *@generated
	 */
	public void setCarno2(java.lang.String value) {
		this.carno2 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCarcolor1() {
		return this.carcolor1;
	}

	/**
	 *@generated
	 */
	public void setCarcolor1(java.lang.String value) {
		this.carcolor1 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCarcolor2() {
		return this.carcolor2;
	}

	/**
	 *@generated
	 */
	public void setCarcolor2(java.lang.String value) {
		this.carcolor2 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getFrameno() {
		return this.frameno;
	}

	/**
	 *@generated
	 */
	public void setFrameno(java.lang.String value) {
		this.frameno = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getFramewgt() {
		return this.framewgt;
	}

	/**
	 *@generated
	 */
	public void setFramewgt(java.math.BigDecimal value) {
		this.framewgt = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getCartypeid() {
		return this.cartypeid;
	}

	/**
	 *@generated
	 */
	public void setCartypeid(java.lang.Long value) {
		this.cartypeid = value;
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
	public java.util.Date getBuydate() {
		return this.buydate;
	}

	/**
	 *@generated
	 */
	public void setBuydate(java.util.Date value) {
		this.buydate = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getHeadcolor() {
		return this.headcolor;
	}

	/**
	 *@generated
	 */
	public void setHeadcolor(java.lang.String value) {
		this.headcolor = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getHeadprice() {
		return this.headprice;
	}

	/**
	 *@generated
	 */
	public void setHeadprice(java.math.BigDecimal value) {
		this.headprice = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getFrameprice() {
		return this.frameprice;
	}

	/**
	 *@generated
	 */
	public void setFrameprice(java.math.BigDecimal value) {
		this.frameprice = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getLicdate() {
		return this.licdate;
	}

	/**
	 *@generated
	 */
	public void setLicdate(java.util.Date value) {
		this.licdate = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getRegisterdate() {
		return this.registerdate;
	}

	/**
	 *@generated
	 */
	public void setRegisterdate(java.util.Date value) {
		this.registerdate = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getEngineno() {
		return this.engineno;
	}

	/**
	 *@generated
	 */
	public void setEngineno(java.lang.String value) {
		this.engineno = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getMarkno1() {
		return this.markno1;
	}

	/**
	 *@generated
	 */
	public void setMarkno1(java.lang.String value) {
		this.markno1 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getMarkno2() {
		return this.markno2;
	}

	/**
	 *@generated
	 */
	public void setMarkno2(java.lang.String value) {
		this.markno2 = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getCertificateno() {
		return this.certificateno;
	}

	/**
	 *@generated
	 */
	public void setCertificateno(java.lang.String value) {
		this.certificateno = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getDriverid() {
		return this.driverid;
	}

	/**
	 *@generated
	 */
	public void setDriverid(java.lang.Long value) {
		this.driverid = value;
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