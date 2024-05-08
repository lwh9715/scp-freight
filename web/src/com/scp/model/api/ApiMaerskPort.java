package com.scp.model.api;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "api_maersk_port")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class ApiMaerskPort implements java.io.Serializable {
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;

	@Column(name = "brandscac", length = 20)
	private java.lang.String brandscac;
	
	@Column(name = "geoId", length = 50)
	private java.lang.String geoId;
	
	@Column(name = "rkstCode", length = 5)
	private java.lang.String rkstCode;
	
	@Column(name = "unLocCode", length = 5)
	private java.lang.String unLocCode;
	
	@Column(name = "cityName", length = 20)
	private java.lang.String cityName;
	
	@Column(name = "countryName", length = 20)
	private java.lang.String countryName;
	
	@Column(name = "ispol")
	private java.lang.Boolean ispol;
	
	@Column(name = "ispod")
	private java.lang.Boolean ispod;
	
	@Column(name = "inputer" , length = 100)
	private java.lang.String inputer;
	
	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;
	
	@Column(name = "updater" , length = 100)
	private java.lang.String updater;
	
	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;
	
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getBrandscac() {
		return brandscac;
	}

	public void setBrandscac(java.lang.String brandscac) {
		this.brandscac = brandscac;
	}

	public java.lang.String getGeoId() {
		return geoId;
	}

	public void setGeoId(java.lang.String geoId) {
		this.geoId = geoId;
	}

	public java.lang.String getRkstCode() {
		return rkstCode;
	}

	public void setRkstCode(java.lang.String rkstCode) {
		this.rkstCode = rkstCode;
	}

	public java.lang.String getUnLocCode() {
		return unLocCode;
	}

	public void setUnLocCode(java.lang.String unLocCode) {
		this.unLocCode = unLocCode;
	}

	public java.lang.String getCityName() {
		return cityName;
	}

	public void setCityName(java.lang.String cityName) {
		this.cityName = cityName;
	}

	public java.lang.String getCountryName() {
		return countryName;
	}

	public void setCountryName(java.lang.String countryName) {
		this.countryName = countryName;
	}

	public java.lang.Boolean getIspol() {
		return ispol;
	}

	public void setIspol(java.lang.Boolean ispol) {
		this.ispol = ispol;
	}

	public java.lang.Boolean getIspod() {
		return ispod;
	}

	public void setIspod(java.lang.Boolean ispod) {
		this.ispod = ispod;
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

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
	}
	
	
	
}
