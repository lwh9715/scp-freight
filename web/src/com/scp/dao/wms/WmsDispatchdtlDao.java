package com.scp.dao.wms;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.wms.WmsDispatchdtl;



/**
* 
 * @author sunny
 */
@Component
public class WmsDispatchdtlDao extends BaseDaoImpl<WmsDispatchdtl, Long>{
	
	protected WmsDispatchdtlDao(Class<WmsDispatchdtl> persistancesClass) {
		super(persistancesClass);
	}

	public WmsDispatchdtlDao() {
		this(WmsDispatchdtl.class);
	}
}