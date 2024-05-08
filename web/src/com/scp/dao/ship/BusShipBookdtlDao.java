package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusShipBookdtl;



/**
* 
 * @author neo
 */
@Component
public class BusShipBookdtlDao extends BaseDaoImpl<BusShipBookdtl, Long>{
	
	protected BusShipBookdtlDao(Class<BusShipBookdtl> persistancesClass) {
		super(persistancesClass);
	}

	public BusShipBookdtlDao() {
		this(BusShipBookdtl.class);
	}
}