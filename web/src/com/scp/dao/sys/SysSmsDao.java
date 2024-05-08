package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysSms;



/**
* 
 * @author neo
 */
@Component
public class SysSmsDao extends BaseDaoImpl<SysSms, Long>{
	
	protected SysSmsDao(Class<SysSms> persistancesClass) {
		super(persistancesClass);
	}

	public SysSmsDao() {
		this(SysSms.class);
	}
}