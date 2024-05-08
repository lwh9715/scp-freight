package com.scp.model.data;

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
@Table(name = "dat_package")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class DatPackage implements java.io.Serializable {

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
	@Column(name = "code", length = 20)
	private java.lang.String code;

	/**
	 *@generated
	 */
	@Column(name = "namec", length = 30)
	private java.lang.String namec;

	/**
	 *@generated
	 */
	@Column(name = "namee", length = 30)
	private java.lang.String namee;
	
	@Column(name = "corpid")
	private Long corpid;

	
	
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

	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	public long getId() {
		return id;
	}

	public java.lang.String getCode() {
		return code;
	}

	public java.lang.String getNamec() {
		return namec;
	}

	public java.lang.String getNamee() {
		return namee;
	}

	public Long getCorpid() {
		return corpid;
	}

	public java.lang.String getInputer() {
		return inputer;
	}

	public java.util.Date getInputtime() {
		return inputtime;
	}

	public java.lang.String getUpdater() {
		return updater;
	}

	public java.util.Date getUpdatetime() {
		return updatetime;
	}

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setCode(java.lang.String code) {
		this.code = code;
	}

	public void setNamec(java.lang.String namec) {
		this.namec = namec;
	}

	public void setNamee(java.lang.String namee) {
		this.namee = namee;
	}

	public void setCorpid(Long corpid) {
		this.corpid = corpid;
	}

	public void setInputer(java.lang.String inputer) {
		this.inputer = inputer;
	}

	public void setInputtime(java.util.Date inputtime) {
		this.inputtime = inputtime;
	}

	public void setUpdater(java.lang.String updater) {
		this.updater = updater;
	}

	public void setUpdatetime(java.util.Date updatetime) {
		this.updatetime = updatetime;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
	}


}