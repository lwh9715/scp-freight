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
@Table(name = "sys_corpaccount")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class SysCorpaccount implements java.io.Serializable {

	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	@Column(name = "customerid")
	private java.lang.Long customerid;

	@Column(name = "bankname", length = 100)
	private java.lang.String bankname;
	
	@Column(name = "bankaddr")
	private java.lang.String bankaddr;

	@Column(name = "accountno", length = 100)
	private java.lang.String accountno;
	
	@Column(name = "currency", length = 100)
	private java.lang.String currency;

	@Column(name = "accountcont")
	private java.lang.String accountcont;

	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;

	@Column(name = "inputtime")
	private java.util.Date inputtime;

	@Column(name = "updater", length = 100)
	private java.lang.String updater;

	@Column(name = "updatetime")
	private java.util.Date updatetime;

	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	@Column(name = "bankno", length = 100)
	private java.lang.String bankno;




	@Column(name = "bankopeningaddress")
	private java.lang.String bankopeningaddress;

	@Column(name = "swiftno")
	private java.lang.String swiftno;

	@Column(name = "recoderemark")
	private java.lang.String recoderemark;

	@Column(name = "banknumber")
	private java.lang.String banknumber;

	@Column(name = "accountcountry")
	private java.lang.String accountcountry;

	@Column(name = "isdefault")
	private java.lang.Boolean isdefault;


	public String getBankopeningaddress() {
		return bankopeningaddress;
	}

	public void setBankopeningaddress(String bankopeningaddress) {
		this.bankopeningaddress = bankopeningaddress;
	}

	public String getSwiftno() {
		return swiftno;
	}

	public void setSwiftno(String swiftno) {
		this.swiftno = swiftno;
	}

	public String getRecoderemark() {
		return recoderemark;
	}

	public void setRecoderemark(String recoderemark) {
		this.recoderemark = recoderemark;
	}

	public String getBanknumber() {
		return banknumber;
	}

	public void setBanknumber(String banknumber) {
		this.banknumber = banknumber;
	}

	public String getAccountcountry() {
		return accountcountry;
	}

	public void setAccountcountry(String accountcountry) {
		this.accountcountry = accountcountry;
	}

	public Boolean getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(Boolean isdefault) {
		this.isdefault = isdefault;
	}

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

	public java.lang.String getBankname() {
		return bankname;
	}

	public void setBankname(java.lang.String bankname) {
		this.bankname = bankname;
	}

	public java.lang.String getBankaddr() {
		return bankaddr;
	}

	public void setBankaddr(java.lang.String bankaddr) {
		this.bankaddr = bankaddr;
	}

	public java.lang.String getAccountno() {
		return accountno;
	}

	public void setAccountno(java.lang.String accountno) {
		this.accountno = accountno;
	}

	public java.lang.String getCurrency() {
		return currency;
	}

	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}

	public java.lang.String getAccountcont() {
		return accountcont;
	}

	public void setAccountcont(java.lang.String accountcont) {
		this.accountcont = accountcont;
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

	public java.lang.String getBankno() {
		return bankno;
	}

	public void setBankno(java.lang.String bankno) {
		this.bankno = bankno;
	}


}