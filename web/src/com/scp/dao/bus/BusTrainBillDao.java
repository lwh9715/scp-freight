package com.scp.dao.bus;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bus.BusTrainBill;

@Component
@Lazy(true)
public class BusTrainBillDao extends BaseDaoImpl<BusTrainBill, Long>{
	
	protected BusTrainBillDao(Class<BusTrainBill> persistancesClass) {
		super(persistancesClass);
	}

	public BusTrainBillDao() {
		this(BusTrainBill.class);
	}
	
	
}