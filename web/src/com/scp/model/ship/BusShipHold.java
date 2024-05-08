package com.scp.model.ship;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "bus_ship_hold")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusShipHold implements java.io.Serializable {

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
	@Column(name = "holdtype")
	private java.lang.String holdtype;

	/**
	 *@generated
	 */
	@Column(name = "holdstate")
	private java.lang.String holdstate;
	@Column(name = "remarks")
	private java.lang.String remarks;

	@Column(name = "linktbl")
	private java.lang.String linktbl;

	/**
	 *@generated
	 */
	@Column(name = "linkid")
	private java.lang.Long linkid;

	/**
	 *@generated
	 */
	@Column(name = "inputer", length = 30)
	private java.lang.String inputer;

	/**
	 *@generated
	 */
	@Column(name = "inputtime")
	private java.util.Date inputtime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getHoldtype() {
		return holdtype;
	}

	public void setHoldtype(java.lang.String holdtype) {
		this.holdtype = holdtype;
	}

	public java.lang.String getHoldstate() {
		return holdstate;
	}

	public void setHoldstate(java.lang.String holdstate) {
		this.holdstate = holdstate;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
	}

	public java.lang.String getLinktbl() {
		return linktbl;
	}

	public void setLinktbl(java.lang.String linktbl) {
		this.linktbl = linktbl;
	}

	public java.lang.Long getLinkid() {
		return linkid;
	}

	public void setLinkid(java.lang.Long linkid) {
		this.linkid = linkid;
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

}