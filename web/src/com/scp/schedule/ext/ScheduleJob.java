package com.scp.schedule.ext;

/**
 * 
 * 自动升级
 * 
 */
public class ScheduleJob {

	public static final String CONCURRENT_IS = "1";

	public static final String CONCURRENT_NOT = "0";
	
	public enum JobStatus{Running,Stop,Pause};
	public enum JobType{SQL,Shell,JAVA};

	private Long jobId;
	private String jobGroup; // 任务组
	private String jobName;// 任务名称
	private String jobStatus; // 任务发布状态
	private String isConcurrent; // 运行状态
	private String cronExpression;
	
	private String jobType; // 任务类型
	private String jobCommond; // 任务
	private String beanClass;// 一个以所给名字注册的bean的实例
	private String methodName;
	private String jobData; // 参数

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
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

	public String getIsConcurrent() {
		return isConcurrent;
	}

	public void setIsConcurrent(String isConcurrent) {
		this.isConcurrent = isConcurrent;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(String beanClass) {
		this.beanClass = beanClass;
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


	public static String getConcurrentIs() {
		return CONCURRENT_IS;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getJobCommond() {
		return jobCommond;
	}

	public void setJobCommond(String jobCommond) {
		this.jobCommond = jobCommond;
	}

}