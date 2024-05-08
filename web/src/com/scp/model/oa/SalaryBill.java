package com.scp.model.oa;

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
@Table(name = "oa_salarybill")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class SalaryBill implements java.io.Serializable {

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
	@Column(name = "workplace")
	private java.lang.String workplace;
	
	/**
	 *@generated
	 */
	@Column(name = "currency")
	private java.lang.String currency;
	
	@Column(name = "namee")
	private java.lang.String namee;
	
	@Column(name = "costcenter")
	private java.lang.String costcenter;
	
	
	@Column(name = "yearno")
	private java.lang.Integer yearno;
	
	@Column(name = "monthno")
	private java.lang.Integer monthno;
	
	@Column(name = "days1")
	private java.lang.Integer days1;
	
	@Column(name = "days2")
	private java.lang.Integer days2;
	
	@Column(name = "pay_base")
	private java.math.BigDecimal pay_base;
	
	@Column(name = "pay_add_job")
	private java.math.BigDecimal pay_add_job;
	
	@Column(name = "pay_add_eat")
	private java.math.BigDecimal pay_add_eat;
	
	@Column(name = "pay_add_ft")
	private java.math.BigDecimal pay_add_ft;
	
	@Column(name = "pay_add_ovt")
	private java.math.BigDecimal pay_add_ovt;
	
	@Column(name = "pay_add_oth")
	private java.math.BigDecimal pay_add_oth;
	
	@Column(name = "pay_cut_leave")
	private java.math.BigDecimal pay_cut_leave;
	
	@Column(name = "pay_cut_late")
	private java.math.BigDecimal pay_cut_late;
	
	@Column(name = "pay_cut_fund")
	private java.math.BigDecimal pay_cut_fund;
	
	@Column(name = "pay_cut_security")
	private java.math.BigDecimal pay_cut_security;
	
	@Column(name = "pay_cut_tax")
	private java.math.BigDecimal pay_cut_tax;
	
	@Column(name = "pay_cut_oth")
	private java.math.BigDecimal pay_cut_oth;
	

	@Column(name = "amt")
	private java.math.BigDecimal amt;
	
	@Column(name = "amtadjust")
	private java.math.BigDecimal amtadjust;
	
	@Column(name = "overseasadd")
	private java.math.BigDecimal overseasadd;
	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getworkplace() {
		return workplace;
	}

	public void setworkplace(java.lang.String workplace) {
		this.workplace = workplace;
	}

	public java.lang.String getNamee() {
		return namee;
	}

	public void setNamee(java.lang.String namee) {
		this.namee = namee;
	}

	public java.lang.String getCostcenter() {
		return costcenter;
	}

	public void setCostcenter(java.lang.String costcenter) {
		this.costcenter = costcenter;
	}

	public java.lang.Integer getYearno() {
		return yearno;
	}

	public void setYearno(java.lang.Integer yearno) {
		this.yearno = yearno;
	}

	public java.lang.Integer getMonthno() {
		return monthno;
	}

	public void setMonthno(java.lang.Integer monthno) {
		this.monthno = monthno;
	}

	public java.lang.Integer getDays1() {
		return days1;
	}

	public void setDays1(java.lang.Integer days1) {
		this.days1 = days1;
	}

	public java.lang.Integer getDays2() {
		return days2;
	}

	public void setDays2(java.lang.Integer days2) {
		this.days2 = days2;
	}

	public java.math.BigDecimal getPay_base() {
		return pay_base;
	}

	public void setPay_base(java.math.BigDecimal payBase) {
		pay_base = payBase;
	}

	public java.math.BigDecimal getPay_add_job() {
		return pay_add_job;
	}

	public void setPay_add_job(java.math.BigDecimal payAddJob) {
		pay_add_job = payAddJob;
	}

	public java.math.BigDecimal getPay_add_eat() {
		return pay_add_eat;
	}

	public void setPay_add_eat(java.math.BigDecimal payAddEat) {
		pay_add_eat = payAddEat;
	}

	public java.math.BigDecimal getPay_add_ft() {
		return pay_add_ft;
	}

	public void setPay_add_ft(java.math.BigDecimal payAddFt) {
		pay_add_ft = payAddFt;
	}

	public java.math.BigDecimal getPay_add_ovt() {
		return pay_add_ovt;
	}

	public void setPay_add_ovt(java.math.BigDecimal payAddOvt) {
		pay_add_ovt = payAddOvt;
	}

	public java.math.BigDecimal getPay_add_oth() {
		return pay_add_oth;
	}

	public void setPay_add_oth(java.math.BigDecimal payAddOth) {
		pay_add_oth = payAddOth;
	}

	public java.math.BigDecimal getPay_cut_leave() {
		return pay_cut_leave;
	}

	public void setPay_cut_leave(java.math.BigDecimal payCutLeave) {
		pay_cut_leave = payCutLeave;
	}

	public java.math.BigDecimal getPay_cut_late() {
		return pay_cut_late;
	}

	public void setPay_cut_late(java.math.BigDecimal payCutLate) {
		pay_cut_late = payCutLate;
	}

	public java.math.BigDecimal getPay_cut_fund() {
		return pay_cut_fund;
	}

	public void setPay_cut_fund(java.math.BigDecimal payCutFund) {
		pay_cut_fund = payCutFund;
	}

	public java.math.BigDecimal getPay_cut_security() {
		return pay_cut_security;
	}

	public void setPay_cut_security(java.math.BigDecimal payCutSecurity) {
		pay_cut_security = payCutSecurity;
	}

	public java.math.BigDecimal getPay_cut_tax() {
		return pay_cut_tax;
	}

	public void setPay_cut_tax(java.math.BigDecimal payCutTax) {
		pay_cut_tax = payCutTax;
	}

	public java.math.BigDecimal getPay_cut_oth() {
		return pay_cut_oth;
	}

	public void setPay_cut_oth(java.math.BigDecimal payCutOth) {
		pay_cut_oth = payCutOth;
	}

	public java.math.BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(java.math.BigDecimal amt) {
		this.amt = amt;
	}

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
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

	public java.lang.String getWorkplace() {
		return workplace;
	}

	public void setWorkplace(java.lang.String workplace) {
		this.workplace = workplace;
	}

	public java.math.BigDecimal getAmtadjust() {
		return amtadjust;
	}

	public void setAmtadjust(java.math.BigDecimal amtadjust) {
		this.amtadjust = amtadjust;
	}

	public java.math.BigDecimal getOverseasadd() {
		return overseasadd;
	}

	public void setOverseasadd(java.math.BigDecimal overseasadd) {
		this.overseasadd = overseasadd;
	}

	public java.lang.String getCurrency() {
		return currency;
	}

	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}
	
	
	
	
	
}