package com.scp.service.ship;


import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.ship.MgrTrainBookingDao;
import com.scp.model.ship.MgrTrainBooking;
import com.scp.util.StrUtils;

@Component
public class MgrTrainBookingService {

	@Resource
	public MgrTrainBookingDao mgrTrainBookingDao;

	public void saveData(MgrTrainBooking data) {
		if(0 == data.getId()){
			mgrTrainBookingDao.create(data);
		}else{
			mgrTrainBookingDao.modify(data);
		}
	}

	public void removeDate(String[] ids, String usercode) {
		String sql = "UPDATE bus_booking_train SET isdelete = TRUE ,updater = '"+usercode+"',updatetime = NOW() WHERE id IN ("+StrUtils.array2List(ids)+");";
		mgrTrainBookingDao.executeSQL(sql);
	}

}
