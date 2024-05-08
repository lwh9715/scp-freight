package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysConfig;



/**
* 
 * @author neo
 */
@Component
public class SysConfigDao extends BaseDaoImpl<SysConfig, Long>{
	
	protected SysConfigDao(Class<SysConfig> persistancesClass) {
		super(persistancesClass);
	}

	public SysConfigDao() {
		this(SysConfig.class);
	}
	
	public SysConfig findConfig(String key) {
		key = key.replace("'", "''");
		SysConfig sysConfig = this.findOneRowByClauseWhere("key='"+key+"'");
		return sysConfig;
	}
}