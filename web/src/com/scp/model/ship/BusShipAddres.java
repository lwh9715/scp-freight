package com.scp.model.ship;

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
@Table(name = "bus_ship_addres")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusShipAddres implements java.io.Serializable {

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
	@Column(name = "linkid")
	private java.lang.Long linkid;
	
	/**
	 *@generated
	 */
	@Column(name = "linktype", length = 1)
	private java.lang.String linktype;
	
	/**
	 *@generated
	 */
	@Column(name = "cneename")
	private java.lang.String cneename;
	
	/**
	 *@generated
	 */
	@Column(name = "sendaddress")
	private java.lang.String sendaddress;
	
	/**
	 *@generated
	 */
	@Column(name = "sendcontact")
	private java.lang.String sendcontact;
	
	/**
	 *@generated
	 */
	@Column(name = "sendtel")
	private java.lang.String sendtel;
	
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

	public java.lang.Long getLinkid() {
		return linkid;
	}

	public void setLinkid(java.lang.Long linkid) {
		this.linkid = linkid;
	}

	public java.lang.String getLinktype() {
		return linktype;
	}

	public void setLinktype(java.lang.String linktype) {
		this.linktype = linktype;
	}

	public java.lang.String getCneename() {
		return cneename;
	}

	public void setCneename(java.lang.String cneename) {
		this.cneename = cneename;
	}

	public java.lang.String getSendaddress() {
		return sendaddress;
	}

	public void setSendaddress(java.lang.String sendaddress) {
		this.sendaddress = sendaddress;
	}

	public java.lang.String getSendcontact() {
		return sendcontact;
	}

	public void setSendcontact(java.lang.String sendcontact) {
		this.sendcontact = sendcontact;
	}

	public java.lang.String getSendtel() {
		return sendtel;
	}

	public void setSendtel(java.lang.String sendtel) {
		this.sendtel = sendtel;
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