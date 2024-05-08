package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusShipjoin;



/**
* 
 * @author neo
 */
@Component
public class BusShipjoinDao extends BaseDaoImpl<BusShipjoin, Long>{
	
	protected BusShipjoinDao(Class<BusShipjoin> persistancesClass) {
		super(persistancesClass);
	}

	public BusShipjoinDao() {
		this(BusShipjoin.class);
	}
}