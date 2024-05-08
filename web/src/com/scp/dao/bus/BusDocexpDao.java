package com.scp.dao.bus;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bus.BusDocexp;



/**
* 
 * @author neo
 */
@Component
public class BusDocexpDao extends BaseDaoImpl<BusDocexp, Long>{
	
	protected BusDocexpDao(Class<BusDocexp> persistancesClass) {
		super(persistancesClass);
	}

	public BusDocexpDao() {
		this(BusDocexp.class);
	}
}