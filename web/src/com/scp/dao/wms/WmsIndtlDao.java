package com.scp.dao.wms;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.wms.WmsIndtl;



/**
* 
 * @author sunny
 */
@Component
public class WmsIndtlDao extends BaseDaoImpl<WmsIndtl, Long>{
	
	protected WmsIndtlDao(Class<WmsIndtl> persistancesClass) {
		super(persistancesClass);
	}

	public WmsIndtlDao() {
		this(WmsIndtl.class);
	}
}