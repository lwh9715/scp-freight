package com.scp.dao.api;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.api.ApiRobotBook;


@Component
@Lazy(true)
public class ApiRobotBookDao extends BaseDaoImpl<ApiRobotBook, Long>{
	
	protected ApiRobotBookDao(Class<ApiRobotBook> persistancesClass) {
		super(persistancesClass);
	}

	public ApiRobotBookDao() {
		this(ApiRobotBook.class);
	}
	
	
}