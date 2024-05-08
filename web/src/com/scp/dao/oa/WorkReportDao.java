package com.scp.dao.oa;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.OaWorkReport;

/**
 * 
 * @author kai
 */
@Component
public class WorkReportDao extends BaseDaoImpl<OaWorkReport, Long> {

	protected WorkReportDao(Class<OaWorkReport> persistancesClass) {
		super(persistancesClass);
	}

	public WorkReportDao() {
		this(OaWorkReport.class);
	}
}
