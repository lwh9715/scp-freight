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
@Table(name = "sys_corpinv")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class SysCorpinv implements java.io.Serializable {

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
	@Column(name = "linkid")
	private java.lang.Long linkid;

	/**
	 *@generated
	 */
	@Column(name = "linktbl", length = 30)
	private java.lang.String linktbl;
	
	/**
	 *@generated
	 */
	@Column(name = "invnamec")
	private java.lang.String invnamec;
	
	@Column(name = "invnamee")
	private java.lang.String invnamee;
	
	@Column(name = "licno")
	private java.lang.String licno;
	
	@Column(name = "tel1",length=100)
	private java.lang.String tel1;
	
	@Column(name = "addressc",length=254)
	private java.lang.String addressc;
	
	@Column(name = "tel1andaddressc")
	private java.lang.String tel1andaddressc;
	
	@Column(name = "bankname",length=254)
	private java.lang.String bankname;
	
	@Column(name = "banknumber",length=254)
	private java.lang.String banknumber;
	
	@Column(name = "information")
	private java.lang.String information;
	
	@Column(name = "amount")
	private java.math.BigDecimal amount;

	@Column(name = "invoicetypepur",length=200)
	private java.lang.String invoicetypepur;
	
	@Column(name = "invoicetypord",length=200)
	private java.lang.String invoicetypord;
	
	@Column(name = "invoicetyrate",length=200)
	private java.lang.String invoicetyrate;
	
	@Column(name = "ordermakdate")
	private java.util.Date ordermakdate;
	
	@Column(name = "bankandnumber",length=200)
	private java.lang.String bankandnumber;
	
	@Column(name = "email", length = 100)
	private java.lang.String email;
	
	@Column(name = "phone", length = 100)
	private java.lang.String phone;
	
	@Column(name = "isaffirm")
	private java.lang.Boolean isaffirm;
	
	@Column(name = "affirmtime")
	private java.util.Date affirmtime;
	
	@Column(name = "affirmuserid", length = 100)
	private java.lang.Long affirmuserid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.Long getLinkid() {
		return linkid;
	}

	public void setLinkid(java.lang.Long linkid) {
		this.linkid = linkid;
	}

	public java.lang.String getLinktbl() {
		return linktbl;
	}

	public void setLinktbl(java.lang.String linktbl) {
		this.linktbl = linktbl;
	}

	public java.lang.String getInvnamec() {
		return invnamec;
	}

	public void setInvnamec(java.lang.String invnamec) {
		this.invnamec = invnamec;
	}

	public java.lang.String getInvnamee() {
		return invnamee;
	}

	public void setInvnamee(java.lang.String invnamee) {
		this.invnamee = invnamee;
	}

	public java.lang.String getLicno() {
		return licno;
	}

	public void setLicno(java.lang.String licno) {
		this.licno = licno;
	}

	public java.lang.String getTel1() {
		return tel1;
	}

	public void setTel1(java.lang.String tel1) {
		this.tel1 = tel1;
	}

	public java.lang.String getAddressc() {
		return addressc;
	}

	public void setAddressc(java.lang.String addressc) {
		this.addressc = addressc;
	}

	public java.lang.String getTel1andaddressc() {
		return tel1andaddressc;
	}

	public void setTel1andaddressc(java.lang.String tel1andaddressc) {
		this.tel1andaddressc = tel1andaddressc;
	}

	public java.lang.String getBankname() {
		return bankname;
	}

	public void setBankname(java.lang.String bankname) {
		this.bankname = bankname;
	}

	public java.lang.String getBanknumber() {
		return banknumber;
	}

	public void setBanknumber(java.lang.String banknumber) {
		this.banknumber = banknumber;
	}

	public java.lang.String getInformation() {
		return information;
	}

	public void setInformation(java.lang.String information) {
		this.information = information;
	}

	public java.math.BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(java.math.BigDecimal amount) {
		this.amount = amount;
	}

	public java.lang.String getInvoicetypepur() {
		return invoicetypepur;
	}

	public void setInvoicetypepur(java.lang.String invoicetypepur) {
		this.invoicetypepur = invoicetypepur;
	}

	public java.lang.String getInvoicetypord() {
		return invoicetypord;
	}

	public void setInvoicetypord(java.lang.String invoicetypord) {
		this.invoicetypord = invoicetypord;
	}

	public java.lang.String getInvoicetyrate() {
		return invoicetyrate;
	}

	public void setInvoicetyrate(java.lang.String invoicetyrate) {
		this.invoicetyrate = invoicetyrate;
	}

	public java.util.Date getOrdermakdate() {
		return ordermakdate;
	}

	public void setOrdermakdate(java.util.Date ordermakdate) {
		this.ordermakdate = ordermakdate;
	}

	public java.lang.String getBankandnumber() {
		return bankandnumber;
	}

	public void setBankandnumber(java.lang.String bankandnumber) {
		this.bankandnumber = bankandnumber;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.lang.String getPhone() {
		return phone;
	}

	public void setPhone(java.lang.String phone) {
		this.phone = phone;
	}

	public java.lang.Boolean getIsaffirm() {
		return isaffirm;
	}

	public void setIsaffirm(java.lang.Boolean isaffirm) {
		this.isaffirm = isaffirm;
	}

	public java.util.Date getAffirmtime() {
		return affirmtime;
	}

	public void setAffirmtime(java.util.Date affirmtime) {
		this.affirmtime = affirmtime;
	}

	public java.lang.Long getAffirmuserid() {
		return affirmuserid;
	}

	public void setAffirmuserid(java.lang.Long affirmuserid) {
		this.affirmuserid = affirmuserid;
	}
	
}