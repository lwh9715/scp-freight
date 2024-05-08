package com.scp.dao.bpm;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bpm.BpmAssignRef;


/**
 * @author neo
 */
@Component
@Lazy(true)
public class BpmAssignRefDao extends BaseDaoImpl<BpmAssignRef, Long>{
	
	protected BpmAssignRefDao(Class<BpmAssignRef> persistancesClass) {
		super(persistancesClass);
	}

	public BpmAssignRefDao() {
		this(BpmAssignRef.class);
	}
	
	
}