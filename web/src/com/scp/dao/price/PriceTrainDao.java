package com.scp.dao.price;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.price.PriceTrain;

@Component
public class PriceTrainDao extends BaseDaoImpl<PriceTrain, Long>{

	protected PriceTrainDao(Class<PriceTrain> persistancesClass) {
		super(persistancesClass);
	}
	
	public PriceTrainDao(){
		this(PriceTrain.class);
	}
}