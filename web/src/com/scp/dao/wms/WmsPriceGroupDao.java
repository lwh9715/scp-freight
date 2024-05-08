package com.scp.dao.wms;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.wms.WmsPriceGroup;



/**
* 
 * @author sunny
 */
@Component
public class WmsPriceGroupDao extends BaseDaoImpl<WmsPriceGroup, Long>{
	
	protected WmsPriceGroupDao(Class<WmsPriceGroup> persistancesClass) {
		super(persistancesClass);
	}

	public WmsPriceGroupDao() {
		this(WmsPriceGroup.class);
	}
}