package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaDocnum;



/**
* 
 * @author neo
 */
@Component
public class FinaDocnumDao extends BaseDaoImpl<FinaDocnum, Long>{
	
	protected FinaDocnumDao(Class<FinaDocnum> persistancesClass) {
		super(persistancesClass);
	}

	public FinaDocnumDao() {
		this(FinaDocnum.class);
	}
}