package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusCustoms;



/**
* 
 * @author neo
 */
@Component
public class BusCustomsDao extends BaseDaoImpl<BusCustoms, Long>{
	
	protected BusCustomsDao(Class<BusCustoms> persistancesClass) {
		super(persistancesClass);
	}

	public BusCustomsDao() {
		this(BusCustoms.class);
	}
}