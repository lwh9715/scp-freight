package com.scp.dao.bus;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bus.BusAirBill;

/**
 * @author neo
 */
@Component
@Lazy(true)
public class BusAirBillDao extends BaseDaoImpl<BusAirBill, Long>{
	
	protected BusAirBillDao(Class<BusAirBill> persistancesClass) {
		super(persistancesClass);
	}

	public BusAirBillDao() {
		this(BusAirBill.class);
	}
	
	
}