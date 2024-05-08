package com.scp.dao.wms;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.wms.WmsPrice;



/**
* 
 * @author sunny
 */
@Component
public class WmsPriceDao extends BaseDaoImpl<WmsPrice, Long>{
	
	protected WmsPriceDao(Class<WmsPrice> persistancesClass) {
		super(persistancesClass);
	}

	public WmsPriceDao() {
		this(WmsPrice.class);
	}
}