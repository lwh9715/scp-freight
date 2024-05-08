package com.scp.service.bpm;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.bpm.BpmWorkItemDao;
import com.scp.model.bpm.BpmWorkItem;

@Component
@Lazy(true)
public class BpmWorkItemService {

	@Resource
	public BpmWorkItemDao bpmWorkItemDao;

	public void saveData(BpmWorkItem data) {
		if (0 == data.getId()) {
			bpmWorkItemDao.create(data);
		} else {
			bpmWorkItemDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BpmWorkItem data = bpmWorkItemDao.findById(id);
		bpmWorkItemDao.remove(data);
	}


}
