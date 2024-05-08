package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysLog;



/**
* 
 * @author neo
 */
@Component
public class SysLogDao extends BaseDaoImpl<SysLog, Long>{
	
	protected SysLogDao(Class<SysLog> persistancesClass) {
		super(persistancesClass);
	}

	public SysLogDao() {
		this(SysLog.class);
	}
}