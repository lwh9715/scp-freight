package com.scp.service.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.Datdoor2doordayDao;
import com.scp.model.data.Datdoor2doorday;

@Component
public class Door2doordayMgrService{
	
	@Resource
	public Datdoor2doordayDao datdoor2doordayDao; 

	public void saveData(Datdoor2doorday data) {
		if(0 == data.getId()){
			datdoor2doordayDao.create(data);
		}else{
			datdoor2doordayDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		Datdoor2doorday data = datdoor2doordayDao.findById(id);
		if(data == null)return;
		datdoor2doordayDao.remove(data);
	} 
	
}
