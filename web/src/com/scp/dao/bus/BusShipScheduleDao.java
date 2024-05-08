package com.scp.dao.bus;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bus.BusShipSchedule;



/**
* 
 * @author neo
 */
@Component
public class BusShipScheduleDao extends BaseDaoImpl<BusShipSchedule, Long>{
	
	protected BusShipScheduleDao(Class<BusShipSchedule> persistancesClass) {
		super(persistancesClass);
	}

	public BusShipScheduleDao() {
		this(BusShipSchedule.class);
	}
}