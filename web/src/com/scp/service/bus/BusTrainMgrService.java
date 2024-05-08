package com.scp.service.bus;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.bus.BusTrainDao;
import com.scp.model.bus.BusTrain;

@Component
public class BusTrainMgrService{
	
	@Resource
	public BusTrainDao busTrainDao; 
	

	public void saveData(BusTrain data) {
		if(0 == data.getId()){
			busTrainDao.create(data);
		}else{
			busTrainDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusTrain data = busTrainDao.findById(id);
		busTrainDao.remove(data);
	}


	public BusTrain findByjobId(Long jobid) {
		String sql = "isdelete = false AND  jobid ="+jobid;
		return this.busTrainDao.findOneRowByClauseWhere(sql);
	} 
	
	
}