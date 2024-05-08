package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatLinecode;


/**
* 
 * @author neo
 */
@Component
public class DatLinecodeDao extends BaseDaoImpl<DatLinecode, Long>{
	
	protected DatLinecodeDao(Class<DatLinecode> persistancesClass) {
		super(persistancesClass);
	}

	public DatLinecodeDao() {
		this(DatLinecode.class);
	}
}