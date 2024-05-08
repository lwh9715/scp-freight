package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysCustlibCust;



/**
* 
 * @author neo
 */
@Component
public class SysCustlibCustDao extends BaseDaoImpl<SysCustlibCust, Long>{
	
	protected SysCustlibCustDao(Class<SysCustlibCust> persistancesClass) {
		super(persistancesClass);
	}

	public SysCustlibCustDao() {
		this(SysCustlibCust.class);
	}
}