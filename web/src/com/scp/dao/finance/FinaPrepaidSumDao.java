package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaPrepaidSum;


/**
* 
 * @author Yves
 */
@Component
public class FinaPrepaidSumDao extends BaseDaoImpl<FinaPrepaidSum, Long>{
	
	protected FinaPrepaidSumDao(Class<FinaPrepaidSum> persistancesClass) {
		super(persistancesClass);
	}

	public FinaPrepaidSumDao() {
		this(FinaPrepaidSum.class);
	}
}