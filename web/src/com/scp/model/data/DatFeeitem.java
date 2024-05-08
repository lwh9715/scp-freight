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
@Table(name = "dat_feeitem")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class DatFeeitem implements java.io.Serializable {

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
	@Column(name = "code",  length = 30)
	private java.lang.String code;

	/**
	 *@generated
	 */
	@Column(name = "name",  length = 100)
	private java.lang.String name;

	/**
	 *@generated
	 */
	@Column(name = "ispublic ")
	private java.lang.Boolean ispublic ;
	
	
	/**
	 *@generated
	 */
	@Column(name = "iswarehouse")
	private java.lang.Boolean iswarehouse  ;
	
	/**
	 *@generated
	 */
	@Column(name = "isshipping")
	private java.lang.Boolean isshipping   ;
	
	/**
	 *@generated
	 */
	@Column(name = "isair")
	private java.lang.Boolean isair   ;
	
	
	/**
	 *@generated
	 */
	@Column(name = "namee ", length = 100)
	
	private java.lang.String namee ;
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
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	/**
	 *@generated
	 */
	@Column(name = "isext")
	private java.lang.Boolean isext = false;
	
	/**
	 *@generated
	 */
	@Column(name = "ishidden")
	private java.lang.Boolean ishidden  ;
	
	/**
	 *@generated
	 */
	@Column(name = "currency", length = 10)
	private java.lang.String currency = "USD";
	
	
	@Column(name = "corpid")
	private long corpid;

	@Column(name = "freightcode")
	private String freightcode;
	
	@Column(name = "freighttype")
	private String freighttype;

	@Column(name = "unit")
	private String unit;

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

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
	public java.lang.String getName() {
		return this.name;
	}

	/**
	 *@generated
	 */
	public void setName(java.lang.String value) {
		this.name = value;
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

	/**
	 *@generated
	 */
	public void setIsdelete(java.lang.Boolean value) {
		this.isdelete = value;
	}

	

	public java.lang.String getNamee() {
		return namee;
	}

	public void setNamee(java.lang.String namee) {
		this.namee = namee;
	}

	public java.lang.Boolean getIspublic() {
		return ispublic;
	}

	public void setIspublic(java.lang.Boolean ispublic) {
		this.ispublic = ispublic;
	}

	public java.lang.Boolean getIswarehouse() {
		return iswarehouse;
	}

	public void setIswarehouse(java.lang.Boolean iswarehouse) {
		this.iswarehouse = iswarehouse;
	}

	public java.lang.Boolean getIsshipping() {
		return isshipping;
	}

	public void setIsshipping(java.lang.Boolean isshipping) {
		this.isshipping = isshipping;
	}

	public java.lang.Boolean getIsext() {
		return isext;
	}

	public void setIsext(java.lang.Boolean isext) {
		this.isext = isext;
	}

	public java.lang.Boolean getIshidden() {
		return ishidden;
	}

	public void setIshidden(java.lang.Boolean ishidden) {
		this.ishidden = ishidden;
	}

	public java.lang.String getCurrency() {
		return currency;
	}

	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}

	public java.lang.Boolean getIsair() {
		return isair;
	}

	public void setIsair(java.lang.Boolean isair) {
		this.isair = isair;
	}

	public long getCorpid() {
		return corpid;
	}

	public void setCorpid(long corpid) {
		this.corpid = corpid;
	}

	public String getFreightcode() {
		return freightcode;
	}

	public void setFreightcode(String freightcode) {
		this.freightcode = freightcode;
	}

	public String getFreighttype() {
		return freighttype;
	}

	public void setFreighttype(String freighttype) {
		this.freighttype = freighttype;
	}
	
}