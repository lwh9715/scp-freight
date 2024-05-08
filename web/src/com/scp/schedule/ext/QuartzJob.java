package com.scp.schedule.ext;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Description: 计划任务执行处 无状态 , 如果上一个任务没有执行完，当前任务继续执行
 *
 */
public class QuartzJob implements org.quartz.Job {

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
