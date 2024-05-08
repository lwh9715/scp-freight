package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysUseronline;



/**
* 
 * @author neo
 */
@Component
public class SysUseronlineDao extends BaseDaoImpl<SysUseronline, Long>{
	
	protected SysUseronlineDao(Class<SysUseronline> persistancesClass) {
		super(persistancesClass);
	}

	public SysUseronlineDao() {
		this(SysUseronline.class);
	}
}