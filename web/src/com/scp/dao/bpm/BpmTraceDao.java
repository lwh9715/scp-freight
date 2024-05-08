package com.scp.dao.bpm;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bpm.BpmTrace;


/**
 * @author neo
 */
@Component
@Lazy(true)
public class BpmTraceDao extends BaseDaoImpl<BpmTrace, Long>{
	
	protected BpmTraceDao(Class<BpmTrace> persistancesClass) {
		super(persistancesClass);
	}

	public BpmTraceDao() {
		this(BpmTrace.class);
	}
	
	
}