package com.scp.dao.api;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.api.ApiVgm;


@Component
@Lazy(true)
public class ApiVgmDao extends BaseDaoImpl<ApiVgm, Long>{
	
	protected ApiVgmDao(Class<ApiVgm> arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ApiVgmDao() {
		this(ApiVgm.class);
	}
	

}
