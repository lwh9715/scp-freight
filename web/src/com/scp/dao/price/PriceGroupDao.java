package com.scp.dao.price;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.price.PriceGroup;

@Component
public class PriceGroupDao extends BaseDaoImpl<PriceGroup, Long>{

	protected PriceGroupDao(Class<PriceGroup> persistancesClass) {
		super(persistancesClass);
	}
	
	public PriceGroupDao(){
		this(PriceGroup.class);
	}
}