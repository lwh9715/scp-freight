package com.scp.dao.bus;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bus.BusAir;

/**
 * @author neo
 */
@Component
@Lazy(true)
public class BusAirDao extends BaseDaoImpl<BusAir, Long>{
	
	protected BusAirDao(Class<BusAir> persistancesClass) {
		super(persistancesClass);
	}

	public BusAirDao() {
		this(BusAir.class);
	}
	
	
}