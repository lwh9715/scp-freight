package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusFreightCharge;



@Component
public class BusFreightChargeDao extends BaseDaoImpl<BusFreightCharge, Long>{
	
	protected BusFreightChargeDao(Class<BusFreightCharge> persistancesClass) {
		super(persistancesClass);
	}

	public BusFreightChargeDao() {
		this(BusFreightCharge.class);
	}
}