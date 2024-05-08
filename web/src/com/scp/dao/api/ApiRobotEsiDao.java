package com.scp.dao.api;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.api.ApiRobotEsi;


@Component
@Lazy(true)
public class ApiRobotEsiDao extends BaseDaoImpl<ApiRobotEsi, Long>{
	
	protected ApiRobotEsiDao(Class<ApiRobotEsi> persistancesClass) {
		super(persistancesClass);
	}

	public ApiRobotEsiDao() {
		this(ApiRobotEsi.class);
	}
	
	
}