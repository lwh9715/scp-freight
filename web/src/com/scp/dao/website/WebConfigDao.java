package com.scp.dao.website;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.website.WebConfig;

@Component
public class WebConfigDao extends BaseDaoImpl<WebConfig, Long>{

	protected WebConfigDao(Class<WebConfig> persistancesClass) {
		super(persistancesClass);
	}
	
	public WebConfigDao(){
		this(WebConfig.class);
	}
}