package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaRpreq;



/**
* 
 * @author neo
 */
@Component
public class FinaRpreqDao extends BaseDaoImpl<FinaRpreq, Long>{
	
	protected FinaRpreqDao(Class<FinaRpreq> persistancesClass) {
		super(persistancesClass);
	}

	public FinaRpreqDao() {
		this(FinaRpreq.class);
	}
}