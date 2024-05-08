package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatCurrency;



/**
* 
 * @author neo
 */
@Component
public class DatCurrencyDao extends BaseDaoImpl<DatCurrency, Long>{
	
	protected DatCurrencyDao(Class<DatCurrency> persistancesClass) {
		super(persistancesClass);
	}

	public DatCurrencyDao() {
		this(DatCurrency.class);
	}
}