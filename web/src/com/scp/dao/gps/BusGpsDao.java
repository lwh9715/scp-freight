package com.scp.dao.gps;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.gps.BusGps;



/**
* 
 * @author sunny
 */
@Component
public class BusGpsDao extends BaseDaoImpl<BusGps, Long>{
	
	protected BusGpsDao(Class<BusGps> persistancesClass) {
		super(persistancesClass);
	}

	public BusGpsDao() {
		this(BusGps.class);
	}
}