package com.scp.service.oa;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.WorkReportDao;
import com.scp.model.oa.OaWorkReport;

@Component
@Lazy(true)
public class WorkReportService {

	@Resource
	public WorkReportDao workReportDao;

	public void saveData(OaWorkReport data) {
		if (0 == data.getId()) {
			workReportDao.create(data);
		} else {
			workReportDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		OaWorkReport data = workReportDao.findById(id);
		workReportDao.remove(data);
	}
}
