package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusShipCostdtl;



/**
* 
 * @author neo
 */
@Component
public class BusShipCostdtlDao extends BaseDaoImpl<BusShipCostdtl, Long>{
	
	protected BusShipCostdtlDao(Class<BusShipCostdtl> persistancesClass) {
		super(persistancesClass);
	}

	public BusShipCostdtlDao() {
		this(BusShipCostdtl.class);
	}
}