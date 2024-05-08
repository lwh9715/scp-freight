package com.scp.schedule.ext;

import java.util.ArrayList;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.scp.model.sys.SysTimeTask;
import com.scp.service.sysmgr.SysTimeTaskService;
import com.scp.util.AppUtils;

public class ScheduleMgr implements ApplicationContextAware {

	private final Logger logger = LoggerFactory.getLogger(ScheduleMgr.class);

	// public static org.springframework.scheduling.quartz.SchedulerFactoryBean
	// schedulerFactoryBean = null;

	private ApplicationContext appCtx;

	public org.quartz.impl.StdScheduler stdScheduler = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		if (appCtx == null) {
			appCtx = applicationContext;
		}
	}
	
	
	public void init() {
		if (appCtx == null) {
			appCtx = new ClassPathXmlApplicationContext("applicationContext.xml");
		}
		stdScheduler = (StdScheduler) appCtx.getBean("schedulerFactoryBean");
		// Scheduler scheduler = schedulerFactoryBean.getScheduler();
		try {
			logger.info(stdScheduler.getSchedulerName());
			/*addJob();
			
			addJob();
			addJob();
			addJob();
			addJob();
			addJob();*/
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

//	private List<SysTimeTask> getJobsFromDB() {
//		List<SysTimeTask> list = new ArrayList<SysTimeTask>();
//		SysTimeTask tt = new SysTimeTask();
//		tt.setJobName("test1");
//		tt.setConcurrent(false);
//		tt.setMethodName("action");
//		tt.setJobStatus("1");
//		tt.setCron("*/1 * * * * ?");
//		tt.setId(1l);
//		tt.setJobType("SQL");
//		tt.setCommond("SELECT * FROM f_fixbug('type=1')");
//		list.add(tt);
//
//		tt = new SysTimeTask();
//		tt.setJobName("test2");
//		tt.setConcurrent(false);
//		tt.setMethodName("action");
//		tt.setJobStatus("1");
//		tt.setCron("*/2 * * * * ?");
//		tt.setId(1l);
//		tt.setJobType("Shell");
//		tt.setCommond("ls -ll");
//		list.add(tt);
//
//		tt = new SysTimeTask();
//		tt.setJobName("test3");
//		tt.setConcurrent(false);
//		tt.setMethodName("action");
//		tt.setJobStatus("1");
//		tt.setCron("*/10 * * * * ?");
//		tt.setId(1l);
//		tt.setJobType("SQL");
//		tt.setCommond("SELECT * FROM f_fixcommon('type=1')");
//		list.add(tt);
//		return list;
//	}
//	
	
	private List<SysTimeTask> getJobsFromDB() {
		SysTimeTaskService sysTimeTaskService = (SysTimeTaskService)AppUtils.getBeanFromSpringIoc("sysTimeTaskService");
		List<SysTimeTask> list = sysTimeTaskService.sysTimeTaskDao.findAllByClauseWhere("1=1");
		return list;
	}
	
	
	public void addJob() throws SchedulerException {
		// 这里从数据库中获取任务信息数据
		List<SysTimeTask> list = getJobsFromDB();

		List<ScheduleJob> jobList = new ArrayList<ScheduleJob>();
		for (SysTimeTask SysTimeTask : list) {
			ScheduleJob job1 = new ScheduleJob();
			job1.setJobId(SysTimeTask.getId());
			job1.setJobGroup(SysTimeTask.getGroupName()); // 任务组
			job1.setJobName(SysTimeTask.getJobName());// 任务名称
			job1.setJobStatus(SysTimeTask.getJobStatus()); // 任务发布状态
			job1.setIsConcurrent(SysTimeTask.getConcurrent() ? "1" : "0"); // 运行状态
			job1.setCronExpression(SysTimeTask.getCron());
			job1.setBeanClass(SysTimeTask.getBeanName());// 一个以所给名字注册的bean的实例
			job1.setMethodName(SysTimeTask.getMethodName());
			job1.setJobType(SysTimeTask.getJobType());
			job1.setJobCommond(SysTimeTask.getCommond());
			job1.setJobData(SysTimeTask.getJobData()); // 参数
			jobList.add(job1);
		}

		for (ScheduleJob job : jobList) {
			try {
				addJob(job);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 添加任务
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	private void addJob(ScheduleJob job) throws SchedulerException {
		if (job == null ) {
			return;
		}
		// Scheduler scheduler = schedulerFactoryBean.getScheduler();
		logger.debug(stdScheduler + "...........................................add");
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());

		CronTrigger trigger = (CronTrigger) stdScheduler.getTrigger(triggerKey);
		
		//Neo 20191011 先移除，如果改了commond ，需要移除，否则还是之前加入的原始类
		if (null != trigger) {
			stdScheduler.deleteJob(trigger.getJobKey());
		}
		
		if (!job.getJobStatus().equals(ScheduleJob.JobStatus.Running.name()) ) {
			return;
		}
		
		//Neo 20191011  如果存在， 上面先移除，再创建
		Class clazz = ScheduleJob.CONCURRENT_IS.equals(job.getIsConcurrent()) ? QuartzJob.class : QuartzJobNoConcurrent.class;
		JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(
				job.getJobName(), job.getJobGroup()).usingJobData("data",
				job.getJobData()).build();
		jobDetail.getJobDataMap().put("scheduleJob", job);
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
				.cronSchedule(job.getCronExpression());

		trigger = TriggerBuilder.newTrigger().withDescription(
				job.getJobId().toString()).withIdentity(job.getJobName(),
				job.getJobGroup()).withSchedule(scheduleBuilder).build();

		stdScheduler.scheduleJob(jobDetail, trigger);
		
		//

//		// 不存在，创建一个
//		if (null == trigger) {
//			Class clazz = ScheduleJob.CONCURRENT_IS.equals(job.getIsConcurrent()) ? QuartzJob.class : QuartzJobNoConcurrent.class;
//			JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(
//					job.getJobName(), job.getJobGroup()).usingJobData("data",
//					job.getJobData()).build();
//			jobDetail.getJobDataMap().put("scheduleJob", job);
//			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
//					.cronSchedule(job.getCronExpression());
//
//			trigger = TriggerBuilder.newTrigger().withDescription(
//					job.getJobId().toString()).withIdentity(job.getJobName(),
//					job.getJobGroup()).withSchedule(scheduleBuilder).build();
//
//			stdScheduler.scheduleJob(jobDetail, trigger);
//		} else {
//			//stdScheduler.getJobDetail(trigger.getJobKey()).getJobDataMap().put("scheduleJob", job);
//			// Trigger已存在，那么更新相应的定时设置
//			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
//					.cronSchedule(job.getCronExpression());
//			// 按新的cronExpression表达式重新构建trigger
//			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
//					.usingJobData("data", job.getJobData()).withSchedule(
//							scheduleBuilder).build();
//			// 按新的trigger重新设置job执行
//			stdScheduler.rescheduleJob(triggerKey, trigger);
//		}
	}
}
