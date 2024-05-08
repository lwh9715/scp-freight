package com.scp.dao.bus;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bus.BusOptrack;



/**
* 
 * @author neo
 */
@Component
public class BusOptrackDao extends BaseDaoImpl<BusOptrack, Long>{
	
	protected BusOptrackDao(Class<BusOptrack> persistancesClass) {
		super(persistancesClass);
	}

	public BusOptrackDao() {
		this(BusOptrack.class);
	}
}