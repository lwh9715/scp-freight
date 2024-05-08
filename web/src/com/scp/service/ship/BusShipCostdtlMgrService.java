package com.scp.service.ship;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.ship.BusShipCostdtlDao;
import com.scp.model.ship.BusShipCostdtl;
import com.scp.util.StrUtils;

@Component
public class BusShipCostdtlMgrService {

	

	@Resource
	public BusShipCostdtlDao busShipCostdtlDao;

	

	

	public void saveDtlData(BusShipCostdtl data) {
		if (0 == data.getId()) {
			busShipCostdtlDao.create(data);
		} else {
			busShipCostdtlDao.modify(data);
		}
	}
	
	public void removeDate(Long id,String usercode,Date updatetime) {
		String sql = "UPDATE bus_ship_costdtl SET isdelete = TRUE ,updater = '"+usercode+"',updatetime ="+updatetime+" WHERE id = "+id+";";
		busShipCostdtlDao.executeSQL(sql);
	}

	public void delBatch(String[] ids, String usercode, Date updatetime) {
		String idlist = StrUtils.array2List(ids);
		String sql = "UPDATE bus_ship_costdtl SET isdelete = TRUE ,updater = '"+usercode+"',updatetime = '"+updatetime+"' WHERE id in ( "+idlist+");";
		busShipCostdtlDao.executeSQL(sql);
		
	}
}