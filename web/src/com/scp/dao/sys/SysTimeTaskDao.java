package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysTimeTask;



/**
* 
 * @author neo
 */
@Component
public class SysTimeTaskDao extends BaseDaoImpl<SysTimeTask, Long>{
	
	protected SysTimeTaskDao(Class<SysTimeTask> persistancesClass) {
		super(persistancesClass);
	}

	public SysTimeTaskDao() {
		this(SysTimeTask.class);
	}
}