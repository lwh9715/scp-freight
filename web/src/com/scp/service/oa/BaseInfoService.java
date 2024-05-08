package com.scp.service.oa;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.BaseInfoDao;
import com.scp.model.oa.OaSalaryBaseinfo;

@Component
@Lazy(true)
public class BaseInfoService {

	@Resource
	public BaseInfoDao baseInfoDao;

	public void saveData(OaSalaryBaseinfo data) {
		if (0 == data.getId()) {
			baseInfoDao.create(data);
		} else {
			baseInfoDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		OaSalaryBaseinfo data = baseInfoDao.findById(id);
		baseInfoDao.remove(data);
	}

	public void removeModle(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			String sql = "\nUPDATE oa_salary_double SET isdelete = TRUE  WHERE id ='"
					+ ids[i] + "';";
			baseInfoDao.executeSQL(sql);
		}
	}

}
