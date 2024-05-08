package com.scp.dao.price;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.price.PriceAir;

@Component
public class PriceAirDao extends BaseDaoImpl<PriceAir, Long>{

	protected PriceAirDao(Class<PriceAir> persistancesClass) {
		super(persistancesClass);
	}
	
	public PriceAirDao(){
		this(PriceAir.class);
	}
}