package com.scp.service.ship;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.ship.BusShipHoldDao;
import com.scp.model.ship.BusShipHold;
@Component
public class BusShipHoldMgrService {

	@Resource
	public BusShipHoldDao busShipHoldDao;

	public void saveData(BusShipHold data) {
		if (0 == data.getId()) {
			busShipHoldDao.create(data);
		} else {
			busShipHoldDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusShipHold data = busShipHoldDao.findById(id);
		busShipHoldDao.remove(data);
	}
}
