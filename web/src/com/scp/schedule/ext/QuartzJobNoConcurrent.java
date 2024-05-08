package com.scp.schedule.ext;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Description: 若上次执行未完成，下次开始后等待上一个任务执行完才执行(xml 配置方式中是：<property name="concurrent" value="false"/>)
 *
 */
public class QuartzJobNoConcurrent implements org.quartz.Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
		if(ScheduleJob.JobType.SQL.name().equals(scheduleJob.getJobType())){
			TaskExecTools.execQuery(scheduleJob);
		}else if(ScheduleJob.JobType.Shell.name().equals(scheduleJob.getJobType())){
			TaskExecTools.execShell(scheduleJob);
		}else if(ScheduleJob.JobType.JAVA.name().equals(scheduleJob.getJobType())){
			TaskExecTools.execJavaMethod(scheduleJob);
		}else{
			
		}
	}
}
