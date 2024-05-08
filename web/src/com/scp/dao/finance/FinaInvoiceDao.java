package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaInvoice;



/**
* 
 * @author neo
 */
@Component
public class FinaInvoiceDao extends BaseDaoImpl<FinaInvoice, Long>{
	
	protected FinaInvoiceDao(Class<FinaInvoice> persistancesClass) {
		super(persistancesClass);
	}

	public FinaInvoiceDao() {
		this(FinaInvoice.class);
	}
}