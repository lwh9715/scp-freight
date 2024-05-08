package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaArapTempdtl;



/**
* 
 * @author neo
 */
@Component
public class FinaArapTempdtlDao extends BaseDaoImpl<FinaArapTempdtl, Long>{
	
	protected FinaArapTempdtlDao(Class<FinaArapTempdtl> persistancesClass) {
		super(persistancesClass);
	}

	public FinaArapTempdtlDao() {
		this(FinaArapTempdtl.class);
	}
}