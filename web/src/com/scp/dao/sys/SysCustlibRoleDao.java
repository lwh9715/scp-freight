package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysCustlibRole;



/**
* 
 * @author neo
 */
@Component
public class SysCustlibRoleDao extends BaseDaoImpl<SysCustlibRole, Long>{
	
	protected SysCustlibRoleDao(Class<SysCustlibRole> persistancesClass) {
		super(persistancesClass);
	}

	public SysCustlibRoleDao() {
		this(SysCustlibRole.class);
	}
}