package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysCorpcontacts;



/**
* 
 * @author neo
 */
@Component
public class SysCorpcontactsDao extends BaseDaoImpl<SysCorpcontacts, Long>{
	
	protected SysCorpcontactsDao(Class<SysCorpcontacts> persistancesClass) {
		super(persistancesClass);
	}

	public SysCorpcontactsDao() {
		this(SysCorpcontacts.class);
	}
}