package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaBill;



/**
* 
 * @author neo
 */
@Component
public class FinaBillDao extends BaseDaoImpl<FinaBill, Long>{
	
	protected FinaBillDao(Class<FinaBill> persistancesClass) {
		super(persistancesClass);
	}

	public FinaBillDao() {
		this(FinaBill.class);
	}
}