package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysEmail;



/**
* 
 * @author neo
 */
@Component
public class SysEmailDao extends BaseDaoImpl<SysEmail, Long>{
	
	protected SysEmailDao(Class<SysEmail> persistancesClass) {
		super(persistancesClass);
	}

	public SysEmailDao() {
		this(SysEmail.class);
	}
}