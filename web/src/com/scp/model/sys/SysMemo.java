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
@Table(name = "sys_memo")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysMemo implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;

	/**
	 *@generated
	 */
	@Column(name = "name", length = 200)
	private java.lang.String name;

	/**
	 *@generated
	 */
	@Column(name = "remindertimefm", length = 29)
	private java.util.Date remindertimefm;

	/**
	 *@generated
	 */
	@Column(name = "remindertimeend", length = 29)
	private java.util.Date remindertimeend;

	/**
	 *@generated
	 */
	@Column(name = "contents")
	private java.lang.String contents;

	/**
	 *@generated
	 */
	@Column(name = "grade", length = 20)
	private java.lang.String grade;

	/**
	 *@generated
	 */
	@Column(name = "ispublic")
	private java.lang.Boolean ispublic;

	/**
	 *@generated
	 */
	@Column(name = "isvalid")
	private java.lang.Boolean isvalid;

	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String remarks;

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
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

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
	public java.lang.String getName() {
		return this.name;
	}

	/**
	 *@generated
	 */
	public void setName(java.lang.String value) {
		this.name = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getRemindertimefm() {
		return this.remindertimefm;
	}

	/**
	 *@generated
	 */
	public void setRemindertimefm(java.util.Date value) {
		this.remindertimefm = value;
	}

	/**
	 *@generated
	 */
	public java.util.Date getRemindertimeend() {
		return this.remindertimeend;
	}

	/**
	 *@generated
	 */
	public void setRemindertimeend(java.util.Date value) {
		this.remindertimeend = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getContents() {
		return this.contents;
	}

	/**
	 *@generated
	 */
	public void setContents(java.lang.String value) {
		this.contents = value;
	}

	/**
	 *@generated
	 */
	public java.lang.String getGrade() {
		return this.grade;
	}

	/**
	 *@generated
	 */
	public void setGrade(java.lang.String value) {
		this.grade = value;
	}


	public java.lang.Boolean getIspublic() {
		return ispublic;
	}

	public void setIspublic(java.lang.Boolean ispublic) {
		this.ispublic = ispublic;
	}

	public java.lang.Boolean getIsvalid() {
		return isvalid;
	}

	public void setIsvalid(java.lang.Boolean isvalid) {
		this.isvalid = isvalid;
	}

	/**
	 *@generated
	 */
	public java.lang.String getRemarks() {
		return this.remarks;
	}

	/**
	 *@generated
	 */
	public void setRemarks(java.lang.String value) {
		this.remarks = value;
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
	public java.util.Date getInputtime() {
		return this.inputtime;
	}

	/**
	 *@generated
	 */
	public void setInputtime(java.util.Date value) {
		this.inputtime = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Boolean getIsdelete() {
		return this.isdelete;
	}

	/**
	 *@generated
	 */
	public void setIsdelete(java.lang.Boolean value) {
		this.isdelete = value;
	}
}