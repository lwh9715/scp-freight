package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysCorporationApplyMonth;

@Component
public class SysCorporationApplyMonthDao extends BaseDaoImpl<SysCorporationApplyMonth, Long>{
	
	protected SysCorporationApplyMonthDao(Class<SysCorporationApplyMonth> persistancesClass) {
		super(persistancesClass);
	}

	public SysCorporationApplyMonthDao() {
		this(SysCorporationApplyMonth.class);
	}
}
