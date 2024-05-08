package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaJobs;



/**
* 
 * @author neo
 */
@Component
public class FinaJobsDao extends BaseDaoImpl<FinaJobs, Long>{
	
	protected FinaJobsDao(Class<FinaJobs> persistancesClass) {
		super(persistancesClass);
	}

	public FinaJobsDao() {
		this(FinaJobs.class);
	}
}