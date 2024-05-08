package com.scp.service.ship;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.ship.BusShipBillregDao;
import com.scp.model.ship.BusShipBillreg;
@Component
public class BusShipBillregMgrService {

	@Resource
	public BusShipBillregDao BusShipBillregDao;

	public void saveData(BusShipBillreg data) {
		if (0 == data.getId()) {
			BusShipBillregDao.create(data);
		} else {
			BusShipBillregDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusShipBillreg data = BusShipBillregDao.findById(id);
		BusShipBillregDao.remove(data);
	}

}
