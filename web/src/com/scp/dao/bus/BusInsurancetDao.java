package com.scp.dao.bus;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bus.BusInsurance;



@Component
public class BusInsurancetDao extends BaseDaoImpl<BusInsurance, Long>{
	
	protected BusInsurancetDao(Class<BusInsurance> persistancesClass) {
		super(persistancesClass);
	}

	public BusInsurancetDao() {
		this(BusInsurance.class);
	}
}