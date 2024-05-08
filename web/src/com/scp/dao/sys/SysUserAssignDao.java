package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysUserAssign;



/**
* 
 * @author neo
 */
@Component
public class SysUserAssignDao extends BaseDaoImpl<SysUserAssign, Long>{
	
	protected SysUserAssignDao(Class<SysUserAssign> persistancesClass) {
		super(persistancesClass);
	}

	public SysUserAssignDao() {
		this(SysUserAssign.class);
	}
}