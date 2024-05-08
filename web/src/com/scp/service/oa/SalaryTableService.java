package com.scp.service.oa;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.SalaryTableDao;
import com.scp.model.oa.OaSalaryTable;

@Component
@Lazy(true)
public class SalaryTableService {

	@Resource
	public SalaryTableDao salaryTableDao;

	public void saveData(OaSalaryTable data) {
		if (0 == data.getId()) {
			salaryTableDao.create(data);
		} else {
			salaryTableDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		OaSalaryTable data = salaryTableDao.findById(id);
		salaryTableDao.remove(data);
	}

	public void removeModle(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			String sql = "\nUPDATE oa_salary_table SET isdelete = TRUE  WHERE id ='"
					+ ids[i] + "';";
			salaryTableDao.executeSQL(sql);
		}
	}

}
