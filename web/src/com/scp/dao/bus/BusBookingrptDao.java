package com.scp.dao.bus;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bus.BusBookingrpt;

@Component
public class BusBookingrptDao extends BaseDaoImpl<BusBookingrpt, Long>{
	
	protected BusBookingrptDao(Class<BusBookingrpt> persistancesClass) {
		super(persistancesClass);
	}

	public BusBookingrptDao() {
		this(BusBookingrpt.class);
	}
}
