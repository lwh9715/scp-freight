package com.scp.dao.wms;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.wms.WmsDispatch;



/**
* 
 * @author sunny
 */
@Component
public class WmsDispatchDao extends BaseDaoImpl<WmsDispatch, Long>{
	
	protected WmsDispatchDao(Class<WmsDispatch> persistancesClass) {
		super(persistancesClass);
	}

	public WmsDispatchDao() {
		this(WmsDispatch.class);
	}
}