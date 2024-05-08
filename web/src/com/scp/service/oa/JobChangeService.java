package com.scp.service.oa;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.JobChangeDao;
import com.scp.model.oa.OaJobChange;

@Component
@Lazy(true)
public class JobChangeService {

	@Resource
	public JobChangeDao jobChangeDao;

	public void saveData(OaJobChange data) {
		if (0 == data.getId()) {
			jobChangeDao.create(data);
		} else {
			jobChangeDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		OaJobChange data = jobChangeDao.findById(id);
		jobChangeDao.remove(data);
	}

	public void removeModle(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			String sql = "\nUPDATE oa_job_change SET isdelete = TRUE  WHERE id ='"
					+ ids[i] + "';";
			jobChangeDao.executeSQL(sql);
		}
	}

}
