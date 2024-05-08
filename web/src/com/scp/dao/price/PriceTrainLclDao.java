package com.scp.dao.price;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.price.PriceTrainLcl;

@Component
public class PriceTrainLclDao extends BaseDaoImpl<PriceTrainLcl, Long>{

	protected PriceTrainLclDao(Class<PriceTrainLcl> persistancesClass) {
		super(persistancesClass);
	}
	
	public PriceTrainLclDao(){
		this(PriceTrainLcl.class);
	}
}