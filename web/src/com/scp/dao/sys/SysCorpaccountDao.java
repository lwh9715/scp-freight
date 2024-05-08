package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysCorpaccount;



/**
* 
 * @author neo
 */
@Component
public class SysCorpaccountDao extends BaseDaoImpl<SysCorpaccount, Long>{
	
	protected SysCorpaccountDao(Class<SysCorpaccount> persistancesClass) {
		super(persistancesClass);
	}

	public SysCorpaccountDao() {
		this(SysCorpaccount.class);
	}

	
}