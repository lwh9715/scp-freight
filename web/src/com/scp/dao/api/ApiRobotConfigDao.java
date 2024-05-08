package com.scp.dao.api;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.api.ApiRobotConfig;


@Component
@Lazy(true)
public class ApiRobotConfigDao extends BaseDaoImpl<ApiRobotConfig, Long>{
	
	protected ApiRobotConfigDao(Class<ApiRobotConfig> persistancesClass) {
		super(persistancesClass);
	}

	public ApiRobotConfigDao() {
		this(ApiRobotConfig.class);
	}
	
	
}