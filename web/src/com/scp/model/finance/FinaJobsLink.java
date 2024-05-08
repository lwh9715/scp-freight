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
@Table(name = "fina_jobs_link")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class FinaJobsLink implements java.io.Serializable {

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
	@Column(name = "jobidfm")
	private java.lang.Long jobidfm;

	/**
	 *@generated
	 */
	@Column(name = "jobidto")
	private java.lang.Long jobidto;

	/**
	 *@generated
	 */
	public long getId() {
		return this.id;
	}

	/**
	 *@generated
	 */
	public void setId(long value) {
		this.id = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getJobidfm() {
		return this.jobidfm;
	}

	/**
	 *@generated
	 */
	public void setJobidfm(java.lang.Long value) {
		this.jobidfm = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getJobidto() {
		return this.jobidto;
	}

	/**
	 *@generated
	 */
	public void setJobidto(java.lang.Long value) {
		this.jobidto = value;
	}
}