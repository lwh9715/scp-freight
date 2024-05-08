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
@Table(name = "oa_user_visa")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class OaUserVisa implements java.io.Serializable {

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
	@Column(name = "userinfoid")
	private java.lang.Long userinfoid;

	@Column(name = "datestar")
	private java.util.Date datestar;

	@Column(name = "datend")
	private java.util.Date datend;
	
	@Column(name = "cyid",length=3)
	private java.lang.String cyid;
	
	@Column(name = "amt")
	private java.lang.Double amt;
	
	@Column(name = "cyid2",length=3)
	private java.lang.String cyid2;
	
	@Column(name = "amtperson")
	private java.lang.Double amtperson;
	

	@Column(name = "detail")
	private java.lang.String detail;

	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

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

	public java.lang.Long getUserinfoid() {
		return userinfoid;
	}

	public void setUserinfoid(java.lang.Long userinfoid) {
		this.userinfoid = userinfoid;
	}

	public java.util.Date getDatestar() {
		return datestar;
	}

	public void setDatestar(java.util.Date datestar) {
		this.datestar = datestar;
	}

	public java.util.Date getDatend() {
		return datend;
	}

	public void setDatend(java.util.Date datend) {
		this.datend = datend;
	}
	
	public java.lang.String getCyid() {
		return cyid;
	}

	public void setCyid(java.lang.String cyid) {
		this.cyid = cyid;
	}

	public java.lang.Double getAmt() {
		return amt;
	}

	public void setAmt(java.lang.Double amt) {
		this.amt = amt;
	}

	public java.lang.String getCyid2() {
		return cyid2;
	}

	public void setCyid2(java.lang.String cyid2) {
		this.cyid2 = cyid2;
	}

	public java.lang.Double getAmtperson() {
		return amtperson;
	}

	public void setAmtperson(java.lang.Double amtperson) {
		this.amtperson = amtperson;
	}

	public java.lang.String getDetail() {
		return detail;
	}

	public void setDetail(java.lang.String detail) {
		this.detail = detail;
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
