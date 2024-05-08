package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusShipHold;



/**
* 
 * @author neo
 */
@Component
public class BusShipHoldDao extends BaseDaoImpl<BusShipHold, Long>{
	
	protected BusShipHoldDao(Class<BusShipHold> persistancesClass) {
		super(persistancesClass);
	}

	public BusShipHoldDao() {
		this(BusShipHold.class);
	}
}