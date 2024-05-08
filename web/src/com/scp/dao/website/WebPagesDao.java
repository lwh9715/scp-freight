package com.scp.dao.website;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.website.WebPages;

@Component
public class WebPagesDao extends BaseDaoImpl<WebPages, Long>{

	protected WebPagesDao(Class<WebPages> persistancesClass) {
		super(persistancesClass);
	}
	
	public WebPagesDao(){
		this(WebPages.class);
	}
}