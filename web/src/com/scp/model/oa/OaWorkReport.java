package com.scp.model.oa;

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
@Table(name = "oa_workreport")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class OaWorkReport implements java.io.Serializable {

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
	@Column(name = "reptype", length = 1)
	private java.lang.String reptype;

	/**
	 *@generated
	 */
	@Column(name = "timestart", length = 35)
	private java.util.Date timestart;
	
	/**
	 *@generated
	 */
	@Column(name = "timeend", length = 35)
	private java.util.Date timeend;

	/**
	 *@generated
	 */
	@Column(name = "weekno")
	private java.lang.Long weekno;

	/**
	 *@generated
	 */
	@Column(name = "reptitle", length = 100)
	private java.lang.String reptitle;

	/**
	 *@generated
	 */
	@Column(name = "repcontent")
	private java.lang.String repcontent;

	/**
	 *@generated
	 */
	@Column(name = "rep2userid")
	private java.lang.Long rep2userid;
	
	/**
	 *@generated
	 */
	@Column(name = "isemailsent")
	private java.lang.Boolean isemailsent;

	/**
	 *@generated
	 */
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	
	public java.lang.String getReptype() {
		return reptype;
	}

	public void setReptype(java.lang.String reptype) {
		this.reptype = reptype;
	}

	public java.util.Date getTimestart() {
		return timestart;
	}

	public void setTimestart(java.util.Date timestart) {
		this.timestart = timestart;
	}

	public java.util.Date getTimeend() {
		return timeend;
	}

	public void setTimeend(java.util.Date timeend) {
		this.timeend = timeend;
	}

	public java.lang.Long getWeekno() {
		return weekno;
	}

	public void setWeekno(java.lang.Long weekno) {
		this.weekno = weekno;
	}

	public java.lang.String getReptitle() {
		return reptitle;
	}

	public void setReptitle(java.lang.String reptitle) {
		this.reptitle = reptitle;
	}

	public java.lang.String getRepcontent() {
		return repcontent;
	}

	public void setRepcontent(java.lang.String repcontent) {
		this.repcontent = repcontent;
	}

	public java.lang.Long getRep2userid() {
		return rep2userid;
	}

	public void setRep2userid(java.lang.Long rep2userid) {
		this.rep2userid = rep2userid;
	}

	public java.lang.Boolean getIsemailsent() {
		return isemailsent;
	}

	public void setIsemailsent(java.lang.Boolean isemailsent) {
		this.isemailsent = isemailsent;
	}

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
	}

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

	public java.lang.String getUpdater() {
		return updater;
	}

	public void setUpdater(java.lang.String updater) {
		this.updater = updater;
	}

	public java.util.Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(java.util.Date updatetime) {
		this.updatetime = updatetime;
	}

}
