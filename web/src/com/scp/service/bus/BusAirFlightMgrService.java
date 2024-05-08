package com.scp.service.bus;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.bus.BusAirFlightDao;
import com.scp.model.bus.BusAirFlight;
import com.scp.util.StrUtils;

@Component
@Lazy(true)
public class BusAirFlightMgrService {

	@Resource
	public BusAirFlightDao busAirFlightDao;

	public void saveData(BusAirFlight data) {
		if (0 == data.getId()) {
			busAirFlightDao.create(data);
		} else {
			busAirFlightDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusAirFlight data = busAirFlightDao.findById(id);
		busAirFlightDao.remove(data);
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @param usercode
	 */
	public void removeDate(String[] ids,String usercode) {
		String sql = "UPDATE bus_airflight SET isdelete = TRUE ,updater = '"+usercode+"',updatetime = NOW() WHERE id IN ("+StrUtils.array2List(ids)+");";
		busAirFlightDao.executeSQL(sql);
	}
	
}