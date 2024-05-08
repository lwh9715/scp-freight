package com.scp.dao.del;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.del.DelDelivery;



/**
* 
 * @author sunny
 */
@Component
public class DelDeliveryDao extends BaseDaoImpl<DelDelivery, Long>{
	
	protected DelDeliveryDao(Class<DelDelivery> persistancesClass) {
		super(persistancesClass);
	}

	public DelDeliveryDao() {
		this(DelDelivery.class);
	}
}