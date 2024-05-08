package com.scp.dao.api;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.api.ApiRobotFeeadd;

@Component
@Lazy(true)
public class RobotFeeaddDao extends BaseDaoImpl<ApiRobotFeeadd, Long>{
	protected RobotFeeaddDao(Class<ApiRobotFeeadd> persistancesClass) {
		super(persistancesClass);
	}

	public RobotFeeaddDao() {
		this(ApiRobotFeeadd.class);
	}

}
