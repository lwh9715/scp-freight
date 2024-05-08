package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysCustlib;



/**
* 
 * @author neo
 */
@Component
public class SysCustlibDao extends BaseDaoImpl<SysCustlib, Long>{
	
	protected SysCustlibDao(Class<SysCustlib> persistancesClass) {
		super(persistancesClass);
	}

	public SysCustlibDao() {
		this(SysCustlib.class);
	}
}