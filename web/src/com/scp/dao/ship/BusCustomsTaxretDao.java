package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusCustomsTaxret;

/**
* 
 * @author Neo
 */
@Component
public class BusCustomsTaxretDao extends BaseDaoImpl<BusCustomsTaxret, Long>{
	
	protected BusCustomsTaxretDao(Class<BusCustomsTaxret> persistancesClass) {
		super(persistancesClass);
	}

	public BusCustomsTaxretDao() {
		this(BusCustomsTaxret.class);
	}
}