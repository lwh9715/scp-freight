package com.scp.service.data;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatUnitDao;
import com.scp.model.data.DatUnit;

@Component
public class UnitMgrService{
	
	@Resource
	public DatUnitDao datUnitDao; 

	public void saveData(DatUnit data) {
		if(0 == data.getId()){
			datUnitDao.create(data);
		}else{
			datUnitDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		DatUnit data = datUnitDao.findById(id);
		datUnitDao.remove(data);
	} 
}