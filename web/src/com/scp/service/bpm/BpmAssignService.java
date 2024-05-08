package com.scp.service.bpm;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.bpm.BpmAssignDao;
import com.scp.model.bpm.BpmAssign;
import com.scp.util.AppUtils;

@Component
@Lazy(true)
public class BpmAssignService {

	@Resource
	public BpmAssignDao bpmAssignDao;

	public void saveData(BpmAssign data) {
		if (0 == data.getId()) {
			bpmAssignDao.create(data);
		} else {
			bpmAssignDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BpmAssign data = bpmAssignDao.findById(id);
		bpmAssignDao.remove(data);
	}

	public void removeDate(String[] ids) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			stringBuilder.append("\nUPDATE bpm_assign SET isdelete = TRUE, updater = '"+AppUtils.getUserSession().getUsercode()
					+"',updatetime = NOW() WHERE id = " + id + ";");
		}
		bpmAssignDao.executeSQL(stringBuilder.toString());
	} 
	
	public void addcopy(String[] ids) {
		for (String id : ids) {
			BpmAssign assign = bpmAssignDao.findById(Long.parseLong(id));
			BpmAssign bpmAssign = new BpmAssign();
			bpmAssign.setFormview(assign.getFormview());
			bpmAssign.setIs2submit(assign.getIs2submit());
			bpmAssign.setProcess_id(assign.getProcess_id());
			bpmAssign.setStep(assign.getStep());
			bpmAssign.setTaskId(assign.getTaskId());
			bpmAssign.setTaskname(assign.getTaskname());
			bpmAssign.setTousercode(assign.getTousercode());
			bpmAssign.setUrl(assign.getUrl());
			bpmAssign.setUserid(assign.getUserid());
			bpmAssignDao.create(bpmAssign);
		}
	} 
	
	
	public void updateBatch(String[] ids, String column,
			String value, String usercode) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE bpm_assign SET " + column + " = '" + value.trim() + "' , updater = '"+usercode+"' , updatetime = NOW() WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		bpmAssignDao.executeSQL(stringBuffer.toString());
	}

}
