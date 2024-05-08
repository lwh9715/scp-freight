package com.scp.model.data;

import java.math.BigDecimal;

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
@Table(name = "dat_door2doorday")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class Datdoor2doorday implements java.io.Serializable {

	/**
	 *@generated
	 */
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;

	@Column(name = "polid")
	private BigDecimal polid ;
	
	@Column(name = "destinationid")
	private BigDecimal destinationid ;
	
	@Column(name = "cntypeid")
	private BigDecimal cntypeid ;
	
	@Column(name = "googstypeid")
	private BigDecimal googstypeid ;
	
	@Column(name = "transday")
	private int transday ;

	/**
	 *@generated
	 */
	@Column(name = "remarks")
	private java.lang.String namee;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getPolid() {
		return polid;
	}

	public void setPolid(BigDecimal polid) {
		this.polid = polid;
	}

	public BigDecimal getCntypeid() {
		return cntypeid;
	}

	public void setCntypeid(BigDecimal cntypeid) {
		this.cntypeid = cntypeid;
	}

	public BigDecimal getGoogstypeid() {
		return googstypeid;
	}

	public void setGoogstypeid(BigDecimal googstypeid) {
		this.googstypeid = googstypeid;
	}

	public int getTransday() {
		return transday;
	}

	public void setTransday(int transday) {
		this.transday = transday;
	}

	public java.lang.String getNamee() {
		return namee;
	}

	public void setNamee(java.lang.String namee) {
		this.namee = namee;
	}

	public BigDecimal getDestinationid() {
		return destinationid;
	}

	public void setDestinationid(BigDecimal destinationid) {
		this.destinationid = destinationid;
	}
	
}