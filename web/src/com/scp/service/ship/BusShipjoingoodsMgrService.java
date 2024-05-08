package com.scp.service.ship;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.ship.BusShipjoingoodsDao;
import com.scp.model.ship.BusShipjoingoods;

@Component
public class BusShipjoingoodsMgrService{
	

	
	
	@Resource
	public BusShipjoingoodsDao busShipjoingoodsDao; 
	

	public void saveData(BusShipjoingoods data) {
		if(0 == data.getId()){
			busShipjoingoodsDao.create(data);
		}else{
			busShipjoingoodsDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusShipjoingoods data = busShipjoingoodsDao.findById(id);
		busShipjoingoodsDao.remove(data);
	}

//	public BusShipping findByjobId(Long jobid) {
//		String sql = "SELECT * FROM bus_shipping WHERE isdelete = false AND  jobid ="+jobid;
//		
//		return null;
//	} 
//	
	
	
}