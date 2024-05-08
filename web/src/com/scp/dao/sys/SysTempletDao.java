package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysTemplet;



/**
* 
 * @author neo
 */
@Component
public class SysTempletDao extends BaseDaoImpl<SysTemplet, Long>{
	
	protected SysTempletDao(Class<SysTemplet> persistancesClass) {
		super(persistancesClass);
	}

	public SysTempletDao() {
		this(SysTemplet.class);
	}
}