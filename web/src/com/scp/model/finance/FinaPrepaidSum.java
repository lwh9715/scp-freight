package com.scp.model.finance;

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
@Table(name = "fina_prepaid_sum")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class FinaPrepaidSum implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "prepaid")
	private Long prepaid;
	
	@Column(name = "rp", length = 1)
	private java.lang.String rp;
	
	@Column(name = "cyid", length = 3)
	private java.lang.String cyid;
	
	@Column(name = "amt")
	private java.math.BigDecimal amt;
	
	@Column(name = "bankid")
	private Long bankid;
	
	@Column(name = "chequeno", length = 30)
	private java.lang.String chequeno;
	
	@Column(name = "chequeenddate")
	private java.util.Date chequeenddate;
	
	@Column(name = "corpid")
	private Long corpid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getPrepaid() {
		return prepaid;
	}

	public void setPrepaid(Long prepaid) {
		this.prepaid = prepaid;
	}

	public java.lang.String getRp() {
		return rp;
	}

	public void setRp(java.lang.String rp) {
		this.rp = rp;
	}

	public java.lang.String getCyid() {
		return cyid;
	}

	public void setCyid(java.lang.String cyid) {
		this.cyid = cyid;
	}

	public java.math.BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(java.math.BigDecimal amt) {
		this.amt = amt;
	}

	public Long getBankid() {
		return bankid;
	}

	public void setBankid(Long bankid) {
		this.bankid = bankid;
	}

	public java.lang.String getChequeno() {
		return chequeno;
	}

	public void setChequeno(java.lang.String chequeno) {
		this.chequeno = chequeno;
	}

	public java.util.Date getChequeenddate() {
		return chequeenddate;
	}

	public void setChequeenddate(java.util.Date chequeenddate) {
		this.chequeenddate = chequeenddate;
	}

	public Long getCorpid() {
		return corpid;
	}

	public void setCorpid(Long corpid) {
		this.corpid = corpid;
	}
	
	
}