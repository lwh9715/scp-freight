package com.scp.dao.bus;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bus.BusAirExtfee;

/**
 * @author neo
 */
@Component
@Lazy(true)
public class BusAirExtfeeDao extends BaseDaoImpl<BusAirExtfee, Long>{
	
	protected BusAirExtfeeDao(Class<BusAirExtfee> persistancesClass) {
		super(persistancesClass);
	}

	public BusAirExtfeeDao() {
		this(BusAirExtfee.class);
	}
	
	
}