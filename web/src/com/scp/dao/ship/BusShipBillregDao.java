package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusShipBillreg;

/**
 * 
 * @author Neo
 */
@Component
public class BusShipBillregDao extends BaseDaoImpl<BusShipBillreg, Long> {

	protected BusShipBillregDao(Class<BusShipBillreg> persistancesClass) {
		super(persistancesClass);
	}

	public BusShipBillregDao() {
		this(BusShipBillreg.class);
	}

}
