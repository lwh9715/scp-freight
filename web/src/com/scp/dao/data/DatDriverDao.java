package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatDriver;



/**
* 
 * @author neo
 */
@Component
public class DatDriverDao extends BaseDaoImpl<DatDriver, Long>{
	
	protected DatDriverDao(Class<DatDriver> persistancesClass) {
		super(persistancesClass);
	}

	public DatDriverDao() {
		this(DatDriver.class);
	}
}