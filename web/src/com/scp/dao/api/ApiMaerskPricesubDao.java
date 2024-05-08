package com.scp.dao.api;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.api.ApiMaerskPriceSub;


/**
 * @author neo
 */
@Component
@Lazy(true)
public class ApiMaerskPricesubDao extends BaseDaoImpl<ApiMaerskPriceSub, Long>{
	
	protected ApiMaerskPricesubDao(Class<ApiMaerskPriceSub> persistancesClass) {
		super(persistancesClass);
	}

	public ApiMaerskPricesubDao() {
		this(ApiMaerskPriceSub.class);
	}
	
}