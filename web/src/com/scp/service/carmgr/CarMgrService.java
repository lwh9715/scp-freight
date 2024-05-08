package com.scp.service.carmgr;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatCarDao;
import com.scp.model.data.DatCar;

@Component
public class CarMgrService{
	

	
	
	@Resource
	public DatCarDao datCarDao; 

	public void saveData(DatCar data) {
		if(0 == data.getId()){
			datCarDao.create(data);
		}else{
			datCarDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		DatCar data = datCarDao.findById(id);
		datCarDao.remove(data);
	} 
	
	
	
}