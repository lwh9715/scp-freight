package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusTruck;



/**
* 
 * @author neo
 */
@Component
public class BusTruckDao extends BaseDaoImpl<BusTruck, Long>{
	
	protected BusTruckDao(Class<BusTruck> persistancesClass) {
		super(persistancesClass);
	}

	public BusTruckDao() {
		this(BusTruck.class);
	}
}