package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysTimeTaskLog;



/**
* 
 * @author neo
 */
@Component
public class SysTimeTaskLogDao extends BaseDaoImpl<SysTimeTaskLog, Long>{
	
	protected SysTimeTaskLogDao(Class<SysTimeTaskLog> persistancesClass) {
		super(persistancesClass);
	}

	public SysTimeTaskLogDao() {
		this(SysTimeTaskLog.class);
	}
}