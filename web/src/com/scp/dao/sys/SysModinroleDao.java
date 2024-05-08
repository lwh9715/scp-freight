package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysModinrole;



/**
* 
 * @author neo
 */
@Component
public class SysModinroleDao extends BaseDaoImpl<SysModinrole, Long>{
	
	protected SysModinroleDao(Class<SysModinrole> persistancesClass) {
		super(persistancesClass);
	}

	public SysModinroleDao() {
		this(SysModinrole.class);
	}
}