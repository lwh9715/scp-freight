package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatCartype;



/**
* 
 * @author neo
 */
@Component
public class DatCartypeDao extends BaseDaoImpl<DatCartype, Long>{
	
	protected DatCartypeDao(Class<DatCartype> persistancesClass) {
		super(persistancesClass);
	}

	public DatCartypeDao() {
		this(DatCartype.class);
	}
}