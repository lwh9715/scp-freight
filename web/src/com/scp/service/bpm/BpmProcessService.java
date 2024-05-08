package com.scp.service.bpm;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.bpm.BpmProcessDao;
import com.scp.exception.NoRowException;
import com.scp.model.bpm.BpmProcess;

@Component
@Lazy(true)
public class BpmProcessService {

	@Resource
	public BpmProcessDao bpmProcessDao;

	public void saveData(BpmProcess data) {
		if (0 == data.getId()) {
			bpmProcessDao.create(data);
		} else {
			bpmProcessDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BpmProcess data = bpmProcessDao.findById(id);
		bpmProcessDao.remove(data);
	}

	
	public BpmProcess findBpmProcessByCode(String code){
		BpmProcess bpmProcess = null;
		try {
			bpmProcess = this.bpmProcessDao.findOneRowByClauseWhere(" code = '"+code+"'");
		}catch (NoRowException e) {
			bpmProcess = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bpmProcess;
	}

}
