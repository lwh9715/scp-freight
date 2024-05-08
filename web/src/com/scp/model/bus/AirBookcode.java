package com.scp.model.bus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "air_bookingcode")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class AirBookcode implements java.io.Serializable{
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	@Column(name = "airlineid")
	private java.lang.Long airlineid;
	
	@Column(name = "bookcode", length = 50)
	private java.lang.String bookcode;
	
	@Column(name = "doccode", length = 50)
	private java.lang.String doccode;
	
	@Column(name = "bookabbr", length = 50)
	private java.lang.String bookabbr;
	
	@Column(name = "bookname", length = 100)
	private java.lang.String bookname;
	
	@Column(name = "orderno", nullable = false)
	private short orderno;
	
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;
	
	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;

	@Column(name = "updater", length = 100)
	private java.lang.String updater;

	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.Long getAirlineid() {
		return airlineid;
	}

	public void setAirlineid(java.lang.Long airlineid) {
		this.airlineid = airlineid;
	}

	public java.lang.String getBookcode() {
		return bookcode;
	}

	public void setBookcode(java.lang.String bookcode) {
		this.bookcode = bookcode;
	}

	public java.lang.String getDoccode() {
		return doccode;
	}

	public void setDoccode(java.lang.String doccode) {
		this.doccode = doccode;
	}

	public java.lang.String getBookabbr() {
		return bookabbr;
	}

	public void setBookabbr(java.lang.String bookabbr) {
		this.bookabbr = bookabbr;
	}

	public java.lang.String getBookname() {
		return bookname;
	}

	public void setBookname(java.lang.String bookname) {
		this.bookname = bookname;
	}

	public short getOrderno() {
		return orderno;
	}

	public void setOrderno(short orderno) {
		this.orderno = orderno;
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
