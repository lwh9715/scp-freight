package com.scp.dao.bpm;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bpm.BpmAssign;


/**
 * @author neo
 */
@Component
@Lazy(true)
public class BpmAssignDao extends BaseDaoImpl<BpmAssign, Long>{
	
	protected BpmAssignDao(Class<BpmAssign> persistancesClass) {
		super(persistancesClass);
	}

	public BpmAssignDao() {
		this(BpmAssign.class);
	}
	
	
}