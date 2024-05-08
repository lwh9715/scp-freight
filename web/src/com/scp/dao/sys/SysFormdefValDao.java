package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysFormdefVal;



/**
* 
 * @author neo
 */
@Component
public class SysFormdefValDao extends BaseDaoImpl<SysFormdefVal, Long>{
	
	protected SysFormdefValDao(Class<SysFormdefVal> persistancesClass) {
		super(persistancesClass);
	}

	public SysFormdefValDao() {
		this(SysFormdefVal.class);
	}
}