package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysGridDef;

/**
 * 
 * @author Neo
 */
@Component
public class SysGridDefDao extends BaseDaoImpl<SysGridDef, Long> {

	protected SysGridDefDao(Class<SysGridDef> persistancesClass) {
		super(persistancesClass);
	}

	public SysGridDefDao() {
		this(SysGridDef.class);
	}
}
