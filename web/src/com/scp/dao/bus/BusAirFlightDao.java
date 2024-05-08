package com.scp.dao.bus;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bus.BusAirFlight;

/**
 * @author neo
 */
@Component
@Lazy(true)
public class BusAirFlightDao extends BaseDaoImpl<BusAirFlight, Long>{
	
	protected BusAirFlightDao(Class<BusAirFlight> persistancesClass) {
		super(persistancesClass);
	}

	public BusAirFlightDao() {
		this(BusAirFlight.class);
	}
	
	
}