package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaInvoiceDtl;



/**
* 
 * @author neo
 */
@Component
public class FinaInvoiceDtlDao extends BaseDaoImpl<FinaInvoiceDtl, Long>{
	
	protected FinaInvoiceDtlDao(Class<FinaInvoiceDtl> persistancesClass) {
		super(persistancesClass);
	}

	public FinaInvoiceDtlDao() {
		this(FinaInvoiceDtl.class);
	}
}