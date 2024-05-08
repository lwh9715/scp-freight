package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaArapTemplet;



/**
* 
 * @author neo
 */
@Component
public class FinaArapTempletDao extends BaseDaoImpl<FinaArapTemplet, Long>{
	
	protected FinaArapTempletDao(Class<FinaArapTemplet> persistancesClass) {
		super(persistancesClass);
	}

	public FinaArapTempletDao() {
		this(FinaArapTemplet.class);
	}
}