package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysFaq;



/**
* 
 * @author neo
 */
@Component
public class SysFaqDao extends BaseDaoImpl<SysFaq, Long>{
	
	protected SysFaqDao(Class<SysFaq> persistancesClass) {
		super(persistancesClass);
	}

	public SysFaqDao() {
		this(SysFaq.class);
	}
}