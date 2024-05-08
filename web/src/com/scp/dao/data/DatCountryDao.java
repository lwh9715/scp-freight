package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatCountry;



/**
* 
 * @author neo
 */
@Component
public class DatCountryDao extends BaseDaoImpl<DatCountry, Long>{
	
	protected DatCountryDao(Class<DatCountry> persistancesClass) {
		super(persistancesClass);
	}

	public DatCountryDao() {
		this(DatCountry.class);
	}
}