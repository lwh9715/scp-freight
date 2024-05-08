package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.Sysformcfg;



/**
* 
 * @author neo
 */
@Component
public class SysformcfgDao extends BaseDaoImpl<Sysformcfg, Long>{
	
	protected SysformcfgDao(Class<Sysformcfg> persistancesClass) {
		super(persistancesClass);
	}

	public SysformcfgDao() {
		this(Sysformcfg.class);
	}
}