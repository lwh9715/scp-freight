package com.scp.dao.price;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.price.Pricefclpubrule;

@Component
public class PricefclpubruleDao extends BaseDaoImpl<Pricefclpubrule, Long>{

	protected PricefclpubruleDao(Class<Pricefclpubrule> persistancesClass) {
		super(persistancesClass);
	}
	
	public PricefclpubruleDao(){
		this(Pricefclpubrule.class);
	}
}