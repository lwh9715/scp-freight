package com.scp.dao.bpm;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bpm.BpmTask;


/**
 * @author neo
 */
@Component
@Lazy(true)
public class BpmTaskDao extends BaseDaoImpl<BpmTask, Long>{
	
	protected BpmTaskDao(Class<BpmTask> persistancesClass) {
		super(persistancesClass);
	}

	public BpmTaskDao() {
		this(BpmTask.class);
	}
	
	
}