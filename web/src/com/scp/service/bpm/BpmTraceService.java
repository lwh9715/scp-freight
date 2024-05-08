package com.scp.service.bpm;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.bpm.BpmTraceDao;
import com.scp.model.bpm.BpmTrace;

@Component
@Lazy(true)
public class BpmTraceService {

	@Resource
	public BpmTraceDao bpmTraceDao;

	public void saveData(BpmTrace data) {
		if (0 == data.getId()) {
			bpmTraceDao.create(data);
		} else {
			bpmTraceDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BpmTrace data = bpmTraceDao.findById(id);
		bpmTraceDao.remove(data);
	}


}
