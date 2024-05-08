package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatCar;



/**
* 
 * @author neo
 */
@Component
public class DatCarDao extends BaseDaoImpl<DatCar, Long>{
	
	protected DatCarDao(Class<DatCar> persistancesClass) {
		super(persistancesClass);
	}

	public DatCarDao() {
		this(DatCar.class);
	}
}