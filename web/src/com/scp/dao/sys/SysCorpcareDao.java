package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysCorpcare;



/**
* 
 * @author neo
 */
@Component
public class SysCorpcareDao extends BaseDaoImpl<SysCorpcare, Long>{
	
	protected SysCorpcareDao(Class<SysCorpcare> persistancesClass) {
		super(persistancesClass);
	}

	public SysCorpcareDao() {
		this(SysCorpcare.class);
	}
	
	
}