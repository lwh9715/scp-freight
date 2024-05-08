package com.scp.dao.api;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.api.ApiMaerskPort;


/**
 * @author neo
 */
@Component
@Lazy(true)
public class ApiMaerskPortDao extends BaseDaoImpl<ApiMaerskPort, Long>{
	
	protected ApiMaerskPortDao(Class<ApiMaerskPort> persistancesClass) {
		super(persistancesClass);
	}

	public ApiMaerskPortDao() {
		this(ApiMaerskPort.class);
	}
	
	
}