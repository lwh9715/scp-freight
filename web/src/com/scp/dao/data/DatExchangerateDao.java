package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatExchangerate;



/**
* 
 * @author neo
 */
@Component
public class DatExchangerateDao extends BaseDaoImpl<DatExchangerate, Long>{
	
	protected DatExchangerateDao(Class<DatExchangerate> persistancesClass) {
		super(persistancesClass);
	}

	public DatExchangerateDao() {
		this(DatExchangerate.class);
	}
}