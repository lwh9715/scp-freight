package com.scp.model.sys;

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
@Table(name = "sys_corpvisit")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class SysCorpvisit implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	/**
	 *@generated
	 */
	@Column(name = "customerid")
	private java.lang.Long customerid;

	/**
	 *@generated
	 */
	@Column(name = "customernamec")
	private java.lang.String customernamec;
	
	/**
	 *@generated
	 */
	@Column(name = "customercode")
	private java.lang.String customercode;

	/**
	 *@generated
	 */
	@Column(name = "customernamee")
	private java.lang.String customernamee;

	/**
	 *@generated
	 */
	@Column(name = "customeraddr")
	private java.lang.String customeraddr;

	/**
	 *@generated
	 */
	@Column(name = "sales")
	private java.lang.String sales;

	/**
	 *@generated
	 */
	@Column(name = "visttime")
	private java.util.Date visttime;

	/**
	 *@generated
	 */
	@Column(name = "contactname")
	private java.lang.String contactname;

	/**
	 *@generated
	 */
	@Column(name = "contacttel")
	private java.lang.String contacttel;

	/**
	 *@generated
	 */
	@Column(name = "contactqq")
	private java.lang.String contactqq;

	/**
	 *@generated
	 */
	@Column(name = "contactemail")
	private java.lang.String contactemail;

	/**
	 *@generated
	 */
	@Column(name = "contactduty")
	private java.lang.String contactduty;

	/**
	 *@generated
	 */
	@Column(name = "islead")
	private java.lang.Boolean islead;
	
	
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	

	/**
	 *@generated
	 */
	@Column(name = "contactname2")
	private java.lang.String contactname2;

	/**
	 *@generated
	 */
	@Column(name = "contacttel2")
	private java.lang.String contacttel2;

	/**
	 *@generated
	 */
	@Column(name = "contactqq2")
	private java.lang.String contactqq2;

	/**
	 *@generated
	 */
	@Column(name = "contactemail2")
	private java.lang.String contactemail2;

	/**
	 *@generated
	 */
	@Column(name = "contactduty2")
	private java.lang.String contactduty2;

	/**
	 *@generated
	 */
	@Column(name = "islead2")
	private java.lang.Boolean islead2;

	/**
	 *@generated
	 */
	@Column(name = "contaceps")
	private java.lang.String contaceps;

	/**
	 *@generated
	 */
	@Column(name = "goodsdesc")
	private java.lang.String goodsdesc;

	/**
	 *@generated
	 */
	@Column(name = "goodscity")
	private java.lang.String goodscity;

	/**
	 *@generated
	 */
	@Column(name = "goodspiece")
	private java.lang.String goodspiece;

	/**
	 *@generated
	 */
	@Column(name = "corper1")
	private java.lang.String corper1;

	/**
	 *@generated
	 */
	@Column(name = "corperdesc")
	private java.lang.String corperdesc;

	/**
	 *@generated
	 */
	@Column(name = "corper2")
	private java.lang.String corper2;

	/**
	 *@generated
	 */
	@Column(name = "corper2desc")
	private java.lang.String corper2desc;

	/**
	 *@generated
	 */
	@Column(name = "corpps")
	private java.lang.String corpps;

	/**
	 *@generated
	 */
	@Column(name = "isexpandplan")
	private java.lang.Boolean isexpandplan;

	/**
	 *@generated
	 */
	@Column(name = "countryport")
	private java.lang.String countryport;

	/**
	 *@generated
	 */
	@Column(name = "baseinfo")
	private java.lang.String baseinfo;
	
	
	@Column(name = "vister")
	private java.lang.String vister;
	

	/**
	 *@generated
	 */
	@Column(name = "remarkresponse")
	private java.lang.String remarkresponse;

	/**
	 *@generated
	 */
	@Column(name = "maintenance")
	private java.lang.String maintenance;

	/**
	 *@generated
	 */
	@Column(name = "area1")
	private java.lang.Boolean area1;

	/**
	 *@generated
	 */
	@Column(name = "area2")
	private java.lang.Boolean area2;

	/**
	 *@generated
	 */
	@Column(name = "area3")
	private java.lang.Boolean area3;

	/**
	 *@generated
	 */
	@Column(name = "area4")
	private java.lang.Boolean area4;

	/**
	 *@generated
	 */
	@Column(name = "area5")
	private java.lang.Boolean area5;

	/**
	 *@generated
	 */
	@Column(name = "area6")
	private java.lang.Boolean area6;

	/**
	 *@generated
	 */
	@Column(name = "area7")
	private java.lang.Boolean area7;

	/**
	 *@generated
	 */
	@Column(name = "area8")
	private java.lang.Boolean area8;
	
	@Column(name = "isemailsent")
	private java.lang.Boolean isemailsent;
	

	/**
	 *@generated
	 */
	@Column(name = "area9")
	private java.lang.Boolean area9;

	/**
	 *@generated
	 */
	@Column(name = "area10")
	private java.lang.Boolean area10;

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
	
	
	
	@Column(name = "cs1", length = 30)
	private java.lang.String cs1;

	
	@Column(name = "cs2", length = 30)
	private java.lang.String cs2;


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.Long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(java.lang.Long customerid) {
		this.customerid = customerid;
	}

	public java.lang.String getCustomercode() {
		return customercode;
	}

	public void setCustomercode(java.lang.String customercode) {
		this.customercode = customercode;
	}

	public java.lang.String getCustomernamec() {
		return customernamec;
	}

	public void setCustomernamec(java.lang.String customernamec) {
		this.customernamec = customernamec;
	}

	public java.lang.String getCustomernamee() {
		return customernamee;
	}

	public void setCustomernamee(java.lang.String customernamee) {
		this.customernamee = customernamee;
	}

	public java.lang.String getCustomeraddr() {
		return customeraddr;
	}

	public void setCustomeraddr(java.lang.String customeraddr) {
		this.customeraddr = customeraddr;
	}

	public java.lang.String getSales() {
		return sales;
	}

	public void setSales(java.lang.String sales) {
		this.sales = sales;
	}

	public java.util.Date getVisttime() {
		return visttime;
	}

	public void setVisttime(java.util.Date visttime) {
		this.visttime = visttime;
	}

	public java.lang.String getContactname() {
		return contactname;
	}

	public void setContactname(java.lang.String contactname) {
		this.contactname = contactname;
	}

	public java.lang.String getContacttel() {
		return contacttel;
	}

	public void setContacttel(java.lang.String contacttel) {
		this.contacttel = contacttel;
	}

	public java.lang.String getContactqq() {
		return contactqq;
	}

	public void setContactqq(java.lang.String contactqq) {
		this.contactqq = contactqq;
	}

	public java.lang.String getContactemail() {
		return contactemail;
	}

	public void setContactemail(java.lang.String contactemail) {
		this.contactemail = contactemail;
	}

	public java.lang.String getContactduty() {
		return contactduty;
	}

	public void setContactduty(java.lang.String contactduty) {
		this.contactduty = contactduty;
	}

	public java.lang.Boolean getIslead() {
		return islead;
	}

	public void setIslead(java.lang.Boolean islead) {
		this.islead = islead;
	}

	public java.lang.String getContactname2() {
		return contactname2;
	}

	public void setContactname2(java.lang.String contactname2) {
		this.contactname2 = contactname2;
	}

	public java.lang.String getContacttel2() {
		return contacttel2;
	}

	public void setContacttel2(java.lang.String contacttel2) {
		this.contacttel2 = contacttel2;
	}

	public java.lang.String getContactqq2() {
		return contactqq2;
	}

	public void setContactqq2(java.lang.String contactqq2) {
		this.contactqq2 = contactqq2;
	}

	public java.lang.String getContactemail2() {
		return contactemail2;
	}

	public void setContactemail2(java.lang.String contactemail2) {
		this.contactemail2 = contactemail2;
	}

	public java.lang.String getContactduty2() {
		return contactduty2;
	}

	public void setContactduty2(java.lang.String contactduty2) {
		this.contactduty2 = contactduty2;
	}

	public java.lang.Boolean getIslead2() {
		return islead2;
	}

	public void setIslead2(java.lang.Boolean islead2) {
		this.islead2 = islead2;
	}

	public java.lang.String getContaceps() {
		return contaceps;
	}

	public void setContaceps(java.lang.String contaceps) {
		this.contaceps = contaceps;
	}

	public java.lang.String getGoodsdesc() {
		return goodsdesc;
	}

	public void setGoodsdesc(java.lang.String goodsdesc) {
		this.goodsdesc = goodsdesc;
	}

	public java.lang.String getGoodscity() {
		return goodscity;
	}

	public void setGoodscity(java.lang.String goodscity) {
		this.goodscity = goodscity;
	}

	public java.lang.String getGoodspiece() {
		return goodspiece;
	}

	public void setGoodspiece(java.lang.String goodspiece) {
		this.goodspiece = goodspiece;
	}

	public java.lang.String getCorper1() {
		return corper1;
	}

	public void setCorper1(java.lang.String corper1) {
		this.corper1 = corper1;
	}

	public java.lang.String getCorperdesc() {
		return corperdesc;
	}

	public void setCorperdesc(java.lang.String corperdesc) {
		this.corperdesc = corperdesc;
	}

	public java.lang.String getCorper2() {
		return corper2;
	}

	public void setCorper2(java.lang.String corper2) {
		this.corper2 = corper2;
	}

	public java.lang.String getCorper2desc() {
		return corper2desc;
	}

	public void setCorper2desc(java.lang.String corper2desc) {
		this.corper2desc = corper2desc;
	}

	public java.lang.String getCorpps() {
		return corpps;
	}

	public void setCorpps(java.lang.String corpps) {
		this.corpps = corpps;
	}

	public java.lang.Boolean getIsexpandplan() {
		return isexpandplan;
	}

	public void setIsexpandplan(java.lang.Boolean isexpandplan) {
		this.isexpandplan = isexpandplan;
	}

	public java.lang.String getCountryport() {
		return countryport;
	}

	public void setCountryport(java.lang.String countryport) {
		this.countryport = countryport;
	}

	public java.lang.String getBaseinfo() {
		return baseinfo;
	}

	public void setBaseinfo(java.lang.String baseinfo) {
		this.baseinfo = baseinfo;
	}

	public java.lang.String getRemarkresponse() {
		return remarkresponse;
	}

	public void setRemarkresponse(java.lang.String remarkresponse) {
		this.remarkresponse = remarkresponse;
	}

	public java.lang.String getMaintenance() {
		return maintenance;
	}

	public void setMaintenance(java.lang.String maintenance) {
		this.maintenance = maintenance;
	}

	public java.lang.Boolean getArea1() {
		return area1;
	}

	public void setArea1(java.lang.Boolean area1) {
		this.area1 = area1;
	}

	public java.lang.Boolean getArea2() {
		return area2;
	}

	public void setArea2(java.lang.Boolean area2) {
		this.area2 = area2;
	}

	public java.lang.Boolean getArea3() {
		return area3;
	}

	public void setArea3(java.lang.Boolean area3) {
		this.area3 = area3;
	}

	public java.lang.Boolean getArea4() {
		return area4;
	}

	public void setArea4(java.lang.Boolean area4) {
		this.area4 = area4;
	}

	public java.lang.Boolean getArea5() {
		return area5;
	}

	public void setArea5(java.lang.Boolean area5) {
		this.area5 = area5;
	}

	public java.lang.Boolean getArea6() {
		return area6;
	}

	public void setArea6(java.lang.Boolean area6) {
		this.area6 = area6;
	}

	public java.lang.Boolean getArea7() {
		return area7;
	}

	public void setArea7(java.lang.Boolean area7) {
		this.area7 = area7;
	}

	public java.lang.Boolean getArea8() {
		return area8;
	}

	public void setArea8(java.lang.Boolean area8) {
		this.area8 = area8;
	}

	public java.lang.Boolean getArea9() {
		return area9;
	}

	public void setArea9(java.lang.Boolean area9) {
		this.area9 = area9;
	}

	public java.lang.Boolean getArea10() {
		return area10;
	}

	public void setArea10(java.lang.Boolean area10) {
		this.area10 = area10;
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

	public java.lang.Boolean getIsemailsent() {
		return isemailsent;
	}

	public void setIsemailsent(java.lang.Boolean isemailsent) {
		this.isemailsent = isemailsent;
	}

	public java.lang.String getVister() {
		return vister;
	}

	public void setVister(java.lang.String vister) {
		this.vister = vister;
	}

	public java.lang.String getCs1() {
		return cs1;
	}

	public void setCs1(java.lang.String cs1) {
		this.cs1 = cs1;
	}

	public java.lang.String getCs2() {
		return cs2;
	}

	public void setCs2(java.lang.String cs2) {
		this.cs2 = cs2;
	}

}