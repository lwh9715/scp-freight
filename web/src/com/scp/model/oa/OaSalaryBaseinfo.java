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

@Table(name = "oa_salary_baseinfo")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class OaSalaryBaseinfo implements java.io.Serializable {

	// Fields
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;
	
	@Column(name = "userinfoid")
	private long userinfoid;
	
	@Column(name = "name")
	private String name;
	

	@Column(name = "jobno")
	private String jobno;
	
	@Column(name = "salaryplace")
	private String salaryplace;
	
	@Column(name = "sbplace")
	private String sbplace;
	
	@Column(name = "amt_bs1")
	private double amt_bs1;
	
	@Column(name = "amt_bs2")
	private double amt_bs2;
	
	@Column(name = "amt_yx")
	private double amt_yx;
	

	
	@Column(name = "amt_wp1")
	private double amt_wp1;
	
	@Column(name = "amt_wp2")
	private double amt_wp2;
	
	@Column(name = "amt_ylo")
	private double amt_ylo;
	
	@Column(name = "amt_ylo2")
	private double amt_ylo2;
	
	@Column(name = "amt_yli")
	private double amt_yli;
	
	@Column(name = "amt_yli2")
	private double amt_yli2;
	
	

	
	@Column(name = "amt_sye")
	private double amt_sye;
	

	@Column(name = "amt_sye2")
	private double amt_sye2;
	
	@Column(name = "amt_gs")
	private double amt_gs;
	
	@Column(name = "amt_shy")
	private double amt_shy;
	
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
	
	@Column(name = "datebegin")
	private Date datebegin;
	
	@Column(name = "dateend")
	private Date dateend;
	

	
	@Column(name = "sspbase", length = 30)
	private java.lang.Double sspbase;
	
	@Column(name = "pfpbase", length = 30)
	private java.lang.Double pfpbase;
	

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserinfoid() {
		return this.userinfoid;
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



	public String getSalaryplace() {
		return this.salaryplace;
	}

	public void setSalaryplace(String salaryplace) {
		this.salaryplace = salaryplace;
	}
	
	

	public double getAmt_yx() {
		return amt_yx;
	}

	public void setAmt_yx(double amtYx) {
		amt_yx = amtYx;
	}
	
	


	public double getAmt_wp1() {
		return amt_wp1;
	}

	public void setAmt_wp1(double amtWp1) {
		amt_wp1 = amtWp1;
	}

	public double getAmt_wp2() {
		return amt_wp2;
	}

	public void setAmt_wp2(double amtWp2) {
		amt_wp2 = amtWp2;
	}

	public double getAmt_ylo() {
		return amt_ylo;
	}

	public void setAmt_ylo(double amtYlo) {
		amt_ylo = amtYlo;
	}

	public double getAmt_yli() {
		return amt_yli;
	}

	public void setAmt_yli(double amtYli) {
		amt_yli = amtYli;
	}

	public double getAmt_gs() {
		return amt_gs;
	}

	public void setAmt_gs(double amtGs) {
		amt_gs = amtGs;
	}

	public double getAmt_sye() {
		return amt_sye;
	}

	public void setAmt_sye(double amtSye) {
		amt_sye = amtSye;
	}

	public double getAmt_shy() {
		return amt_shy;
	}

	public void setAmt_shy(double amtShy) {
		amt_shy = amtShy;
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

	public double getAmt_bs1() {
		return amt_bs1;
	}

	public void setAmt_bs1(double amtBs1) {
		amt_bs1 = amtBs1;
	}

	public double getAmt_bs2() {
		return amt_bs2;
	}

	public void setAmt_bs2(double amtBs2) {
		amt_bs2 = amtBs2;
	}

	public double getAmt_ylo2() {
		return amt_ylo2;
	}

	public void setAmt_ylo2(double amtYlo2) {
		amt_ylo2 = amtYlo2;
	}

	public double getAmt_yli2() {
		return amt_yli2;
	}

	public void setAmt_yli2(double amtYli2) {
		amt_yli2 = amtYli2;
	}

	public double getAmt_sye2() {
		return amt_sye2;
	}

	public void setAmt_sye2(double amtSye2) {
		amt_sye2 = amtSye2;
	}

	public java.lang.Double getSspbase() {
		return sspbase;
	}

	public void setSspbase(java.lang.Double sspbase) {
		this.sspbase = sspbase;
	}

	public java.lang.Double getPfpbase() {
		return pfpbase;
	}

	public void setPfpbase(java.lang.Double pfpbase) {
		this.pfpbase = pfpbase;
	}

	public Date getDatebegin() {
		return datebegin;
	}

	public void setDatebegin(Date datebegin) {
		this.datebegin = datebegin;
	}

	public Date getDateend() {
		return dateend;
	}

	public void setDateend(Date dateend) {
		this.dateend = dateend;
	}

	public String getJobno() {
		return jobno;
	}

	public void setJobno(String jobno) {
		this.jobno = jobno;
	}


	
	

}