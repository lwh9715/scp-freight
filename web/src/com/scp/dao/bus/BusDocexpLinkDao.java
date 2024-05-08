package com.scp.dao.bus;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bus.BusDocexpLink;



/**
* 
 * @author neo
 */
@Component
public class BusDocexpLinkDao extends BaseDaoImpl<BusDocexpLink, Long>{
	
	protected BusDocexpLinkDao(Class<BusDocexpLink> persistancesClass) {
		super(persistancesClass);
	}

	public BusDocexpLinkDao() {
		this(BusDocexpLink.class);
	}
}