package com.scp.model.wms;

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
@Table(name = "wms_in_locdtl")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class WmsInLocdtl implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;
	
	@Column(name = "corpid")
	private Long corpid;

	public Long getCorpid() {
		return corpid;
	}

	public void setCorpid(Long corpid) {
		this.corpid = corpid;
	}

	/**
	 *@generated
	 */
	@Column(name = "warehouseid")
	private java.lang.Long warehouseid;

	/**
	 *@generated
	 */
	@Column(name = "indtlid")
	private java.lang.Long indtlid;

	public java.lang.Long getIndtlid() {
		return indtlid;
	}

	public void setIndtlid(java.lang.Long indtlid) {
		this.indtlid = indtlid;
	}

	/**
	 *@generated
	 */
	@Column(name = "areaid")
	private java.lang.Long areaid;

	/**
	 *@generated
	 */
	@Column(name = "locid")
	private java.lang.Long locid;


	/**
	 *@generated
	 */
	@Column(name = "pieces")
	private java.math.BigDecimal pieces;
	
	public java.math.BigDecimal getPieceout() {
		return pieceout;
	}

	public void setPieceout(java.math.BigDecimal pieceout) {
		this.pieceout = pieceout;
	}

	/**
	 *@generated
	 */
	@Column(name = "pieceout")
	private java.math.BigDecimal pieceout;
	
	
	
	
	
	
	/**
	 *@generated
	 */
	@Column(name = "iscompleted")
	private java.lang.Boolean iscompleted;

	public java.lang.Boolean getIscompleted() {
		return iscompleted;
	}

	public void setIscompleted(java.lang.Boolean iscompleted) {
		this.iscompleted = iscompleted;
	}

	/**
	 *@generated
	 */
	@Column(name = "gdscbm")
	private java.math.BigDecimal gdscbm;

	/**
	 *@generated
	 */
	@Column(name = "gdswgt")
	private java.math.BigDecimal gdswgt;

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
	public java.lang.Long getWarehouseid() {
		return this.warehouseid;
	}

	/**
	 *@generated
	 */
	public void setWarehouseid(java.lang.Long value) {
		this.warehouseid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getAreaid() {
		return this.areaid;
	}

	/**
	 *@generated
	 */
	public void setAreaid(java.lang.Long value) {
		this.areaid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getLocid() {
		return this.locid;
	}

	/**
	 *@generated
	 */
	public void setLocid(java.lang.Long value) {
		this.locid = value;
	}


	/**
	 *@generated
	 */
	public java.math.BigDecimal getPieces() {
		return this.pieces;
	}

	/**
	 *@generated
	 */
	public void setPieces(java.math.BigDecimal value) {
		this.pieces = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGdscbm() {
		return this.gdscbm;
	}

	/**
	 *@generated
	 */
	public void setGdscbm(java.math.BigDecimal value) {
		this.gdscbm = value;
	}

	/**
	 *@generated
	 */
	public java.math.BigDecimal getGdswgt() {
		return this.gdswgt;
	}

	/**
	 *@generated
	 */
	public void setGdswgt(java.math.BigDecimal value) {
		this.gdswgt = value;
	}
}