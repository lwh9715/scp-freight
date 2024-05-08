package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysReport;



/**
* 
 * @author neo
 */
@Component
public class SysReportConfigDao extends BaseDaoImpl<SysReport, Long>{
	
	protected SysReportConfigDao(Class<SysReport> persistancesClass) {
		super(persistancesClass);
	}

	public SysReportConfigDao() {
		this(SysReport.class);
	}
}