package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusShipjoinlink;



/**
* 
 * @author neo
 */
@Component
public class BusShipjoinlinkDao extends BaseDaoImpl<BusShipjoinlink, Long>{
	
	protected BusShipjoinlinkDao(Class<BusShipjoinlink> persistancesClass) {
		super(persistancesClass);
	}

	public BusShipjoinlinkDao() {
		this(BusShipjoinlink.class);
	}
}