package com.scp.model.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "bus_unsubscribe")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class Unsubscribe implements java.io.Serializable {
	
	@Column(name = "id")
	@Id
	@GeneratedValue(generator="idStrategy")
	private Long id;
	
	@Column(name = "email", length = 30)
	private String email;
	
	@Column(name = "reason")
	private String reason;
	
	@Column(name = "date", length = 29)
	private Date date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getDate() {
		return date;
	}
 
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Unsubscribe [date=" + date + ", email=" + email + ", id=" + id
				+ ", reason=" + reason + "]";
	}
	
	
}
