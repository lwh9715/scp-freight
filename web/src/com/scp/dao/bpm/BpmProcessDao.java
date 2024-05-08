package com.scp.dao.bpm;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bpm.BpmProcess;


/**
 * @author neo
 */
@Component
@Lazy(true)
public class BpmProcessDao extends BaseDaoImpl<BpmProcess, Long>{
	
	protected BpmProcessDao(Class<BpmProcess> persistancesClass) {
		super(persistancesClass);
	}

	public BpmProcessDao() {
		this(BpmProcess.class);
	}
	
	
}