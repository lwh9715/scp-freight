package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysMemo;



/**
* 
 * @author neo
 */
@Component("sysMemoDao")
public class SysMemoDao extends BaseDaoImpl<SysMemo, Long>{
	
	protected SysMemoDao(Class<SysMemo> persistancesClass) {
		super(persistancesClass);
	}

	public SysMemoDao() {
		this(SysMemo.class);
	}
}