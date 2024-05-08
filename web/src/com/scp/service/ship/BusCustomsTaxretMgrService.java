package com.scp.service.ship;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.ship.BusCustomsTaxretDao;
import com.scp.model.ship.BusCustomsTaxret;

@Component
public class BusCustomsTaxretMgrService {

	@Resource
	public BusCustomsTaxretDao busCustomsTaxretDao;
	
	public void saveData(BusCustomsTaxret data) {
		if(0 == data.getId()){
			busCustomsTaxretDao.create(data);
		}else{
			busCustomsTaxretDao.modify(data);
		}
	}
	
	public void removeData(Long id) {
		BusCustomsTaxret data = busCustomsTaxretDao.findById(id);
		busCustomsTaxretDao.remove(data);
	}

}
