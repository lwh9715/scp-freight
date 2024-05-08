package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusShipContainer;



/**
* 
 * @author neo
 */
@Component
public class BusShipContainerDao extends BaseDaoImpl<BusShipContainer, Long>{
	
	protected BusShipContainerDao(Class<BusShipContainer> persistancesClass) {
		super(persistancesClass);
	}

	public BusShipContainerDao() {
		this(BusShipContainer.class);
	}
}