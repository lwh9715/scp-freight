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
@Table(name = "sys_log")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id_tmp",sequenceName="seq_id_tmp")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id_tmp") }) 
public class SysLog implements java.io.Serializable {

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
	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;

	/**
	 *@generated
	 */
	@Column(name = "logtime", length = 29)
	private java.util.Date logtime;

	/**
	 *@generated
	 */
	@Column(name = "logdesc")
	private java.lang.String logdesc;

	/**
	 *@generated
	 */
	@Column(name = "refid")
	private java.lang.Long refid;
	
	@Column(name = "logtype")
	private java.lang.String logtype;

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
	public java.lang.String getInputer() {
		return this.inputer;
	}

	/**
	 *@generated
	 */
	public void setInputer(java.lang.String value) {
		this.inputer = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getLogtime() {
		return this.logtime;
	}

	/**
	 *@generated
	 */
	public void setLogtime(java.util.Date value) {
		this.logtime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getLogdesc() {
		return this.logdesc;
	}

	/**
	 *@generated
	 */
	public void setLogdesc(java.lang.String value) {
		this.logdesc = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getRefid() {
		return this.refid;
	}

	/**
	 *@generated
	 */
	public void setRefid(java.lang.Long value) {
		this.refid = value;
	}

	public java.lang.String getLogtype() {
		return logtype;
	}

	public void setLogtype(java.lang.String logtype) {
		this.logtype = logtype;
	}
}