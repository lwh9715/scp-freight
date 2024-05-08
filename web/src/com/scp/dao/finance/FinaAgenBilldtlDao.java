package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaAgenBilldtl;


@Component
public class FinaAgenBilldtlDao extends BaseDaoImpl<FinaAgenBilldtl, Long>{
	
	protected FinaAgenBilldtlDao(Class<FinaAgenBilldtl> persistancesClass) {
		super(persistancesClass);
	}

	public FinaAgenBilldtlDao() {
		this(FinaAgenBilldtl.class);
	}
}