package com.scp.dao.price;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.price.PriceFclFeeadd;

/**
 * @author bruce
 */
@Component
public class PricefclfeeaddDao extends BaseDaoImpl<PriceFclFeeadd, Long> {

	protected PricefclfeeaddDao(Class<PriceFclFeeadd> persistancesClass) {
		super(persistancesClass);
	}

	public PricefclfeeaddDao() {
		this(PriceFclFeeadd.class);
	}
}
