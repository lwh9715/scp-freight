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
@Table(name = "dat_reviewedor")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class DatReviewedor implements java.io.Serializable {

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
	@Column(name = "corpid")
	private long corpid;

	/**
	 *@generated
	 */
	@Column(name = "userid")
	private long userid;

	/**
	 *@generated
	 */
	@Column(name = "processid")
	private long processid;
	
	
	/**
	 *@generated
	 */
	@Column(name = "assignname")
	private java.lang.String assignname;
	
	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCorpid() {
		return corpid;
	}

	public void setCorpid(long corpid) {
		this.corpid = corpid;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getProcessid() {
		return processid;
	}

	public void setProcessid(long processid) {
		this.processid = processid;
	}

	public java.lang.String getAssignname() {
		return assignname;
	}

	public void setAssignname(java.lang.String assignname) {
		this.assignname = assignname;
	}

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
	}
	
}