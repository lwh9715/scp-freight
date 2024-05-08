package com.scp.model.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "sys_corpext")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native" , parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class SysCorpext implements java.io.Serializable {

    @Column(name = "id", nullable = false)
    @Id
    @GeneratedValue(generator="idStrategy")
    private long id;

    @Column(name = "customerid")
    private java.lang.Long customerid;

    @Column(name = "merchantcode",  length = 255)
    private java.lang.String merchantcode;

    @Column(name = "merchantorganizationtype",  length = 255)
    private java.lang.String merchantorganizationtype ;

    @Column(name = "merchantname1",  length = 255)
    private java.lang.String merchantname1;

    @Column(name = "merchantname2",  length = 255)
    private java.lang.String merchantname2;

    @Column(name = "merchantname3",  length = 255)
    private java.lang.String merchantname3;

    @Column(name = "merchantname4",  length = 255)
    private java.lang.String merchantname4;

    @Column(name = "secondname",  length = 255)
    private java.lang.String secondname;

    @Column(name = "groupmerchantabbr",  length = 255)
    private java.lang.String groupmerchantabbr;

    @Column(name = "merchantlanguage",  length = 255)
    private java.lang.String merchantlanguage;

    @Column(name = "isch")
    private java.lang.Boolean isch;

    @Column(name = "isen")
    private java.lang.Boolean isen;

    @Column(name = "merchantregistrationcountry",  length = 255)
    private java.lang.String merchantregistrationcountry;

    @Column(name = "merchantregistrationarea",  length = 255)
    private java.lang.String merchantregistrationarea ;

    @Column(name = "merchantregisteredcity",  length = 255)
    private java.lang.String merchantregisteredcity;

    @Column(name = "merchantregisteredaddress",  length = 255)
    private java.lang.String merchantregisteredaddress;

    @Column(name = "merchantzipcode",  length = 255)
    private java.lang.String merchantzipcode;

    @Column(name = "phone",  length = 255)
    private java.lang.String phone;

    @Column(name = "ext",  length = 255)
    private java.lang.String ext;

    @Column(name = "mobilephone",  length = 255)
    private java.lang.String mobilephone;

    @Column(name = "mailbox",  length = 255)
    private java.lang.String mailbox;

    @Column(name = "fax",  length = 255)
    private java.lang.String fax;
    
    @Column(name = "associatedtype",  length = 255)
    private java.lang.String associatedtype;

    @Column(name = "ext2",  length = 255)
    private java.lang.String ext2;

    @Column(name = "inputer",  length = 255)
    private java.lang.String inputer;

    @Column(name = "inputtime", length = 29)
    private java.util.Date inputtime;

    @Column(name = "updater",  length = 255)
    private java.lang.String updater;

    @Column(name = "updatetime", length = 29)
    private java.util.Date updatetime;

    @Column(name = "custcode",  length = 255)
    private java.lang.String custcode;

    @Column(name = "mkscode",  length = 255)
    private java.lang.String mkscode;


    public String getMkscode() {
        return mkscode;
    }

    public void setMkscode(String mkscode) {
        this.mkscode = mkscode;
    }

    public String getCustcode() {
        return custcode;
    }

    public void setCustcode(String custcode) {
        this.custcode = custcode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Long customerid) {
        this.customerid = customerid;
    }

    public String getMerchantcode() {
        return merchantcode;
    }

    public void setMerchantcode(String merchantcode) {
        this.merchantcode = merchantcode;
    }

    public String getMerchantorganizationtype() {
        return merchantorganizationtype;
    }

    public void setMerchantorganizationtype(String merchantorganizationtype) {
        this.merchantorganizationtype = merchantorganizationtype;
    }

    public String getMerchantname1() {
        return merchantname1;
    }

    public void setMerchantname1(String merchantname1) {
        this.merchantname1 = merchantname1;
    }

    public String getMerchantname2() {
        return merchantname2;
    }

    public void setMerchantname2(String merchantname2) {
        this.merchantname2 = merchantname2;
    }

    public String getMerchantname3() {
        return merchantname3;
    }

    public void setMerchantname3(String merchantname3) {
        this.merchantname3 = merchantname3;
    }

    public String getMerchantname4() {
        return merchantname4;
    }

    public void setMerchantname4(String merchantname4) {
        this.merchantname4 = merchantname4;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getGroupmerchantabbr() {
        return groupmerchantabbr;
    }

    public void setGroupmerchantabbr(String groupmerchantabbr) {
        this.groupmerchantabbr = groupmerchantabbr;
    }

    public String getMerchantlanguage() {
        return merchantlanguage;
    }

    public void setMerchantlanguage(String merchantlanguage) {
        this.merchantlanguage = merchantlanguage;
    }

    public Boolean getIsch() {
        return isch;
    }

    public void setIsch(Boolean isch) {
        this.isch = isch;
    }

    public Boolean getIsen() {
        return isen;
    }

    public void setIsen(Boolean isen) {
        this.isen = isen;
    }

    public String getMerchantregistrationcountry() {
        return merchantregistrationcountry;
    }

    public void setMerchantregistrationcountry(String merchantregistrationcountry) {
        this.merchantregistrationcountry = merchantregistrationcountry;
    }

    public String getMerchantregistrationarea() {
        return merchantregistrationarea;
    }

    public void setMerchantregistrationarea(String merchantregistrationarea) {
        this.merchantregistrationarea = merchantregistrationarea;
    }

    public String getMerchantregisteredcity() {
        return merchantregisteredcity;
    }

    public void setMerchantregisteredcity(String merchantregisteredcity) {
        this.merchantregisteredcity = merchantregisteredcity;
    }

    public String getMerchantregisteredaddress() {
        return merchantregisteredaddress;
    }

    public void setMerchantregisteredaddress(String merchantregisteredaddress) {
        this.merchantregisteredaddress = merchantregisteredaddress;
    }

    public String getMerchantzipcode() {
        return merchantzipcode;
    }

    public void setMerchantzipcode(String merchantzipcode) {
        this.merchantzipcode = merchantzipcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getInputer() {
        return inputer;
    }

    public void setInputer(String inputer) {
        this.inputer = inputer;
    }

    public Date getInputtime() {
        return inputtime;
    }

    public void setInputtime(Date inputtime) {
        this.inputtime = inputtime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

	public java.lang.String getAssociatedtype() {
		return associatedtype;
	}

	public void setAssociatedtype(java.lang.String associatedtype) {
		this.associatedtype = associatedtype;
	}
    
    
}
