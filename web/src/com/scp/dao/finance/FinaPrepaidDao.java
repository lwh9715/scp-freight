package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaPrepaid;


/**
* 
 * @author Yves
 */
@Component
public class FinaPrepaidDao extends BaseDaoImpl<FinaPrepaid, Long>{
	
	protected FinaPrepaidDao(Class<FinaPrepaid> persistancesClass) {
		super(persistancesClass);
	}

	public FinaPrepaidDao() {
		this(FinaPrepaid.class);
	}
}