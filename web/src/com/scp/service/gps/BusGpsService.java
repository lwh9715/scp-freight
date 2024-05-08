package com.scp.service.gps;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.gps.BusGpsDao;
import com.scp.model.gps.BusGps;

@Component
@Lazy(true)
public class BusGpsService {

	@Resource
	public BusGpsDao busGpsDao;

	public void saveData(BusGps data) {
		if (0 == data.getId()) {
			busGpsDao.create(data);
		} else {
			busGpsDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusGps data = busGpsDao.findById(id);
		busGpsDao.remove(data);
	}

}
