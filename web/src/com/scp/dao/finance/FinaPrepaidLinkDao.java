package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaPrepaidLink;


/**
* 
 * @author Yves
 */
@Component
public class FinaPrepaidLinkDao extends BaseDaoImpl<FinaPrepaidLink, Long>{
	
	protected FinaPrepaidLinkDao(Class<FinaPrepaidLink> persistancesClass) {
		super(persistancesClass);
	}

	public FinaPrepaidLinkDao() {
		this(FinaPrepaidLink.class);
	}
}