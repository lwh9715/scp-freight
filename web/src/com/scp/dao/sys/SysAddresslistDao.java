package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysAddresslist;



/**
* 
 * @author neo
 */
@Component
public class SysAddresslistDao extends BaseDaoImpl<SysAddresslist, Long>{
	
	protected SysAddresslistDao(Class<SysAddresslist> persistancesClass) {
		super(persistancesClass);
	}

	public SysAddresslistDao() {
		this(SysAddresslist.class);
	}
}