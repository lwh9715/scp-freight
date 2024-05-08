package com.scp.dao.website;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.website.WebRegister;

@Component
public class WebRegisterDao extends BaseDaoImpl<WebRegister, Long>{

	protected WebRegisterDao(Class<WebRegister> persistancesClass) {
		super(persistancesClass);
	}
	
	public WebRegisterDao(){
		this(WebRegister.class);
	}
}