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

@Table(name = "oa_fee")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class OaFee implements java.io.Serializable {

	// Fields
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	@Column(name = "userinfoid")
	private long userinfoid;

	@Column(name = "emaildate")
	private Date emaildate;

	@Column(name = "feedate")
	private Date feedate;

	@Column(name = "fee_jobnos")
	private String fee_jobnos;

	@Column(name = "amt")
	private double amt;

	@Column(name = "jobno")
	private String jobno;

	@Column(name = "name")
	private String name;

	@Column(name = "thingdes")
	private String thingdes;

	@Column(name = "quarter")
	private String quarter;

	@Column(name = "dutyer")
	private String dutyer;

	@Column(name = "depart")
	private String depart;

	@Column(name = "person_amt")
	private double person_amt;

	@Column(name = "person_pra_amt")
	private double person_pra_amt;

	@Column(name = "currency")
	private String currency;

	@Column(name = "perfor_amt")
	private double perfor_amt;

	@Column(name = "perfor_amt_from")
	private String perfor_amt_from;

	@Column(name = "corp_amt")
	private double corp_amt;

	@Column(name = "comm_case")
	private String comm_case;

	@Column(name = "suggest")
	private String suggest;

	@Column(name = "follow_states")
	private String follow_states;

	@Column(name = "amt_states")
	private String amt_states;

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

	public Date getFeedate() {
		return this.feedate;
	}

	public void setFeedate(Date feedate) {
		this.feedate = feedate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmt() {
		return this.amt;
	}

	public void setAmt(double amt) {
		this.amt = amt;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public Date getEmaildate() {
		return emaildate;
	}

	public void setEmaildate(Date emaildate) {
		this.emaildate = emaildate;
	}

	public String getFee_jobnos() {
		return fee_jobnos;
	}

	public void setFee_jobnos(String feeJobnos) {
		fee_jobnos = feeJobnos;
	}

	public String getJobno() {
		return jobno;
	}

	public void setJobno(String jobno) {
		this.jobno = jobno;
	}

	public String getThingdes() {
		return thingdes;
	}

	public void setThingdes(String thingdes) {
		this.thingdes = thingdes;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public String getDutyer() {
		return dutyer;
	}

	public void setDutyer(String dutyer) {
		this.dutyer = dutyer;
	}

	public String getDepart() {
		return depart;
	}

	public void setDepart(String depart) {
		this.depart = depart;
	}

	public double getPerson_amt() {
		return person_amt;
	}

	public void setPerson_amt(double personAmt) {
		person_amt = personAmt;
	}

	public double getPerson_pra_amt() {
		return person_pra_amt;
	}

	public void setPerson_pra_amt(double personPraAmt) {
		person_pra_amt = personPraAmt;
	}

	public double getPerfor_amt() {
		return perfor_amt;
	}

	public void setPerfor_amt(double perforAmt) {
		perfor_amt = perforAmt;
	}

	public String getPerfor_amt_from() {
		return perfor_amt_from;
	}

	public void setPerfor_amt_from(String perforAmtFrom) {
		perfor_amt_from = perforAmtFrom;
	}

	public double getCorp_amt() {
		return corp_amt;
	}

	public void setCorp_amt(double corpAmt) {
		corp_amt = corpAmt;
	}

	public String getComm_case() {
		return comm_case;
	}

	public void setComm_case(String commCase) {
		comm_case = commCase;
	}

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	public String getFollow_states() {
		return follow_states;
	}

	public void setFollow_states(String followStates) {
		follow_states = followStates;
	}

	public String getAmt_states() {
		return amt_states;
	}

	public void setAmt_states(String amtStates) {
		amt_states = amtStates;
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

}