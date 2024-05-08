package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaRpreqdtl;



/**
* 
 * @author neo
 */
@Component
public class FinaRpreqdtlDao extends BaseDaoImpl<FinaRpreqdtl, Long>{
	
	protected FinaRpreqdtlDao(Class<FinaRpreqdtl> persistancesClass) {
		super(persistancesClass);
	}

	public FinaRpreqdtlDao() {
		this(FinaRpreqdtl.class);
	}
}