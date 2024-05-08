package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysEmailFastext;



/**
* 
 * @author neo
 */
@Component
public class SysEmailFastextDao extends BaseDaoImpl<SysEmailFastext, Long>{
	
	protected SysEmailFastextDao(Class<SysEmailFastext> persistancesClass) {
		super(persistancesClass);
	}

	public SysEmailFastextDao() {
		this(SysEmailFastext.class);
	}
}