package com.scp.dao.oa;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.OaSalaryTable;

/**
 * 
 * @author
 */
@Component
public class SalaryTableDao extends BaseDaoImpl<OaSalaryTable, Long> {

	protected SalaryTableDao(Class<OaSalaryTable> persistancesClass) {
		super(persistancesClass);
	}

	public SalaryTableDao() {
		this(OaSalaryTable.class);
	}
}
