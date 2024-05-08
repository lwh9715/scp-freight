package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysFormdef;



/**
* 
 * @author neo
 */
@Component
public class SysFormdefDao extends BaseDaoImpl<SysFormdef, Long>{
	
	protected SysFormdefDao(Class<SysFormdef> persistancesClass) {
		super(persistancesClass);
	}

	public SysFormdefDao() {
		this(SysFormdef.class);
	}
}