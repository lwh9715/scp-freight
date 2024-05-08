package com.scp.dao.gps;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.gps.BusGpsRef;



/**
* 
 * @author sunny
 */
@Component
public class BusGpsRefDao extends BaseDaoImpl<BusGpsRef, Long>{
	
	protected BusGpsRefDao(Class<BusGpsRef> persistancesClass) {
		super(persistancesClass);
	}

	public BusGpsRefDao() {
		this(BusGpsRef.class);
	}
}