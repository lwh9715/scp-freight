package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaStatement;



/**
* 
 * @author neo
 */
@Component
public class FinaStatementDao extends BaseDaoImpl<FinaStatement, Long>{
	
	protected FinaStatementDao(Class<FinaStatement> persistancesClass) {
		super(persistancesClass);
	}

	public FinaStatementDao() {
		this(FinaStatement.class);
	}
}