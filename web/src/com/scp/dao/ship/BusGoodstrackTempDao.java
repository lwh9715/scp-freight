package com.scp.dao.ship;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusGoodstrackTemp;



@Component
public class BusGoodstrackTempDao extends BaseDaoImpl<BusGoodstrackTemp, Long>{
	
	protected BusGoodstrackTempDao(Class<BusGoodstrackTemp> persistancesClass) {
		super(persistancesClass);
	}

	public BusGoodstrackTempDao() {
		this(BusGoodstrackTemp.class);
	}

	
}
