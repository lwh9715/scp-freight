package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusShipping;



/**
* 
 * @author neo
 */
@Component
public class BusShippingDao extends BaseDaoImpl<BusShipping, Long>{
	
	protected BusShippingDao(Class<BusShipping> persistancesClass) {
		super(persistancesClass);
	}

	public BusShippingDao() {
		this(BusShipping.class);
	}

	
}