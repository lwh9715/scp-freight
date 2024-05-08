package com.scp.model.api;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "api_maersk_pricesub")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class ApiMaerskPriceSub implements java.io.Serializable {
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;

	@Column(name = "pol", length = 50)
	private java.lang.String pol;
	
	@Column(name = "pod", length = 50)
	private java.lang.String pod;
	
	@Column(name = "cntcode", length = 10)
	private java.lang.String cntcode;
	
//	@Column(name = "offersjson")
//	private java.lang.String offersjson;
	
	@Column(name = "inputer" , length = 100)
	private java.lang.String inputer;
	
	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;
	
	@Column(name = "vessel" , length = 100)
	private java.lang.String vessel;
	
	@Column(name = "voyage" , length = 100)
	private java.lang.String voyage;
	
	@Column(name = "args")
	private java.lang.String args;
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getPol() {
		return pol;
	}

	public void setPol(java.lang.String pol) {
		this.pol = pol;
	}

	public java.lang.String getPod() {
		return pod;
	}

	public void setPod(java.lang.String pod) {
		this.pod = pod;
	}

	public java.lang.String getCntcode() {
		return cntcode;
	}

	public void setCntcode(java.lang.String cntcode) {
		this.cntcode = cntcode;
	}

//	public java.lang.String getOffersjson() {
//		return offersjson;
//	}
//
//	public void setOffersjson(java.lang.String offersjson) {
//		this.offersjson = offersjson;
//	}

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

	public java.lang.String getVessel() {
		return vessel;
	}

	public void setVessel(java.lang.String vessel) {
		this.vessel = vessel;
	}

	public java.lang.String getVoyage() {
		return voyage;
	}

	public void setVoyage(java.lang.String voyage) {
		this.voyage = voyage;
	}

	public java.lang.String getArgs() {
		return args;
	}

	public void setArgs(java.lang.String args) {
		this.args = args;
	}
	
}
