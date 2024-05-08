package com.scp.service.ship;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.ship.BusFreightChargeDao;
import com.scp.model.ship.BusFreightCharge;

@Component
public class BusCreightChargeMgrService {

	@Resource
	public BusFreightChargeDao busFreightChargeDao;
	
	public void saveData(BusFreightCharge data) {
		if(0 == data.getId()){
			busFreightChargeDao.create(data);
		}else{
			busFreightChargeDao.modify(data);
		}
	}
	
	public void removeData(Long id) {
		BusFreightCharge data = busFreightChargeDao.findById(id);
		busFreightChargeDao.remove(data);
	}

}
