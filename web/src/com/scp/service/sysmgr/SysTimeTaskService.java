package com.scp.service.sysmgr;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.sys.SysTimeTaskDao;
import com.scp.dao.sys.SysTimeTaskLogDao;
import com.scp.model.sys.SysTimeTask;
import com.scp.model.sys.SysTimeTaskLog;
import com.scp.schedule.ext.ScheduleJob;

@Component
public class SysTimeTaskService{
	
	@Resource
	public SysTimeTaskDao sysTimeTaskDao;
	
	@Resource
	public SysTimeTaskLogDao sysTimeTaskLogDao;
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	public void saveDataTask(SysTimeTask data) {
		if(0 == data.getId()){
			sysTimeTaskDao.create(data);
		}else{
			sysTimeTaskDao.modify(data);
		}
	}

	public void removeDateTask(Long id) {
		SysTimeTask data = sysTimeTaskDao.findById(id);
		sysTimeTaskDao.remove(data);
	}

	public void saveDataTaskLog(SysTimeTaskLog data) {
		if(0 == data.getId()){
			sysTimeTaskLogDao.create(data);
		}else{
			sysTimeTaskLogDao.modify(data);
		}
	}

	public void removeDateTaskLog(Long id) {
		SysTimeTaskLog data = sysTimeTaskLogDao.findById(id);
		sysTimeTaskLogDao.remove(data);
	}

	public void stop(String[] ids) {
		for(String id : ids) {
			SysTimeTask data = sysTimeTaskDao.findById(Long.valueOf(id));
			data.setJobStatus(ScheduleJob.JobStatus.Stop.name());
			sysTimeTaskDao.modify(data);
		}
		
	}

	public void start(String[] ids) {
		for(String id : ids) {
			SysTimeTask data = sysTimeTaskDao.findById(Long.valueOf(id));
			data.setJobStatus(ScheduleJob.JobStatus.Running.name());
			sysTimeTaskDao.modify(data);
		}
	}

	public void delAllLogs(String timetaskid) {
		String sql = "DELETE FROM sys_timetask_log WHERE (timetaskid = " + timetaskid + " OR -1 = " + timetaskid + ") ;";
		sysTimeTaskDao.executeSQL(sql);
	}
}
