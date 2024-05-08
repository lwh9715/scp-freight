package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysCorpinv;



/**
* 
 * @author neo
 */
@Component
public class SysCorpinvDao extends BaseDaoImpl<SysCorpinv, Long>{
	
	protected SysCorpinvDao(Class<SysCorpinv> persistancesClass) {
		super(persistancesClass);
	}

	public SysCorpinvDao() {
		this(SysCorpinv.class);
	}
}