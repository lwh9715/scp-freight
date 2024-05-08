package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusShipAddres;



/**
* 
 * @author neo
 */
@Component
public class BusShipAddresDao extends BaseDaoImpl<BusShipAddres, Long>{
	
	protected BusShipAddresDao(Class<BusShipAddres> persistancesClass) {
		super(persistancesClass);
	}

	public BusShipAddresDao() {
		this(BusShipAddres.class);
	}
}