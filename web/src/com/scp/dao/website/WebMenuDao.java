package com.scp.dao.website;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.website.WebMenu;

@Component
public class WebMenuDao extends BaseDaoImpl<WebMenu, Long>{

	protected WebMenuDao(Class<WebMenu> persistancesClass) {
		super(persistancesClass);
	}
	
	public WebMenuDao(){
		this(WebMenu.class);
	}
}