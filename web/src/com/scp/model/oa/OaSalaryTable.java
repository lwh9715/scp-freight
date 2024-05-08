package com.scp.model.oa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "oa_salary_table")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public  class OaSalaryTable implements java.io.Serializable {
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;
	
	@Column(name = "salaryplace")
	private String salaryplace;
	
	@Column(name = "sbplace")
	private String sbplace;
	
	@Column(name = "jobplace")
	private String jobplace;
	
	@Column(name = "userinfoid")
	private java.lang.Long userinfoid;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "yearno")
	private short yearno;
	
	@Column(name = "monthno")
	private short monthno;
	
	@Column(name = "amt_yx")
	private double amtYx;
	
	@Column(name = "amt_yx2")
	private double amtYx2;
	
	@Column(name = "amt_wp1")
	private double amtWp1;
	
	@Column(name = "amt_wp2")
	private double amtWp2;
	
	@Column(name = "amt_sum_yfgz")
	private double amtSumYfgz;
	
	@Column(name = "amt_add_jb")
	private double amtAddJb;
	
	@Column(name = "amt_add_bt")
	private double amtAddBt;
	
	@Column(name = "amt_add_qqj")
	private double amtAddQqj;
	
	@Column(name = "amt_cut_tz")
	private double amtCutTz;
	
	@Column(name = "amt_cut_wxj")
	private double amtCutWxj;
	
	@Column(name = "amt_cut_cd")
	private double amtCutCd;
	
	@Column(name = "amt_cut_sddh")
	private double amtCutSddh;
	
	@Column(name = "amt_cut_other")
	private double amtCutOther;
	
	@Column(name = "amtsum_before_tax")
	private double amtsumBeforeTax;
	
	@Column(name = "amt_cut_ylo")
	private double amtCutYlo;
	
	@Column(name = "amt_cut_yli")
	private double amtCutYli;
	
	@Column(name = "amt_cut_sye")
	private double amtCutSye;
	
	@Column(name = "amt_cut_gjj")
	private double amtCutGjj;
	
	@Column(name = "amt_cut_grsds")
	private double amtCutGrsds;
	
	@Column(name = "amt_sum_grdk")
	private double amtSumGrdk;
	
	@Column(name = "amt_sum_shdk")
	private double amtSumShdk;
	
	@Column(name = "amt_sum_bank_yf1")
	private double amtSumBankYf1;
	
	@Column(name = "amt_sum_bank_yf2")
	private double amtSumBankYf2;
	
	@Column(name = "amt_sum_cash_yf1")
	private double amtSumCashYf1;
	
	@Column(name = "amt_sum_cash_yf2")
	private double amtSumCashYf2;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "isdelete")
	private boolean isdelete;
	
	@Column(name = "inputer")
	private String inputer;
	
	@Column(name = "inputtime")
	private Date inputtime;
	
	@Column(name = "updater")
	private String updater;
	
	@Column(name = "updatetime")
	private Date updatetime;


	// Property accessors

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSalaryplace() {
		return this.salaryplace;
	}

	
	
	public java.lang.Long getUserinfoid() {
		return userinfoid;
	}

	public void setUserinfoid(java.lang.Long userinfoid) {
		this.userinfoid = userinfoid;
	}

	public void setSalaryplace(String salaryplace) {
		this.salaryplace = salaryplace;
	}

	public void setUserinfoid(long userinfoid) {
		this.userinfoid = userinfoid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getYearno() {
		return this.yearno;
	}

	public void setYearno(short yearno) {
		this.yearno = yearno;
	}

	public short getMonthno() {
		return this.monthno;
	}

	public void setMonthno(short monthno) {
		this.monthno = monthno;
	}

	public double getAmtYx() {
		return this.amtYx;
	}

	public void setAmtYx(double amtYx) {
		this.amtYx = amtYx;
	}

	public double getAmtYx2() {
		return this.amtYx2;
	}

	public void setAmtYx2(double amtYx2) {
		this.amtYx2 = amtYx2;
	}

	public double getAmtWp1() {
		return this.amtWp1;
	}

	public void setAmtWp1(double amtWp1) {
		this.amtWp1 = amtWp1;
	}

	public double getAmtWp2() {
		return this.amtWp2;
	}

	public void setAmtWp2(double amtWp2) {
		this.amtWp2 = amtWp2;
	}

	public double getAmtSumYfgz() {
		return this.amtSumYfgz;
	}

	public void setAmtSumYfgz(double amtSumYfgz) {
		this.amtSumYfgz = amtSumYfgz;
	}

	public double getAmtAddJb() {
		return this.amtAddJb;
	}

	public void setAmtAddJb(double amtAddJb) {
		this.amtAddJb = amtAddJb;
	}

	public double getAmtAddBt() {
		return this.amtAddBt;
	}

	public void setAmtAddBt(double amtAddBt) {
		this.amtAddBt = amtAddBt;
	}

	public double getAmtAddQqj() {
		return this.amtAddQqj;
	}

	public void setAmtAddQqj(double amtAddQqj) {
		this.amtAddQqj = amtAddQqj;
	}

	public double getAmtCutTz() {
		return this.amtCutTz;
	}

	public void setAmtCutTz(double amtCutTz) {
		this.amtCutTz = amtCutTz;
	}

	public double getAmtCutWxj() {
		return this.amtCutWxj;
	}

	public void setAmtCutWxj(double amtCutWxj) {
		this.amtCutWxj = amtCutWxj;
	}

	public double getAmtCutCd() {
		return this.amtCutCd;
	}

	public void setAmtCutCd(double amtCutCd) {
		this.amtCutCd = amtCutCd;
	}

	public double getAmtCutSddh() {
		return this.amtCutSddh;
	}

	public void setAmtCutSddh(double amtCutSddh) {
		this.amtCutSddh = amtCutSddh;
	}

	public double getAmtCutOther() {
		return this.amtCutOther;
	}

	public void setAmtCutOther(double amtCutOther) {
		this.amtCutOther = amtCutOther;
	}

	public double getAmtsumBeforeTax() {
		return this.amtsumBeforeTax;
	}

	public void setAmtsumBeforeTax(double amtsumBeforeTax) {
		this.amtsumBeforeTax = amtsumBeforeTax;
	}

	public double getAmtCutYlo() {
		return this.amtCutYlo;
	}

	public void setAmtCutYlo(double amtCutYlo) {
		this.amtCutYlo = amtCutYlo;
	}

	public double getAmtCutYli() {
		return this.amtCutYli;
	}

	public void setAmtCutYli(double amtCutYli) {
		this.amtCutYli = amtCutYli;
	}

	public double getAmtCutSye() {
		return this.amtCutSye;
	}

	public void setAmtCutSye(double amtCutSye) {
		this.amtCutSye = amtCutSye;
	}

	public double getAmtCutGjj() {
		return this.amtCutGjj;
	}

	public void setAmtCutGjj(double amtCutGjj) {
		this.amtCutGjj = amtCutGjj;
	}

	public double getAmtCutGrsds() {
		return this.amtCutGrsds;
	}

	public void setAmtCutGrsds(double amtCutGrsds) {
		this.amtCutGrsds = amtCutGrsds;
	}

	public double getAmtSumGrdk() {
		return this.amtSumGrdk;
	}

	public void setAmtSumGrdk(double amtSumGrdk) {
		this.amtSumGrdk = amtSumGrdk;
	}

	public double getAmtSumShdk() {
		return this.amtSumShdk;
	}

	public void setAmtSumShdk(double amtSumShdk) {
		this.amtSumShdk = amtSumShdk;
	}

	public double getAmtSumBankYf1() {
		return this.amtSumBankYf1;
	}

	public void setAmtSumBankYf1(double amtSumBankYf1) {
		this.amtSumBankYf1 = amtSumBankYf1;
	}

	public double getAmtSumBankYf2() {
		return this.amtSumBankYf2;
	}

	public void setAmtSumBankYf2(double amtSumBankYf2) {
		this.amtSumBankYf2 = amtSumBankYf2;
	}

	public double getAmtSumCashYf1() {
		return this.amtSumCashYf1;
	}

	public void setAmtSumCashYf1(double amtSumCashYf1) {
		this.amtSumCashYf1 = amtSumCashYf1;
	}

	public double getAmtSumCashYf2() {
		return this.amtSumCashYf2;
	}

	public void setAmtSumCashYf2(double amtSumCashYf2) {
		this.amtSumCashYf2 = amtSumCashYf2;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public boolean getIsdelete() {
		return this.isdelete;
	}

	public void setIsdelete(boolean isdelete) {
		this.isdelete = isdelete;
	}

	public String getInputer() {
		return this.inputer;
	}

	public void setInputer(String inputer) {
		this.inputer = inputer;
	}

	public Date getInputtime() {
		return this.inputtime;
	}

	public void setInputtime(Date inputtime) {
		this.inputtime = inputtime;
	}

	public String getUpdater() {
		return this.updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public Date getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getSbplace() {
		return sbplace;
	}

	public void setSbplace(String sbplace) {
		this.sbplace = sbplace;
	}

	public String getJobplace() {
		return jobplace;
	}

	public void setJobplace(String jobplace) {
		this.jobplace = jobplace;
	}

}