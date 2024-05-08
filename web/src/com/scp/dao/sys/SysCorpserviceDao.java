package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysCorpservice;



/**
* 
 * @author neo
 */
@Component
public class SysCorpserviceDao extends BaseDaoImpl<SysCorpservice, Long>{
	
	protected SysCorpserviceDao(Class<SysCorpservice> persistancesClass) {
		super(persistancesClass);
	}

	public SysCorpserviceDao() {
		this(SysCorpservice.class);
	}
}