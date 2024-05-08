package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusShipBooking;



/**
* 
 * @author neo
 */
@Component
public class BusShipBookingDao extends BaseDaoImpl<BusShipBooking, Long>{
	
	protected BusShipBookingDao(Class<BusShipBooking> persistancesClass) {
		super(persistancesClass);
	}

	public BusShipBookingDao() {
		this(BusShipBooking.class);
	}
}