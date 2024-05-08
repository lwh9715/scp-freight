package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaDoc;



/**
* 
 * @author neo
 */
@Component
public class FinaDocDao extends BaseDaoImpl<FinaDoc, Long>{
	
	protected FinaDocDao(Class<FinaDoc> persistancesClass) {
		super(persistancesClass);
	}

	public FinaDocDao() {
		this(FinaDoc.class);
	}
}