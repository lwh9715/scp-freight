package com.scp.dao.wms;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.wms.WmsIn;



/**
* 
 * @author sunny
 */
@Component
public class WmsInDao extends BaseDaoImpl<WmsIn, Long>{
	
	protected WmsInDao(Class<WmsIn> persistancesClass) {
		super(persistancesClass);
	}

	public WmsInDao() {
		this(WmsIn.class);
	}
}