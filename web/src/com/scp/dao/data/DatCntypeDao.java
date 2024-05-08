package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatCntype;



/**
* 
 * @author neo
 */
@Component
public class DatCntypeDao extends BaseDaoImpl<DatCntype, Long>{
	
	protected DatCntypeDao(Class<DatCntype> persistancesClass) {
		super(persistancesClass);
	}

	public DatCntypeDao() {
		this(DatCntype.class);
	}
}