package com.scp.dao.api;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.api.ApiCoscoBookData;


@Component
@Lazy(true)
public class ApiCoscoBookdataDao extends BaseDaoImpl<ApiCoscoBookData, Long>{
	
	protected ApiCoscoBookdataDao(Class<ApiCoscoBookData> persistancesClass) {
		super(persistancesClass);
	}

	public ApiCoscoBookdataDao() {
		this(ApiCoscoBookData.class);
	}
	
	
}