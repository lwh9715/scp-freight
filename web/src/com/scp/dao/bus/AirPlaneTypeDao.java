package com.scp.dao.bus;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bus.AirPlaneType;

/**
 * @author neo
 */
@Component
@Lazy(true)
public class AirPlaneTypeDao extends BaseDaoImpl<AirPlaneType, Long>{
	
	protected AirPlaneTypeDao(Class<AirPlaneType> persistancesClass) {
		super(persistancesClass);
	}

	public AirPlaneTypeDao() {
		this(AirPlaneType.class);
	}
	
	
}