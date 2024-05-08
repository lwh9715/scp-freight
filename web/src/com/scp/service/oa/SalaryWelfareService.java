package com.scp.service.oa;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.SalaryWelfareDao;
import com.scp.model.oa.OaSalaryWelfare;

@Component
@Lazy(true)
public class SalaryWelfareService {

	@Resource
	public SalaryWelfareDao salaryWelfareDao;

	public void saveData(OaSalaryWelfare data) {
		if (0 == data.getId()) {
			salaryWelfareDao.create(data);
		} else {
			salaryWelfareDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		OaSalaryWelfare data = salaryWelfareDao.findById(id);
		salaryWelfareDao.remove(data);
	}
	
	public void removeModle(String[] ids) {
		for (int i = 0; i < ids.length; i++) {
			String sql = "\nUPDATE oa_salary_welfare SET isdelete = TRUE  WHERE id ='"
					+ ids[i] + "';";
			salaryWelfareDao.executeSQL(sql);
		}
	}

}
