package com.scp.dao.api;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.api.ApiRobotBookPre;


@Component
@Lazy(true)
public class ApiRobotBookPreDao extends BaseDaoImpl<ApiRobotBookPre, Long>{
	
	protected ApiRobotBookPreDao(Class<ApiRobotBookPre> persistancesClass) {
		super(persistancesClass);
	}

	public ApiRobotBookPreDao() {
		this(ApiRobotBookPre.class);
	}
	
	
}