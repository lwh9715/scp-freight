package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysModule;



/**
* 
 * @author neo
 */
@Component
public class SysModuleDao extends BaseDaoImpl<SysModule, Long>{
	
	protected SysModuleDao(Class<SysModule> persistancesClass) {
		super(persistancesClass);
	}

	public SysModuleDao() {
		this(SysModule.class);
	}
}