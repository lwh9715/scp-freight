package com.scp.model.bus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "dat_airplanetype")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class AirPlaneType implements java.io.Serializable{
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator = "idStrategy")
	private long id;

	
	@Column(name = "planetype", length = 100)
	private java.lang.String planetype;
	
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

	public java.lang.String getPlanetype() {
		return planetype;
	}

	public void setPlanetype(java.lang.String planetype) {
		this.planetype = planetype;
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
