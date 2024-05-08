package com.scp.dao.price;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.price.BusPrice;

@Component
public class BusPriceDao extends BaseDaoImpl<BusPrice, Long>{

	protected BusPriceDao(Class<BusPrice> persistancesClass) {
		super(persistancesClass);
	}
	
	public BusPriceDao(){
		this(BusPrice.class);
	}
}