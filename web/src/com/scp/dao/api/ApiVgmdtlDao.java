package com.scp.dao.api;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.api.ApiVgmdtl;


@Component
@Lazy(true)
public class ApiVgmdtlDao extends BaseDaoImpl<ApiVgmdtl, Long> {

	protected ApiVgmdtlDao(Class<ApiVgmdtl> arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	public ApiVgmdtlDao() {
		this(ApiVgmdtl.class);
	}
		
}
