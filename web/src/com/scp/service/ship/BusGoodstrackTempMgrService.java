package com.scp.service.ship;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.ship.BusGoodstrackTempDao;
import com.scp.model.ship.BusGoodstrackTemp;
@Component
public class BusGoodstrackTempMgrService {

	@Resource
	public BusGoodstrackTempDao BusGoodstrackTempDao;

	public void saveData(BusGoodstrackTemp data) {
		if (0 == data.getId()) {
			BusGoodstrackTempDao.create(data);
		} else {
			BusGoodstrackTempDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusGoodstrackTemp data = BusGoodstrackTempDao.findById(id);
		BusGoodstrackTempDao.remove(data);
	}
}
