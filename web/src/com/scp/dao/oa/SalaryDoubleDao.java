package com.scp.dao.oa;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.OaSalaryDouble;

/**
 * 
 * @author
 */
@Component
public class SalaryDoubleDao extends BaseDaoImpl<OaSalaryDouble, Long> {

	protected SalaryDoubleDao(Class<OaSalaryDouble> persistancesClass) {
		super(persistancesClass);
	}

	public SalaryDoubleDao() {
		this(OaSalaryDouble.class);
	}
}
