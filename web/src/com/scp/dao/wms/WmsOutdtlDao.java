package com.scp.dao.wms;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.wms.WmsOutdtl;



/**
* 
 * @author sunny
 */
@Component
public class WmsOutdtlDao extends BaseDaoImpl<WmsOutdtl, Long>{
	
	protected WmsOutdtlDao(Class<WmsOutdtl> persistancesClass) {
		super(persistancesClass);
	}

	public WmsOutdtlDao() {
		this(WmsOutdtl.class);
	}
}