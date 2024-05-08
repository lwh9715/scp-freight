package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusShipBill;



/**
* 
 * @author neo
 */
@Component
public class BusShipBillDao extends BaseDaoImpl<BusShipBill, Long>{
	
	protected BusShipBillDao(Class<BusShipBill> persistancesClass) {
		super(persistancesClass);
	}

	public BusShipBillDao() {
		this(BusShipBill.class);
	}
}