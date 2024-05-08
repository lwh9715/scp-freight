package com.scp.dao.bpm;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bpm.BpmProcessinstance;


/**
 * @author neo
 */
@Component
@Lazy(true)
public class BpmProcessinstanceDao extends BaseDaoImpl<BpmProcessinstance, Long>{
	
	protected BpmProcessinstanceDao(Class<BpmProcessinstance> persistancesClass) {
		super(persistancesClass);
	}

	public BpmProcessinstanceDao() {
		this(BpmProcessinstance.class);
	}
	
	
}