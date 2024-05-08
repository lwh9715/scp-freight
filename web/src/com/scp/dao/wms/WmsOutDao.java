package com.scp.dao.wms;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.wms.WmsOut;



/**
* 
 * @author sunny
 */
@Component
public class WmsOutDao extends BaseDaoImpl<WmsOut, Long>{
	
	protected WmsOutDao(Class<WmsOut> persistancesClass) {
		super(persistancesClass);
	}

	public WmsOutDao() {
		this(WmsOut.class);
	}
}