package com.scp.dao.order;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.order.BusOrder;

/**
 * @author bruce
 */
@Component
public class BusOrderDao extends BaseDaoImpl<BusOrder, Long> {

	protected BusOrderDao(Class<BusOrder> persistancesClass) {
		super(persistancesClass);
	}

	public BusOrderDao() {
		this(BusOrder.class);
	}
}
