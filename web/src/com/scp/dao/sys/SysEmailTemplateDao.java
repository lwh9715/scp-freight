package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysEmailTemplate;


@Component
public class SysEmailTemplateDao extends BaseDaoImpl<SysEmailTemplate, Long>{
	
	protected SysEmailTemplateDao(Class<SysEmailTemplate> persistancesClass) {
		super(persistancesClass);
	}

	public SysEmailTemplateDao() {
		this(SysEmailTemplate.class);
	}
}