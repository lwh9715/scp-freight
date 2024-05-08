package com.scp.service.carmgr;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatCartypeDao;
import com.scp.model.data.DatCartype;

@Component
public class CarTypeMgrService{
	

	
	
	@Resource
	public DatCartypeDao datCartypeDao; 

	public void saveData(DatCartype data) {
		if(0 == data.getId()){
			datCartypeDao.create(data);
		}else{
			datCartypeDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		DatCartype data = datCartypeDao.findById(id);
		datCartypeDao.remove(data);
	} 
	
	
	
}