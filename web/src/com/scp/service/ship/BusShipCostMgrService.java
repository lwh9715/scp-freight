package com.scp.service.ship;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.ship.BusShipCostDao;
import com.scp.model.ship.BusShipCost;

@Component
public class BusShipCostMgrService {

	@Resource
	public BusShipCostDao busShipCostDao;


	public void saveMasterData(BusShipCost data) {
		if (0 == data.getId()) {
			busShipCostDao.create(data);
		} else {
			busShipCostDao.modify(data);
		}
	}

	

	public void removeDate(Long id,String usercode,Date updatetime) {
		String sql = "UPDATE bus_ship_cost SET isdelete = TRUE ,updater = '"+usercode+"',updatetime = '"+updatetime+"'WHERE id = "+id+";";
		sql+="UPDATE bus_ship_costdtl SET isdelete = TRUE ,updater = '"+usercode+"',updatetime = '"+updatetime+"' WHERE costid = "+id+";";
		busShipCostDao.executeSQL(sql);
	}
}