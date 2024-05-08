package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaArap;



/**
* 
 * @author neo
 */
@Component
public class FinaArapDao extends BaseDaoImpl<FinaArap, Long>{
	
	protected FinaArapDao(Class<FinaArap> persistancesClass) {
		super(persistancesClass);
	}

	public FinaArapDao() {
		this(FinaArap.class);
	}
}