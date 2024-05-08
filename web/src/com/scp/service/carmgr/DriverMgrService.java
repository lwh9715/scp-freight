package com.scp.service.carmgr;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatDriverDao;
import com.scp.model.data.DatDriver;

@Component
public class DriverMgrService{
	

	
	
	@Resource
	public DatDriverDao datDriverDao; 

	public void saveData(DatDriver data) {
		if(0 == data.getId()){
			datDriverDao.create(data);
		}else{
			datDriverDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		DatDriver data = datDriverDao.findById(id);
		datDriverDao.remove(data);
	} 
	
	
	
}