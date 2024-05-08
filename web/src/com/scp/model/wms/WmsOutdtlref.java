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
@Table(name = "wms_outdtlref")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class WmsOutdtlref implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
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
	@Column(name = "outdtlid")
	private java.lang.Long outdtlid;

	/**
	 *@generated
	 */
	@Column(name = "indtlid")
	private java.lang.Long indtlid;

	
	/**
	 *@generated
	 */
	@Column(name = "piece")
	private java.lang.Integer piece;

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
	public java.lang.Long getOutdtlid() {
		return this.outdtlid;
	}

	/**
	 *@generated
	 */
	public void setOutdtlid(java.lang.Long value) {
		this.outdtlid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Long getIndtlid() {
		return this.indtlid;
	}

	/**
	 *@generated
	 */
	public void setIndtlid(java.lang.Long value) {
		this.indtlid = value;
	}

	/**
	 *@generated
	 */
	public java.lang.Integer getPiece() {
		return this.piece;
	}

	/**
	 *@generated
	 */
	public void setPiece(java.lang.Integer value) {
		this.piece = value;
	}
}