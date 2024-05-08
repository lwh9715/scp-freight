package com.scp.service.gps;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.gps.BusGpsRefDao;
import com.scp.model.gps.BusGpsRef;

@Component
@Lazy(true)
public class BusGpsRefService {

	@Resource
	public BusGpsRefDao busGpsRefDao;

	public void saveData(BusGpsRef data) {
		if (0 == data.getId()) {
			busGpsRefDao.create(data);
		} else {
			busGpsRefDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusGpsRef data = busGpsRefDao.findById(id);
		busGpsRefDao.remove(data);
	}

}
