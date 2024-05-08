package com.scp.model.salesmgr;


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
@Table(name = "sys_corporation_black")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class BlackList implements java.io.Serializable {

	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "code")
	private java.lang.String code;
	
	@Column(name = "namec")
	private java.lang.String namec;

	@Column(name = "namee")
	private java.lang.String namee;

	@Column(name = "abbr")
	private java.lang.String abbr;
	
	@Column(name = "addressc")
	private java.lang.String addressc;

	@Column(name = "addresse")
	private java.lang.String addresse;

	@Column(name = "tel1")
	private java.lang.String tel1;

	@Column(name = "tel2")
	private java.lang.String tel2;
	
	@Column(name = "fax1")
	private java.lang.String fax1;

	@Column(name = "fax2")
	private java.lang.String fax2;

	@Column(name = "email1")
	private java.lang.String email1;

	@Column(name = "email2")
	private java.lang.String email2;

	@Column(name = "homepage")
	private java.lang.String homepage;

	@Column(name = "contact")
	private java.lang.String contact;
	
	@Column(name = "invoicetitle")
	private java.lang.String invoicetitle;
	
	@Column(name = "taxid")
	private java.lang.String taxid;
	
	@Column(name = "licno")
	private java.lang.String licno;
	
	@Column(name = "remarks")
	private java.lang.String remarks;
	
	@Column(name = "socialcreditno")
	private java.lang.String socialcreditno;
	
	@Column(name = "linkid")
	private long linkid;
	
	@Column(name = "inputer")
	private java.lang.String inputer;
	
	@Column(name = "reason")
	private java.lang.String reason;
	
	public java.lang.String getReason() {
		return reason;
	}

	public void setReason(java.lang.String reason) {
		this.reason = reason;
	}

	public java.lang.String getInputer() {
		return inputer;
	}

	public void setInputer(java.lang.String inputer) {
		this.inputer = inputer;
	}

	public java.lang.String getUpdater() {
		return updater;
	}

	public void setUpdater(java.lang.String updater) {
		this.updater = updater;
	}

	public java.util.Date getInputtime() {
		return inputtime;
	}

	public void setInputtime(java.util.Date inputtime) {
		this.inputtime = inputtime;
	}

	public java.util.Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(java.util.Date updatetime) {
		this.updatetime = updatetime;
	}

	@Column(name = "updater")
	private java.lang.String updater;
	
	@Column(name = "inputtime", length = 35)
	private java.util.Date inputtime;
	
	@Column(name = "updatetime", length = 35)
	private java.util.Date updatetime;
	
	
	public long getLinkid() {
		return linkid;
	}

	public void setLinkid(long linkid) {
		this.linkid = linkid;
	}

	@Column(name = "isstart")
	private java.lang.Boolean isstart;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getCode() {
		return code;
	}

	public void setCode(java.lang.String code) {
		this.code = code;
	}

	public java.lang.String getNamec() {
		return namec;
	}

	public void setNamec(java.lang.String namec) {
		this.namec = namec;
	}

	public java.lang.String getNamee() {
		return namee;
	}

	public void setNamee(java.lang.String namee) {
		this.namee = namee;
	}

	public java.lang.String getAbbr() {
		return abbr;
	}

	public void setAbbr(java.lang.String abbr) {
		this.abbr = abbr;
	}

	public java.lang.String getAddressc() {
		return addressc;
	}

	public void setAddressc(java.lang.String addressc) {
		this.addressc = addressc;
	}

	public java.lang.String getAddresse() {
		return addresse;
	}

	public void setAddresse(java.lang.String addresse) {
		this.addresse = addresse;
	}

	public java.lang.String getTel1() {
		return tel1;
	}

	public void setTel1(java.lang.String tel1) {
		this.tel1 = tel1;
	}

	public java.lang.String getTel2() {
		return tel2;
	}

	public void setTel2(java.lang.String tel2) {
		this.tel2 = tel2;
	}

	public java.lang.String getFax1() {
		return fax1;
	}

	public void setFax1(java.lang.String fax1) {
		this.fax1 = fax1;
	}

	public java.lang.String getFax2() {
		return fax2;
	}

	public void setFax2(java.lang.String fax2) {
		this.fax2 = fax2;
	}

	public java.lang.String getEmail1() {
		return email1;
	}

	public void setEmail1(java.lang.String email1) {
		this.email1 = email1;
	}

	public java.lang.String getEmail2() {
		return email2;
	}

	public void setEmail2(java.lang.String email2) {
		this.email2 = email2;
	}

	public java.lang.String getHomepage() {
		return homepage;
	}

	public void setHomepage(java.lang.String homepage) {
		this.homepage = homepage;
	}

	public java.lang.String getContact() {
		return contact;
	}

	public void setContact(java.lang.String contact) {
		this.contact = contact;
	}

	public java.lang.String getInvoicetitle() {
		return invoicetitle;
	}

	public void setInvoicetitle(java.lang.String invoicetitle) {
		this.invoicetitle = invoicetitle;
	}

	public java.lang.String getTaxid() {
		return taxid;
	}

	public void setTaxid(java.lang.String taxid) {
		this.taxid = taxid;
	}

	public java.lang.String getLicno() {
		return licno;
	}

	public void setLicno(java.lang.String licno) {
		this.licno = licno;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
	}

	public java.lang.String getSocialcreditno() {
		return socialcreditno;
	}

	public void setSocialcreditno(java.lang.String socialcreditno) {
		this.socialcreditno = socialcreditno;
	}


	public java.lang.Boolean getIsstart() {
		return isstart;
	}

	public void setIsstart(java.lang.Boolean isstart) {
		this.isstart = isstart;
	}
	
	
}
