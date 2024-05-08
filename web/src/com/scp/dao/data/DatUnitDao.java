package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatUnit;



/**
* 
 * @author neo
 */
@Component
public class DatUnitDao extends BaseDaoImpl<DatUnit, Long>{
	
	protected DatUnitDao(Class<DatUnit> persistancesClass) {
		super(persistancesClass);
	}

	public DatUnitDao() {
		this(DatUnit.class);
	}
}