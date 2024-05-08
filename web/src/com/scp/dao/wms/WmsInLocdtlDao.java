package com.scp.dao.wms;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.wms.WmsInLocdtl;



/**
* 
 * @author sunny
 */
@Component
public class WmsInLocdtlDao extends BaseDaoImpl<WmsInLocdtl, Long>{
	
	protected WmsInLocdtlDao(Class<WmsInLocdtl> persistancesClass) {
		super(persistancesClass);
	}

	public WmsInLocdtlDao() {
		this(WmsInLocdtl.class);
	}
}