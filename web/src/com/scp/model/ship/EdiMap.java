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
@Table(name = "edi_map")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class EdiMap implements java.io.Serializable {

	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	@Column(name = "key", length = 20)
	private java.lang.String key;

	@Column(name = "valc", length = 50)
	private java.lang.String valc;

	@Column(name = "vale", length = 50)
	private java.lang.String vale;

	@Column(name = "orderno")
	private java.lang.Integer orderno;

	@Column(name = "maptype", length = 20)
	private java.lang.String maptype;

	@Column(name = "linkid")
	private java.lang.Long linkid;

	@Column(name = "linktbl", length = 30)
	private java.lang.String linktbl;
	
	@Column(name = "stscode", length = 30)
	private java.lang.String stscode;
	
	
	public java.lang.String getStscode() {
		return stscode;
	}

	public void setStscode(java.lang.String stscode) {
		this.stscode = stscode;
	}

	public java.lang.String getEditype() {
		return editype;
	}

	public void setEditype(java.lang.String editype) {
		this.editype = editype;
	}

	@Column(name = "editype")
	private java.lang.String editype;

	@Column(name = "remarks")
	private java.lang.String remarks;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getKey() {
		return key;
	}

	public void setKey(java.lang.String key) {
		this.key = key;
	}

	public java.lang.String getValc() {
		return valc;
	}

	public void setValc(java.lang.String valc) {
		this.valc = valc;
	}

	public java.lang.String getVale() {
		return vale;
	}

	public void setVale(java.lang.String vale) {
		this.vale = vale;
	}

	public java.lang.Integer getOrderno() {
		return orderno;
	}

	public void setOrderno(java.lang.Integer orderno) {
		this.orderno = orderno;
	}

	public java.lang.String getMaptype() {
		return maptype;
	}

	public void setMaptype(java.lang.String maptype) {
		this.maptype = maptype;
	}

	public java.lang.Long getLinkid() {
		return linkid;
	}

	public void setLinkid(java.lang.Long linkid) {
		this.linkid = linkid;
	}

	public java.lang.String getLinktbl() {
		return linktbl;
	}

	public void setLinktbl(java.lang.String linktbl) {
		this.linktbl = linktbl;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
	}

}