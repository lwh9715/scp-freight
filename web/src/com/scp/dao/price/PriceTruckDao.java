package com.scp.dao.price;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.price.PriceTruck;

@Component
public class PriceTruckDao extends BaseDaoImpl<PriceTruck, Long>{

	protected PriceTruckDao(Class<PriceTruck> persistancesClass) {
		super(persistancesClass);
	}
	
	public PriceTruckDao(){
		this(PriceTruck.class);
	}
}