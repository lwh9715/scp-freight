package com.scp.model.bus;

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
@Table(name = "bus_insurance")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusInsurance implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "jobid")
	private Long jobid;
	
	@Column(name = "srctype", length = 10)
	private java.lang.String srctype;
	
	@Column(name = "transportid",length = 2)
	private java.lang.String transportid;
	
	@Column(name = "comcode",length = 2)
	private java.lang.String comcode;
	
	@Column(name = "tradeno", length = 32)
	private java.lang.String tradeno;
	
	@Column(name = "name", length = 150)
	private java.lang.String name;
	
	@Column(name = "certificatetype", length = 2)
	private java.lang.String certificatetype;
	
	@Column(name = "certificateno", length = 30)
	private java.lang.String certificateno;
	
	@Column(name = "linkmanname", length = 150)
	private java.lang.String linkmanname;
	
	@Column(name = "address", length = 100)
	private java.lang.String address;
	
	@Column(name = "postcode", length = 6)
	private java.lang.String postcode;
	
	@Column(name = "email", length = 60)
	private java.lang.String email;
	
	@Column(name = "mobiletelephone", length = 30)
	private java.lang.String mobiletelephone;
	
	@Column(name = "officetelephone", length = 30)
	private java.lang.String officetelephone;
	
	@Column(name = "worknum", length = 60)
	private java.lang.String worknum;
	
	@Column(name = "name2", length = 150)
	private java.lang.String name2;
	
	@Column(name = "certificatetype2", length = 2)
	private java.lang.String certificatetype2;
	
	@Column(name = "certificateno2", length = 30)
	private java.lang.String certificateno2;
	
	@Column(name = "linkmanname2", length = 150)
	private java.lang.String linkmanname2;
	
	@Column(name = "address2", length = 100)
	private java.lang.String address2;
	
	@Column(name = "postcode2", length = 6)
	private java.lang.String postcode2;
	
	@Column(name = "email2", length = 60)
	private java.lang.String email2;
	
	@Column(name = "mobiletelephone2", length = 30)
	private java.lang.String mobiletelephone2;
	
	@Column(name = "officetelephone2", length = 30)
	private java.lang.String officetelephone2;
	
	@Column(name = "goodstype",length = 4)
	private java.lang.String goodstype;
	
	@Column(name = "bigpack",length = 4)
	private java.lang.String bigpack;
	
	@Column(name = "smallpack",length = 4)
	private java.lang.String smallpack;
	
	@Column(name = "marks", length = 80)
	private java.lang.String marks;
	
	@Column(name = "goodsdesc", length = 400)
	private java.lang.String goodsdesc;
	
	@Column(name = "goodsnum", length = 200)
	private java.lang.String goodsnum;
	
	@Column(name = "waybillnumber", length = 60)
	private java.lang.String waybillnumber;
	
	@Column(name = "invoicenumber", length = 60)
	private java.lang.String invoicenumber;
	
	@Column(name = "hetong", length = 60)
	private java.lang.String hetong;
	
	@Column(name = "jzx", length = 2)
	private java.lang.String jzx;
	
	@Column(name = "chuanname", length = 20)
	private java.lang.String chuanname;
	
	@Column(name = "transportinfo", length = 60)
	private java.lang.String transportinfo;
	
	@Column(name = "startportname", length = 60)
	private java.lang.String startportname;
	
	@Column(name = "oppoportname", length = 60)
	private java.lang.String oppoportname;
	
	@Column(name = "terminiportname", length = 60)
	private java.lang.String terminiportname;
	
	@Column(name = "startportcode", length = 5)
	private java.lang.String startportcode;
	
	@Column(name = "terminiportcode", length = 5)
	private java.lang.String terminiportcode;
	
	@Column(name = "uncode", length = 4)
	private java.lang.String uncode;
	
	@Column(name = "startdate", length = 29)
	private java.util.Date startdate;
	
	@Column(name = "printformat", length = 4)
	private java.lang.String printformat;
	
	@Column(name = "payplace", length = 80)
	private java.lang.String payplace;
	
	@Column(name = "jcl",length = 4)
	private java.lang.String jcl;
	
	@Column(name = "invoiceamount")
	private java.math.BigDecimal invoiceamount;
	
	@Column(name = "currencycode",length = 4)
	private java.lang.String currencycode;
	
	@Column(name = "hascredit",length = 2)
	private java.lang.String hascredit;
	
	@Column(name = "creditnum", length = 30)
	private java.lang.String creditnum;
	
	@Column(name = "creditinfo", length = 400)
	private java.lang.String creditinfo;
	
	@Column(name = "notice", length = 200)
	private java.lang.String notice;
	
	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;

	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;
	
	@Column(name = "updater", length = 100)
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

	public Long getJobid() {
		return jobid;
	}

	public void setJobid(Long jobid) {
		this.jobid = jobid;
	}

	public java.lang.String getSrctype() {
		return srctype;
	}

	public void setSrctype(java.lang.String srctype) {
		this.srctype = srctype;
	}

	public java.lang.String getTradeno() {
		return tradeno;
	}

	public void setTradeno(java.lang.String tradeno) {
		this.tradeno = tradeno;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getCertificatetype() {
		return certificatetype;
	}

	public void setCertificatetype(java.lang.String certificatetype) {
		this.certificatetype = certificatetype;
	}

	public java.lang.String getCertificateno() {
		return certificateno;
	}

	public void setCertificateno(java.lang.String certificateno) {
		this.certificateno = certificateno;
	}

	public java.lang.String getLinkmanname() {
		return linkmanname;
	}

	public void setLinkmanname(java.lang.String linkmanname) {
		this.linkmanname = linkmanname;
	}

	public java.lang.String getAddress() {
		return address;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	public java.lang.String getPostcode() {
		return postcode;
	}

	public void setPostcode(java.lang.String postcode) {
		this.postcode = postcode;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.lang.String getMobiletelephone() {
		return mobiletelephone;
	}

	public void setMobiletelephone(java.lang.String mobiletelephone) {
		this.mobiletelephone = mobiletelephone;
	}

	public java.lang.String getOfficetelephone() {
		return officetelephone;
	}

	public void setOfficetelephone(java.lang.String officetelephone) {
		this.officetelephone = officetelephone;
	}

	public java.lang.String getWorknum() {
		return worknum;
	}

	public void setWorknum(java.lang.String worknum) {
		this.worknum = worknum;
	}

	public java.lang.String getName2() {
		return name2;
	}

	public void setName2(java.lang.String name2) {
		this.name2 = name2;
	}

	public java.lang.String getCertificatetype2() {
		return certificatetype2;
	}

	public void setCertificatetype2(java.lang.String certificatetype2) {
		this.certificatetype2 = certificatetype2;
	}

	public java.lang.String getCertificateno2() {
		return certificateno2;
	}

	public void setCertificateno2(java.lang.String certificateno2) {
		this.certificateno2 = certificateno2;
	}

	public java.lang.String getLinkmanname2() {
		return linkmanname2;
	}

	public void setLinkmanname2(java.lang.String linkmanname2) {
		this.linkmanname2 = linkmanname2;
	}

	public java.lang.String getAddress2() {
		return address2;
	}

	public void setAddress2(java.lang.String address2) {
		this.address2 = address2;
	}

	public java.lang.String getPostcode2() {
		return postcode2;
	}

	public void setPostcode2(java.lang.String postcode2) {
		this.postcode2 = postcode2;
	}

	public java.lang.String getEmail2() {
		return email2;
	}

	public void setEmail2(java.lang.String email2) {
		this.email2 = email2;
	}

	public java.lang.String getMobiletelephone2() {
		return mobiletelephone2;
	}

	public void setMobiletelephone2(java.lang.String mobiletelephone2) {
		this.mobiletelephone2 = mobiletelephone2;
	}

	public java.lang.String getOfficetelephone2() {
		return officetelephone2;
	}

	public void setOfficetelephone2(java.lang.String officetelephone2) {
		this.officetelephone2 = officetelephone2;
	}

	public java.lang.String getMarks() {
		return marks;
	}

	public void setMarks(java.lang.String marks) {
		this.marks = marks;
	}

	public java.lang.String getGoodsdesc() {
		return goodsdesc;
	}

	public void setGoodsdesc(java.lang.String goodsdesc) {
		this.goodsdesc = goodsdesc;
	}

	public java.lang.String getGoodsnum() {
		return goodsnum;
	}

	public void setGoodsnum(java.lang.String goodsnum) {
		this.goodsnum = goodsnum;
	}

	public java.lang.String getWaybillnumber() {
		return waybillnumber;
	}

	public void setWaybillnumber(java.lang.String waybillnumber) {
		this.waybillnumber = waybillnumber;
	}

	public java.lang.String getInvoicenumber() {
		return invoicenumber;
	}

	public void setInvoicenumber(java.lang.String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}

	public java.lang.String getHetong() {
		return hetong;
	}

	public void setHetong(java.lang.String hetong) {
		this.hetong = hetong;
	}

	public java.lang.String getChuanname() {
		return chuanname;
	}

	public void setChuanname(java.lang.String chuanname) {
		this.chuanname = chuanname;
	}

	public java.lang.String getTransportinfo() {
		return transportinfo;
	}

	public void setTransportinfo(java.lang.String transportinfo) {
		this.transportinfo = transportinfo;
	}

	public java.lang.String getStartportname() {
		return startportname;
	}

	public void setStartportname(java.lang.String startportname) {
		this.startportname = startportname;
	}

	public java.lang.String getOppoportname() {
		return oppoportname;
	}

	public void setOppoportname(java.lang.String oppoportname) {
		this.oppoportname = oppoportname;
	}

	public java.lang.String getTerminiportname() {
		return terminiportname;
	}

	public void setTerminiportname(java.lang.String terminiportname) {
		this.terminiportname = terminiportname;
	}

	public java.lang.String getStartportcode() {
		return startportcode;
	}

	public void setStartportcode(java.lang.String startportcode) {
		this.startportcode = startportcode;
	}

	public java.lang.String getTerminiportcode() {
		return terminiportcode;
	}

	public void setTerminiportcode(java.lang.String terminiportcode) {
		this.terminiportcode = terminiportcode;
	}

	public java.lang.String getUncode() {
		return uncode;
	}

	public void setUncode(java.lang.String uncode) {
		this.uncode = uncode;
	}

	public java.lang.String getPrintformat() {
		return printformat;
	}

	public void setPrintformat(java.lang.String printformat) {
		this.printformat = printformat;
	}

	public java.lang.String getPayplace() {
		return payplace;
	}

	public void setPayplace(java.lang.String payplace) {
		this.payplace = payplace;
	}

	public java.math.BigDecimal getInvoiceamount() {
		return invoiceamount;
	}

	public void setInvoiceamount(java.math.BigDecimal invoiceamount) {
		this.invoiceamount = invoiceamount;
	}

	public java.lang.String getCreditnum() {
		return creditnum;
	}

	public void setCreditnum(java.lang.String creditnum) {
		this.creditnum = creditnum;
	}

	public java.lang.String getCreditinfo() {
		return creditinfo;
	}

	public void setCreditinfo(java.lang.String creditinfo) {
		this.creditinfo = creditinfo;
	}

	public java.lang.String getNotice() {
		return notice;
	}

	public void setNotice(java.lang.String notice) {
		this.notice = notice;
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

	public java.lang.String getTransportid() {
		return transportid;
	}

	public void setTransportid(java.lang.String transportid) {
		this.transportid = transportid;
	}

	public java.lang.String getComcode() {
		return comcode;
	}

	public void setComcode(java.lang.String comcode) {
		this.comcode = comcode;
	}

	public java.lang.String getGoodstype() {
		return goodstype;
	}

	public void setGoodstype(java.lang.String goodstype) {
		this.goodstype = goodstype;
	}

	public java.lang.String getBigpack() {
		return bigpack;
	}

	public void setBigpack(java.lang.String bigpack) {
		this.bigpack = bigpack;
	}

	public java.lang.String getSmallpack() {
		return smallpack;
	}

	public void setSmallpack(java.lang.String smallpack) {
		this.smallpack = smallpack;
	}

	public java.lang.String getJzx() {
		return jzx;
	}

	public void setJzx(java.lang.String jzx) {
		this.jzx = jzx;
	}

	public java.lang.String getJcl() {
		return jcl;
	}

	public void setJcl(java.lang.String jcl) {
		this.jcl = jcl;
	}

	public java.lang.String getCurrencycode() {
		return currencycode;
	}

	public void setCurrencycode(java.lang.String currencycode) {
		this.currencycode = currencycode;
	}

	public java.lang.String getHascredit() {
		return hascredit;
	}

	public void setHascredit(java.lang.String hascredit) {
		this.hascredit = hascredit;
	}

	public java.util.Date getStartdate() {
		return startdate;
	}

	public void setStartdate(java.util.Date startdate) {
		this.startdate = startdate;
	}

	
}