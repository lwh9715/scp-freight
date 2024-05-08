package com.scp.dao.bpm;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bpm.BpmProcessinsVar;


/**
 * @author neo
 */
@Component
@Lazy(true)
public class BpmProcessinsVarDao extends BaseDaoImpl<BpmProcessinsVar, Long>{
	
	protected BpmProcessinsVarDao(Class<BpmProcessinsVar> persistancesClass) {
		super(persistancesClass);
	}

	public BpmProcessinsVarDao() {
		this(BpmProcessinsVar.class);
	}
	
	
}