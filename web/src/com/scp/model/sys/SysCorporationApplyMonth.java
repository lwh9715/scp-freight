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
@Table(name = "sys_corporation_applymonth")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysCorporationApplyMonth implements java.io.Serializable {

	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "customerid")
	private long customerid;

	@Column(name = "registmoney")
	private java.math.BigDecimal registmoney;
	
	@Column(name = "socialcreditno")
	private java.lang.String socialcreditno;
	
	@Column(name = "address")
	private java.lang.String address;
	
	@Column(name = "purchaser")
	private java.lang.String purchaser;
	
	@Column(name = "reconciler")
	private java.lang.String reconciler;
	
	@Column(name = "financer")
	private java.lang.String financer;
	
	@Column(name = "purchasemaill")
	private java.lang.String purchasemaill;
	
	@Column(name = "reconcilemaill")
	private java.lang.String reconcilemaill;
	
	@Column(name = "finacemaill")
	private java.lang.String finacemaill;
	
	@Column(name = "purchastel")
	private java.lang.String purchastel;
	
	@Column(name = "reconciltel")
	private java.lang.String reconciltel;
	
	@Column(name = "finactel")
	private java.lang.String finactel;
	
	@Column(name = "applysettlementday")
	private int applysettlementday;
	
	@Column(name = "otherday")
	private int otherday;
	
	@Column(name = "amount1")
	private int amount1;
	
	@Column(name = "amount2")
	private int amount2;
	
	@Column(name = "profit1")
	private java.math.BigDecimal profit1;
	
	@Column(name = "profit2")
	private java.math.BigDecimal profit2;
	
	@Column(name = "paymethod")
	private java.lang.String paymethod;
	
	@Column(name = "reconciliationday")
	private java.util.Date reconciliationday;
	
	@Column(name = "remarks")
	private java.lang.String remarks;
	
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

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public java.math.BigDecimal getRegistmoney() {
		return registmoney;
	}

	public void setRegistmoney(java.math.BigDecimal registmoney) {
		this.registmoney = registmoney;
	}

	public java.lang.String getSocialcreditno() {
		return socialcreditno;
	}

	public void setSocialcreditno(java.lang.String socialcreditno) {
		this.socialcreditno = socialcreditno;
	}

	public java.lang.String getAddress() {
		return address;
	}

	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	public java.lang.String getPurchaser() {
		return purchaser;
	}

	public void setPurchaser(java.lang.String purchaser) {
		this.purchaser = purchaser;
	}

	public java.lang.String getReconciler() {
		return reconciler;
	}

	public void setReconciler(java.lang.String reconciler) {
		this.reconciler = reconciler;
	}

	public java.lang.String getFinancer() {
		return financer;
	}

	public void setFinancer(java.lang.String financer) {
		this.financer = financer;
	}

	public java.lang.String getPurchasemaill() {
		return purchasemaill;
	}

	public void setPurchasemaill(java.lang.String purchasemaill) {
		this.purchasemaill = purchasemaill;
	}

	public java.lang.String getReconcilemaill() {
		return reconcilemaill;
	}

	public void setReconcilemaill(java.lang.String reconcilemaill) {
		this.reconcilemaill = reconcilemaill;
	}

	public java.lang.String getFinacemaill() {
		return finacemaill;
	}

	public void setFinacemaill(java.lang.String finacemaill) {
		this.finacemaill = finacemaill;
	}

	public java.lang.String getPurchastel() {
		return purchastel;
	}

	public void setPurchastel(java.lang.String purchastel) {
		this.purchastel = purchastel;
	}

	public java.lang.String getReconciltel() {
		return reconciltel;
	}

	public void setReconciltel(java.lang.String reconciltel) {
		this.reconciltel = reconciltel;
	}

	public java.lang.String getFinactel() {
		return finactel;
	}

	public void setFinactel(java.lang.String finactel) {
		this.finactel = finactel;
	}

	public int getApplysettlementday() {
		return applysettlementday;
	}

	public void setApplysettlementday(int applysettlementday) {
		this.applysettlementday = applysettlementday;
	}

	public int getOtherday() {
		return otherday;
	}

	public void setOtherday(int otherday) {
		this.otherday = otherday;
	}

	public int getAmount1() {
		return amount1;
	}

	public void setAmount1(int amount1) {
		this.amount1 = amount1;
	}

	public int getAmount2() {
		return amount2;
	}

	public void setAmount2(int amount2) {
		this.amount2 = amount2;
	}

	public java.math.BigDecimal getProfit1() {
		return profit1;
	}

	public void setProfit1(java.math.BigDecimal profit1) {
		this.profit1 = profit1;
	}

	public java.math.BigDecimal getProfit2() {
		return profit2;
	}

	public void setProfit2(java.math.BigDecimal profit2) {
		this.profit2 = profit2;
	}

	public java.lang.String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(java.lang.String paymethod) {
		this.paymethod = paymethod;
	}

	public java.util.Date getReconciliationday() {
		return reconciliationday;
	}

	public void setReconciliationday(java.util.Date reconciliationday) {
		this.reconciliationday = reconciliationday;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
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
