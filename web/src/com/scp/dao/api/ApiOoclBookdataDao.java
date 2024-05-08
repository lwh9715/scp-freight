package com.scp.dao.api;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.api.ApiOoclBookData;


@Component
@Lazy(true)
public class ApiOoclBookdataDao extends BaseDaoImpl<ApiOoclBookData, Long> {
	
	protected ApiOoclBookdataDao(Class<ApiOoclBookData> persistancesClass) {
		super(persistancesClass);
	}

	public ApiOoclBookdataDao() {
		this(ApiOoclBookData.class);
	}
	
	
}