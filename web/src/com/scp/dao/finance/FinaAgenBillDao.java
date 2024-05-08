package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaAgenBill;


@Component
public class FinaAgenBillDao extends BaseDaoImpl<FinaAgenBill, Long>{
	
	protected FinaAgenBillDao(Class<FinaAgenBill> persistancesClass) {
		super(persistancesClass);
	}

	public FinaAgenBillDao() {
		this(FinaAgenBill.class);
	}
}