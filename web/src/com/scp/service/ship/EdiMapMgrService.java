package com.scp.service.ship;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.ship.EdiMapDao;
import com.scp.model.ship.EdiMap;

@Component
public class EdiMapMgrService {

	@Resource
	public EdiMapDao ediMapDao;
	
	public void saveData(EdiMap data) {
		if(0 == data.getId()){
			ediMapDao.create(data);
		}else{
			ediMapDao.modify(data);
		}
	}
	
	public void removeData(Long id) {
		EdiMap data = ediMapDao.findById(id);
		ediMapDao.remove(data);
	}

}
