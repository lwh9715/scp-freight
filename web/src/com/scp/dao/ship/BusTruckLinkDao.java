package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusTruckLink;



/**
* 
 * @author neo
 */
@Component
public class BusTruckLinkDao extends BaseDaoImpl<BusTruckLink, Long>{
	
	protected BusTruckLinkDao(Class<BusTruckLink> persistancesClass) {
		super(persistancesClass);
	}

	public BusTruckLinkDao() {
		this(BusTruckLink.class);
	}
}