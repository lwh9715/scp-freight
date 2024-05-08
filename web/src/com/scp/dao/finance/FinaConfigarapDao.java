package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaConfigarap;


@Component
public class FinaConfigarapDao extends BaseDaoImpl<FinaConfigarap, Long>{
	
	protected FinaConfigarapDao(Class<FinaConfigarap> persistancesClass) {
		super(persistancesClass);
	}

	public FinaConfigarapDao() {
		this(FinaConfigarap.class);
	}
}