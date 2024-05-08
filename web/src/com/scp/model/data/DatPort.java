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
@Table(name = "dat_port")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class DatPort implements java.io.Serializable {

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
	@Column(name = "code", length = 20)
	private java.lang.String code;

	/**
	 *@generated
	 */
	@Column(name = "namec", length = 100)
	private java.lang.String namec;

	/**
	 *@generated
	 */
	@Column(name = "namee", length = 100)
	private java.lang.String namee;
	
	@Column(name = "country", length = 20)
	private java.lang.String country;
	
	@Column(name = "city", length = 40)
	private java.lang.String city ;

	@Column(name = "line", length = 20)
	private java.lang.String line;
	
	@Column(name = "line2", length = 20)
	private java.lang.String line2;
	
	
	@Column(name = "orderno")
	private java.lang.Long orderno;
	
	@Column(name = "isship")
	private java.lang.Boolean isship;
	
	@Column(name = "isair")
	private java.lang.Boolean isair;
	
	@Column(name = "isvalid")
	private java.lang.Boolean isvalid;
	
	@Column(name = "ispol")
	private java.lang.Boolean ispol;

	

	@Column(name = "ispod")
	private java.lang.Boolean ispod;
	
	@Column(name = "ispdd")
	private java.lang.Boolean ispdd;
	
	
	@Column(name = "link")
	private String link;
	
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
	
	@Column(name = "isdestination")
	private java.lang.Boolean isdestination;
	
	@Column(name = "isbarge")
	private java.lang.Boolean isbarge;
	
	@Column(name = "province", length = 30)
	private java.lang.String province ;
	
	@Column(name = "istrain")
	private java.lang.Boolean istrain;

	public long getId() {
		return id;
	}

	public java.lang.String getCode() {
		return code;
	}

	public java.lang.String getNamec() {
		return namec;
	}

	public java.lang.String getNamee() {
		return namee;
	}

	public java.lang.Boolean getIsship() {
		return isship;
	}

	public java.lang.Boolean getIsair() {
		return isair;
	}

	public java.lang.Boolean getIsvalid() {
		return isvalid;
	}

	public java.lang.Boolean getIspol() {
		return ispol;
	}

	public java.lang.Boolean getIspod() {
		return ispod;
	}

	public java.lang.Boolean getIspdd() {
		return ispdd;
	}

	public java.lang.String getInputer() {
		return inputer;
	}

	public java.util.Date getInputtime() {
		return inputtime;
	}

	public java.lang.String getUpdater() {
		return updater;
	}

	public java.util.Date getUpdatetime() {
		return updatetime;
	}

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setCode(java.lang.String code) {
		this.code = code;
	}

	public void setNamec(java.lang.String namec) {
		this.namec = namec;
	}

	public void setNamee(java.lang.String namee) {
		this.namee = namee;
	}

	public void setIsship(java.lang.Boolean isship) {
		this.isship = isship;
	}

	public void setIsair(java.lang.Boolean isair) {
		this.isair = isair;
	}

	public void setIsvalid(java.lang.Boolean isvalid) {
		this.isvalid = isvalid;
	}

	public void setIspol(java.lang.Boolean ispol) {
		this.ispol = ispol;
	}

	public void setIspod(java.lang.Boolean ispod) {
		this.ispod = ispod;
	}

	public void setIspdd(java.lang.Boolean ispdd) {
		this.ispdd = ispdd;
	}

	public void setInputer(java.lang.String inputer) {
		this.inputer = inputer;
	}

	public void setInputtime(java.util.Date inputtime) {
		this.inputtime = inputtime;
	}

	public void setUpdater(java.lang.String updater) {
		this.updater = updater;
	}

	public void setUpdatetime(java.util.Date updatetime) {
		this.updatetime = updatetime;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
	}

	public java.lang.String getCountry() {
		return country;
	}

	public void setCountry(java.lang.String country) {
		this.country = country;
	}

	public java.lang.String getLine() {
		return line;
	}

	public void setLine(java.lang.String line) {
		this.line = line;
	}

	public java.lang.String getCity() {
		return city;
	}

	public void setCity(java.lang.String city) {
		this.city = city;
	}

	public java.lang.Long getOrderno() {
		return orderno;
	}

	public void setOrderno(java.lang.Long orderno) {
		this.orderno = orderno;
	}


	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public java.lang.Boolean getIsdestination() {
		return isdestination;
	}

	public void setIsdestination(java.lang.Boolean isdestination) {
		this.isdestination = isdestination;
	}

	public java.lang.String getLine2() {
		return line2;
	}

	public void setLine2(java.lang.String line2) {
		this.line2 = line2;
	}

	public java.lang.Boolean getIsbarge() {
		return isbarge;
	}

	public void setIsbarge(java.lang.Boolean isbarge) {
		this.isbarge = isbarge;
	}

	public java.lang.String getProvince() {
		return province;
	}

	public void setProvince(java.lang.String province) {
		this.province = province;
	}

	public java.lang.Boolean getIstrain() {
		return istrain;
	}

	public void setIstrain(java.lang.Boolean istrain) {
		this.istrain = istrain;
	}
	
}