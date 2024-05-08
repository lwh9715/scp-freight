package com.scp.service.bpm;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.bpm.BpmTaskDao;
import com.scp.model.bpm.BpmTask;
import com.scp.util.AppUtils;
import com.scp.util.MessageUtils;

@Component
@Lazy(true)
public class BpmTaskService {

	@Resource
	public BpmTaskDao bpmTaskDao;

	public void saveData(BpmTask data) {
		if (0 == data.getId()) {
			bpmTaskDao.create(data);
		} else {
			bpmTaskDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BpmTask data = bpmTaskDao.findById(id);
		bpmTaskDao.remove(data);
	}

	public void process(String type,String ids[]){
		try {
			for(String id : ids) {
				BpmTask bpmTask = bpmTaskDao.findById(Long.parseLong(id));
				String sql = "SELECT f_bpm_processins_mgr('id="+bpmTask.getProcessinstanceid()+";type="+type+";usercode="+AppUtils.getUserSession().getUsercode()+"');";
				bpmTaskDao.executeQuery(sql);
			}
		}catch(Exception e){
			MessageUtils.showException(e);
			return;
		}
	}
	
	public void readed(String[] ids) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE bpm_task SET status = 'R' WHERE status = 'N' and id = " + id + ";";
			stringBuffer.append(sql);
		}
		bpmTaskDao.executeSQL(stringBuffer.toString());
	}
	
	public void unread(String[] ids) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE bpm_task SET status = 'N' WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		bpmTaskDao.executeSQL(stringBuffer.toString());
	}
	
	public void markwait(String[] ids) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE bpm_task SET status = 'W' WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		bpmTaskDao.executeSQL(stringBuffer.toString());
	}
	
	public void readed2(String[] ids) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE bpm_task SET status = 'R' WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		bpmTaskDao.executeSQL(stringBuffer.toString());
	}

}
