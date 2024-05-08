package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysDepartment;



/**
* 
 * @author neo
 */
@Component
public class SysDepartmentDao extends BaseDaoImpl<SysDepartment, Long>{
	
	protected SysDepartmentDao(Class<SysDepartment> persistancesClass) {
		super(persistancesClass);
	}

	public SysDepartmentDao() {
		this(SysDepartment.class);
	}
}