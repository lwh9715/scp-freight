package com.scp.dao.oa;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.OaSalaryWelfare;

@Component
public class SalaryWelfareDao extends BaseDaoImpl<OaSalaryWelfare, Long> {

	protected SalaryWelfareDao(Class<OaSalaryWelfare> persistancesClass) {
		super(persistancesClass);
	}

	public SalaryWelfareDao() {
		this(OaSalaryWelfare.class);
	}
}
