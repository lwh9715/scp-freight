package com.scp.dao.bpm;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bpm.BpmWorkItem;


/**
 * @author neo
 */
@Component
@Lazy(true)
public class BpmWorkItemDao extends BaseDaoImpl<BpmWorkItem, Long>{
	
	protected BpmWorkItemDao(Class<BpmWorkItem> persistancesClass) {
		super(persistancesClass);
	}

	public BpmWorkItemDao() {
		this(BpmWorkItem.class);
	}
	
	
}