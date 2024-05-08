package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusShipCost;



/**
* 
 * @author neo
 */
@Component
public class BusShipCostDao extends BaseDaoImpl<BusShipCost, Long>{
	
	protected BusShipCostDao(Class<BusShipCost> persistancesClass) {
		super(persistancesClass);
	}

	public BusShipCostDao() {
		this(BusShipCost.class);
	}
}