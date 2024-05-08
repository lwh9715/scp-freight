package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.MgrTrainBooking;

@Component
public class MgrTrainBookingDao extends BaseDaoImpl<MgrTrainBooking, Long>{
	
	protected MgrTrainBookingDao(Class<MgrTrainBooking> persistancesClass) {
		super(persistancesClass);
	}

	public MgrTrainBookingDao() {
		this(MgrTrainBooking.class);
	}
}
