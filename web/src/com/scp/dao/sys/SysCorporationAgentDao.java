package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysCorporationAgent;



@Component
public class SysCorporationAgentDao extends BaseDaoImpl<SysCorporationAgent, Long>{
	
	protected SysCorporationAgentDao(Class<SysCorporationAgent> persistancesClass) {
		super(persistancesClass);
	}

	public SysCorporationAgentDao() {
		this(SysCorporationAgent.class);
	}
}