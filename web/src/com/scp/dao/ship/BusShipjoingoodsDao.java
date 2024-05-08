package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusShipjoingoods;



/**
* 
 * @author neo
 */
@Component
public class BusShipjoingoodsDao extends BaseDaoImpl<BusShipjoingoods, Long>{
	
	protected BusShipjoingoodsDao(Class<BusShipjoingoods> persistancesClass) {
		super(persistancesClass);
	}

	public BusShipjoingoodsDao() {
		this(BusShipjoingoods.class);
	}
}