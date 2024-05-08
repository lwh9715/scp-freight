package com.scp.service.oa;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.SalaryDoubleDao;
import com.scp.model.oa.OaSalaryDouble;

@Component
@Lazy(true)
public class SalaryDoubleService {

	@Resource
	public SalaryDoubleDao salaryDoubleDao;

	public void saveData(OaSalaryDouble data) {
		if (0 == data.getId()) {
			salaryDoubleDao.create(data);
		} else {
			salaryDoubleDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		OaSalaryDouble data = salaryDoubleDao.findById(id);
		salaryDoubleDao.remove(data);
	}
	
	public void removeModle(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			String sql = "\nUPDATE oa_salary_double SET isdelete = TRUE  WHERE id ='"
					+ ids[i] + "';";
			salaryDoubleDao.executeSQL(sql);
		}
	}

}
