package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaJobsLink;



/**
* 
 * @author neo
 */
@Component
public class FinaJobsLinkDao extends BaseDaoImpl<FinaJobsLink, Long>{
	
	protected FinaJobsLinkDao(Class<FinaJobsLink> persistancesClass) {
		super(persistancesClass);
	}

	public FinaJobsLinkDao() {
		this(FinaJobsLink.class);
	}
}