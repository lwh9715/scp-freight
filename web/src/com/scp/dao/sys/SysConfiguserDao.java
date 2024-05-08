package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysConfiguser;



/**
* 
 * @author neo
 */
@Component
public class SysConfiguserDao extends BaseDaoImpl<SysConfiguser, Long>{
	
	protected SysConfiguserDao(Class<SysConfiguser> persistancesClass) {
		super(persistancesClass);
	}

	public SysConfiguserDao() {
		this(SysConfiguser.class);
	}
}