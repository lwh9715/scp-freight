package com.scp.model.ship;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


@Table(name = "bus_freightcharge")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusFreightCharge implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;
	
	/**
	 *@generated
	 */
	@Column(name = "jobid ")
	private java.lang.Long jobid;
	
	/**
	 *@generated
	 */
	@Column(name = "freightcharge", length = 50)
	private java.lang.String freightcharge;

	/**
	 *@generated
	 */
	@Column(name = "rate", length = 30)
	private java.lang.String rate;
	
	/**
	 *@generated
	 */
	@Column(name = "prepaid", length = 30)
	private java.lang.String prepaid;
	
	/**
	 *@generated
	 */
	@Column(name = "collect", length = 30)
	private java.lang.String collect;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.Long getJobid() {
		return jobid;
	}

	public void setJobid(java.lang.Long jobid) {
		this.jobid = jobid;
	}

	public java.lang.String getFreightcharge() {
		return freightcharge;
	}

	public void setFreightcharge(java.lang.String freightcharge) {
		this.freightcharge = freightcharge;
	}

	public java.lang.String getRate() {
		return rate;
	}

	public void setRate(java.lang.String rate) {
		this.rate = rate;
	}

	public java.lang.String getPrepaid() {
		return prepaid;
	}

	public void setPrepaid(java.lang.String prepaid) {
		this.prepaid = prepaid;
	}

	public java.lang.String getCollect() {
		return collect;
	}

	public void setCollect(java.lang.String collect) {
		this.collect = collect;
	}
	
}