package com.scp.model.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Table(name = "sys_timetask")
@SuppressWarnings("serial")
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true,dynamicUpdate=true)
@SequenceGenerator(name="seq_id",sequenceName="seq_id")
@GenericGenerator(name = "idStrategy", strategy = "native"
	, parameters = { @Parameter(name = "sequence", value = "seq_id") }) 
public class SysTimeTask implements java.io.Serializable {
	
	@Column(name = "id", nullable = false)
	@Id
	@GeneratedValue(generator="idStrategy") 
	private long id;
	
	@Column(name = "groupname")
	private String groupName; // 任务组
	
	@Column(name = "jobname")
	private String jobName;// 任务名称
	
	@Column(name = "jobstatus")
	private String jobStatus; // 任务发布状态
	
	@Column(name = "concurrent")
	private Boolean concurrent; // 
	
	@Column(name = "cron")
	private String cron;
	
	@Column(name = "jobtype")
	private String jobType; // 任务类型
	
	@Column(name = "commond")
	private String commond; // 任务
	
	@Column(name = "beanname")
	private String beanName;// 一个以所给名字注册的bean的实例
	
	@Column(name = "methodname")
	private String methodName;
	
	@Column(name = "jobdata")
	private String jobData; // 参数
	
	
	/**
	 *@generated
	 */
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

	
	@Column(name = "isdelete")
	private java.lang.Boolean isdelete;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public Boolean getConcurrent() {
		if(concurrent == null)concurrent=false;
		return concurrent;
	}

	public void setConcurrent(Boolean concurrent) {
		this.concurrent = concurrent;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getCommond() {
		return commond;
	}

	public void setCommond(String commond) {
		this.commond = commond;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getJobData() {
		return jobData;
	}

	public void setJobData(String jobData) {
		this.jobData = jobData;
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

	public java.lang.Boolean getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(java.lang.Boolean isdelete) {
		this.isdelete = isdelete;
	}
	
	
	
}
