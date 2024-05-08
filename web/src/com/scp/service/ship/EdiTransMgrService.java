package com.scp.service.ship;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.ship.EdiTransDao;
import com.scp.model.ship.EdiTrans;

@Component
public class EdiTransMgrService {

	@Resource
	public EdiTransDao ediTransDao;
	
	public void saveData(EdiTrans data) {
		if(0 == data.getId()){
			ediTransDao.create(data);
		}else{
			ediTransDao.modify(data);
		}
	}
	
	public void removeData(Long id) {
		EdiTrans data = ediTransDao.findById(id);
		ediTransDao.remove(data);
	}
	
	public EdiTrans findByjobId(Long jobid) {
		String sql = "jobid ="+jobid;
		return this.ediTransDao.findOneRowByClauseWhere(sql);
	} 

}
