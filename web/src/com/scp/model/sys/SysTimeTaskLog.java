package com.scp.model.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "sys_timetask_log")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id_tmp",sequenceName="seq_id_tmp")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id_tmp") }) 
public class SysTimeTaskLog implements java.io.Serializable {
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "timetaskid")
	private long timetaskid;
	
	@Column(name = "logtime")
	private java.util.Date logtime;
	
	@Column(name = "logstatus")
	private String logstatus; // 状态
	
	@Column(name = "loginfo")
	private String loginfo; // 

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTimetaskid() {
		return timetaskid;
	}

	public void setTimetaskid(long timetaskid) {
		this.timetaskid = timetaskid;
	}

	public java.util.Date getLogtime() {
		return logtime;
	}

	public void setLogtime(java.util.Date logtime) {
		this.logtime = logtime;
	}

	public String getLogstatus() {
		return logstatus;
	}

	public void setLogstatus(String logstatus) {
		this.logstatus = logstatus;
	}

	public String getLoginfo() {
		return loginfo;
	}

	public void setLoginfo(String loginfo) {
		this.loginfo = loginfo;
	}
	
}
