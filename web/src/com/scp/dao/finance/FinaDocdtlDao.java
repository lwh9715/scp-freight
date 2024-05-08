package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaDocdtl;



/**
* 
 * @author neo
 */
@Component
public class FinaDocdtlDao extends BaseDaoImpl<FinaDocdtl, Long>{
	
	protected FinaDocdtlDao(Class<FinaDocdtl> persistancesClass) {
		super(persistancesClass);
	}

	public FinaDocdtlDao() {
		this(FinaDocdtl.class);
	}
}