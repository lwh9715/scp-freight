package com.scp.service.bpm;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.bpm.BpmAssignRefDao;
import com.scp.model.bpm.BpmAssignRef;
import com.scp.util.StrUtils;

@Component
@Lazy(true)
public class BpmAssignRefService {

	@Resource
	public BpmAssignRefDao bpmAssignRefDao;

	public void saveData(BpmAssignRef data) {
		if (0 == data.getId()) {
			bpmAssignRefDao.create(data);
		} else {
			bpmAssignRefDao.modify(data);
		}
	}

	public void remove(String[] ids) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			String sql = "\nDELETE FROM bpm_assign_ref WHERE id="+id+";";
			stringBuilder.append(sql);
		}
		bpmAssignRefDao.executeSQL(stringBuilder.toString());
	}

	public void update(String[] ids, String expression) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			String sql = "\nUPDATE bpm_assign_ref SET expression = '"+StrUtils.getSqlFormat(expression)+"' WHERE id="+id+";";
			stringBuilder.append(sql);
		}
		bpmAssignRefDao.executeSQL(stringBuilder.toString());
	}

	public void addAssignUser(String assignid , String[] ids) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			String sql = "\nINSERT INTO bpm_assign_ref(id,assignid,userid) VALUES(getid(),"+assignid+","+id+");";
			stringBuilder.append(sql);
		}
		bpmAssignRefDao.executeSQL(stringBuilder.toString());
	}
}
