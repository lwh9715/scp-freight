package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysCustlibUser;



/**
* 
 * @author neo
 */
@Component
public class SysCustlibUserDao extends BaseDaoImpl<SysCustlibUser, Long>{
	
	protected SysCustlibUserDao(Class<SysCustlibUser> persistancesClass) {
		super(persistancesClass);
	}

	public SysCustlibUserDao() {
		this(SysCustlibUser.class);
	}
}