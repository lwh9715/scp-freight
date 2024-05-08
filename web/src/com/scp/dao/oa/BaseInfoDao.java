package com.scp.dao.oa;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.OaSalaryBaseinfo;

/**
 * 
 * @author
 */
@Component
public class BaseInfoDao extends BaseDaoImpl<OaSalaryBaseinfo, Long> {

	protected BaseInfoDao(Class<OaSalaryBaseinfo> persistancesClass) {
		super(persistancesClass);
	}

	public BaseInfoDao() {
		this(OaSalaryBaseinfo.class);
	}
}
