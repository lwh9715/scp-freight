package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysUser;



/**
* 
 * @author neo
 */
@Component
public class SysUserDao extends BaseDaoImpl<SysUser, Long>{
	
	protected SysUserDao(Class<SysUser> persistancesClass) {
		super(persistancesClass);
	}

	public SysUserDao() {
		this(SysUser.class);
	}
}