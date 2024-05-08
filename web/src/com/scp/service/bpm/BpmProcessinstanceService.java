package com.scp.service.bpm;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.bpm.BpmProcessinstanceDao;
import com.scp.model.bpm.BpmProcessinstance;

@Component
@Lazy(true)
public class BpmProcessinstanceService {

	@Resource
	public BpmProcessinstanceDao bpmProcessinstanceDao;

	public void saveData(BpmProcessinstance data) {
		if (0 == data.getId()) {
			bpmProcessinstanceDao.create(data);
		} else {
			bpmProcessinstanceDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BpmProcessinstance data = bpmProcessinstanceDao.findById(id);
		bpmProcessinstanceDao.remove(data);
	}


}
