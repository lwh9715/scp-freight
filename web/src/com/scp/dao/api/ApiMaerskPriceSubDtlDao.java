package com.scp.dao.api;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.api.ApiMaerskPriceSubDtl;


/**
 * @author neo
 */
@Component
@Lazy(true)
public class ApiMaerskPriceSubDtlDao extends BaseDaoImpl<ApiMaerskPriceSubDtl, Long>{
	
	protected ApiMaerskPriceSubDtlDao(Class<ApiMaerskPriceSubDtl> persistancesClass) {
		super(persistancesClass);
	}

	public ApiMaerskPriceSubDtlDao() {
		this(ApiMaerskPriceSubDtl.class);
	}
	
}