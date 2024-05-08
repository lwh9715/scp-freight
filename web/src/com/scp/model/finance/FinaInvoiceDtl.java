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
@Table(name = "fina_invoice_dtl")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class FinaInvoiceDtl implements java.io.Serializable {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "invoiceid")
	private java.lang.Long invoiceid;

	@Column(name = "feeitemdec")
	private java.lang.String feeitemdec;
	
	@Column(name = "araptype")
	private java.lang.String araptype;
	
	@Column(name = "currency")
	private java.lang.String currency;
	
	@Column(name = "amount")
	private java.math.BigDecimal amount;
	
	@Column(name = "invoicextype")
	private java.lang.String invoicextype;
	
	@Column(name = "invoicexrate")
	private java.math.BigDecimal invoicexrate;
	
	@Column(name = "invoiceamountflag")
	private java.math.BigDecimal invoiceamountflag;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.Long getInvoiceid() {
		return invoiceid;
	}

	public void setInvoiceid(java.lang.Long invoiceid) {
		this.invoiceid = invoiceid;
	}

	public java.lang.String getFeeitemdec() {
		return feeitemdec;
	}

	public void setFeeitemdec(java.lang.String feeitemdec) {
		this.feeitemdec = feeitemdec;
	}

	public java.lang.String getAraptype() {
		return araptype;
	}

	public void setAraptype(java.lang.String araptype) {
		this.araptype = araptype;
	}

	public java.lang.String getCurrency() {
		return currency;
	}

	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}

	public java.math.BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(java.math.BigDecimal amount) {
		this.amount = amount;
	}

	public java.lang.String getInvoicextype() {
		return invoicextype;
	}

	public void setInvoicextype(java.lang.String invoicextype) {
		this.invoicextype = invoicextype;
	}

	public java.math.BigDecimal getInvoicexrate() {
		return invoicexrate;
	}

	public void setInvoicexrate(java.math.BigDecimal invoicexrate) {
		this.invoicexrate = invoicexrate;
	}

	public java.math.BigDecimal getInvoiceamountflag() {
		return invoiceamountflag;
	}

	public void setInvoiceamountflag(java.math.BigDecimal invoiceamountflag) {
		this.invoiceamountflag = invoiceamountflag;
	}
}