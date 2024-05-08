package com.scp.model.gps;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "bus_gps_ref")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class BusGpsRef implements java.io.Serializable {

	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "gpsidno ", length = 30)
	private java.lang.String gpsidno;
	
	@Column(name = "jobno ", length = 30)
	private java.lang.String jobno;

	@Column(name = "hblno ", length = 30)
	private java.lang.String hblno;
	
	@Column(name = "mblno ", length = 30)
	private java.lang.String mblno;
	
	@Column(name = "sono ", length = 30)
	private java.lang.String sono;
	
	@Column(name = "cntno ", length = 30)
	private java.lang.String cntno;
	
	@Column(name = "trainno ", length = 30)
	private java.lang.String trainno;
	
	@Column(name = "status ", length = 30)
	private java.lang.String status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.String getGpsidno() {
		return gpsidno;
	}

	public void setGpsidno(java.lang.String gpsidno) {
		this.gpsidno = gpsidno;
	}

	public java.lang.String getJobno() {
		return jobno;
	}

	public void setJobno(java.lang.String jobno) {
		this.jobno = jobno;
	}

	public java.lang.String getHblno() {
		return hblno;
	}

	public void setHblno(java.lang.String hblno) {
		this.hblno = hblno;
	}

	public java.lang.String getMblno() {
		return mblno;
	}

	public void setMblno(java.lang.String mblno) {
		this.mblno = mblno;
	}

	public java.lang.String getSono() {
		return sono;
	}

	public void setSono(java.lang.String sono) {
		this.sono = sono;
	}

	public java.lang.String getCntno() {
		return cntno;
	}

	public void setCntno(java.lang.String cntno) {
		this.cntno = cntno;
	}

	public java.lang.String getTrainno() {
		return trainno;
	}

	public void setTrainno(java.lang.String trainno) {
		this.trainno = trainno;
	}

	public java.lang.String getStatus() {
		return status;
	}

	public void setStatus(java.lang.String status) {
		this.status = status;
	}
	
}