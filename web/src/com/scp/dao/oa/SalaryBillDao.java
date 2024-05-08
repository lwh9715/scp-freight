package com.scp.dao.oa;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.SalaryBill;



/**
* 
 * @author 
 */
@Component
public class SalaryBillDao extends BaseDaoImpl<SalaryBill, Long>{
	
	protected SalaryBillDao(Class<SalaryBill> persistancesClass) {
		super(persistancesClass);
	}

	public SalaryBillDao() {
		this(SalaryBill.class);
	}
}