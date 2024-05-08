package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysUserinrole;



/**
* 
 * @author neo
 */
@Component
public class SysUserinroleDao extends BaseDaoImpl<SysUserinrole, Long>{
	
	protected SysUserinroleDao(Class<SysUserinrole> persistancesClass) {
		super(persistancesClass);
	}

	public SysUserinroleDao() {
		this(SysUserinrole.class);
	}
}