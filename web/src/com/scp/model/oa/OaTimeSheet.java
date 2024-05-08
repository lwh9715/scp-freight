package com.scp.model.oa;

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
@Table(name = "oa_timesheet")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@SequenceGenerator(name = "seq_id", sequenceName = "seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native", parameters = { @Parameter(name = "sequence", value = "seq_id") })
public class OaTimeSheet implements java.io.Serializable {

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
	@Column(name = "userinfoid")
	private java.lang.Long userinfoid;
	
	

	@Column(name = "placejob")
	private java.lang.String placejob;

	@Column(name = "year")
	private java.lang.Long year;
	
	@Column(name = "month")
	private java.lang.Long month;
	
	@Column(name = "name")
	private java.lang.String name;
	
	@Column(name = "hoursin1")
	private java.lang.Long hoursin1;
	
	@Column(name = "hoursin2")
	private java.lang.Long hoursin2;
	
	@Column(name = "hoursin3")
	private java.lang.Long hoursin3;
	
	@Column(name = "latersecond")
	private java.lang.Long latersecond;
	
	@Column(name = "latertime")
	private java.lang.Long latertime;
	
	@Column(name = "checklost")
	private java.lang.Long checklost;
	
	@Column(name = "hoursout1")
	private java.lang.Double hoursout1;
	
	@Column(name = "hoursout2")
	private java.lang.Double hoursout2;
	
	@Column(name = "hoursout3")
	private java.lang.Double hoursout3;
	
	@Column(name = "dayout1")
	private java.lang.Double dayout1;
	
	@Column(name = "dayout2")
	private java.lang.Double dayout2;
	
	@Column(name = "fullprize")
	private java.lang.Double fullprize;
	
	@Column(name = "hoursovertime")
	private java.lang.Double hoursovertime;
	
	@Column(name = "remarks")
	private java.lang.String remarks;

	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;

	@Column(name = "inputer", length = 100)
	private java.lang.String inputer;

	/**
	 *@generated
	 */
	@Column(name = "inputtime", length = 29)
	private java.util.Date inputtime;

	/**
	 *@generated
	 */
	@Column(name = "updater", length = 100)
	private java.lang.String updater;

	/**
	 *@generated
	 */
	@Column(name = "updatetime", length = 29)
	private java.util.Date updatetime;
	
	@Column(name = "days")
	private java.lang.Long days;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public java.lang.Long getUserinfoid() {
		return userinfoid;
	}

	public void setUserinfoid(java.lang.Long userinfoid) {
		this.userinfoid = userinfoid;
	}

	public java.lang.String getPlacejob() {
		return placejob;
	}

	public void setPlacejob(java.lang.String placejob) {
		this.placejob = placejob;
	}

	public java.lang.Long getYear() {
		return year;
	}

	public void setYear(java.lang.Long year) {
		this.year = year;
	}

	public java.lang.Long getMonth() {
		return month;
	}

	public void setMonth(java.lang.Long month) {
		this.month = month;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.Long getHoursin1() {
		return hoursin1;
	}

	public void setHoursin1(java.lang.Long hoursin1) {
		this.hoursin1 = hoursin1;
	}

	public java.lang.Long getHoursin2() {
		return hoursin2;
	}

	public void setHoursin2(java.lang.Long hoursin2) {
		this.hoursin2 = hoursin2;
	}

	public java.lang.Long getHoursin3() {
		return hoursin3;
	}

	public void setHoursin3(java.lang.Long hoursin3) {
		this.hoursin3 = hoursin3;
	}

	public java.lang.Long getLatersecond() {
		return latersecond;
	}

	public void setLatersecond(java.lang.Long latersecond) {
		this.latersecond = latersecond;
	}

	public java.lang.Long getLatertime() {
		return latertime;
	}

	public void setLatertime(java.lang.Long latertime) {
		this.latertime = latertime;
	}

	public java.lang.Long getChecklost() {
		return checklost;
	}

	public void setChecklost(java.lang.Long checklost) {
		this.checklost = checklost;
	}

	public java.lang.Double getHoursout1() {
		return hoursout1;
	}

	public void setHoursout1(java.lang.Double hoursout1) {
		this.hoursout1 = hoursout1;
	}

	public java.lang.Double getHoursout2() {
		return hoursout2;
	}

	public void setHoursout2(java.lang.Double hoursout2) {
		this.hoursout2 = hoursout2;
	}

	public java.lang.Double getHoursout3() {
		return hoursout3;
	}

	public void setHoursout3(java.lang.Double hoursout3) {
		this.hoursout3 = hoursout3;
	}

	public java.lang.Double getDayout1() {
		return dayout1;
	}

	public void setDayout1(java.lang.Double dayout1) {
		this.dayout1 = dayout1;
	}

	public java.lang.Double getDayout2() {
		return dayout2;
	}

	public void setDayout2(java.lang.Double dayout2) {
		this.dayout2 = dayout2;
	}

	public java.lang.Double getFullprize() {
		return fullprize;
	}

	public void setFullprize(java.lang.Double fullprize) {
		this.fullprize = fullprize;
	}

	public java.lang.Double getHoursovertime() {
		return hoursovertime;
	}

	public void setHoursovertime(java.lang.Double hoursovertime) {
		this.hoursovertime = hoursovertime;
	}

	public java.lang.String getRemarks() {
		return remarks;
	}

	public void setRemarks(java.lang.String remarks) {
		this.remarks = remarks;
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

	public java.lang.Long getDays() {
		return days;
	}

	public void setDays(java.lang.Long days) {
		this.days = days;
	}
	
}
