package com.scp.dao.website;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.website.WebNews;

@Component
public class WebNewsDao extends BaseDaoImpl<WebNews, Long>{

	protected WebNewsDao(Class<WebNews> persistancesClass) {
		super(persistancesClass);
	}
	
	public WebNewsDao(){
		this(WebNews.class);
	}
}