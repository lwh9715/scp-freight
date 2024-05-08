package com.scp.service.oa;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.oa.SalaryBillDao;
import com.scp.model.oa.SalaryBill;

@Component
@Lazy(true)
public class SalaryBillMgrService {

	
	@Resource
	public SalaryBillDao salaryBillDao;

	public void saveData(SalaryBill data) {
		if(0 == data.getId()){
			salaryBillDao.create(data);
		}else{
			salaryBillDao.modify(data);
		}
	}
	
	public void removeDate(Long id) {
		SalaryBill data = salaryBillDao.findById(id);
		salaryBillDao.remove(data);
	}

	

}