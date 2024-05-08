package com.scp.dao.wms;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.wms.WmsOutdtlref;



/**
* 
 * @author sunny
 */
@Component
public class WmsOutdtlrefDao extends BaseDaoImpl<WmsOutdtlref, Long>{
	
	protected WmsOutdtlrefDao(Class<WmsOutdtlref> persistancesClass) {
		super(persistancesClass);
	}

	public WmsOutdtlrefDao() {
		this(WmsOutdtlref.class);
	}
}