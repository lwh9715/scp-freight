package com.scp.dao.price;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.price.PriceFclUserule;

@Component
public class PriceFclUseruleDao extends BaseDaoImpl<PriceFclUserule, Long>{

	protected PriceFclUseruleDao(Class<PriceFclUserule> persistancesClass) {
		super(persistancesClass);
	}
	
	public PriceFclUseruleDao(){
		this(PriceFclUserule.class);
	}
}