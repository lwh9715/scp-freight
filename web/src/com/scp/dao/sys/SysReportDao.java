package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysReport;



/**
* 
 * @author neo
 */
@Component
public class SysReportDao extends BaseDaoImpl<SysReport, Long>{
	
	protected SysReportDao(Class<SysReport> persistancesClass) {
		super(persistancesClass);
	}

	public SysReportDao() {
		this(SysReport.class);
	}
}