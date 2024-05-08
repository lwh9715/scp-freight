package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.EdiMap;



/**
* 
 * @author neo
 */
@Component
public class EdiMapDao extends BaseDaoImpl<EdiMap, Long>{
	
	protected EdiMapDao(Class<EdiMap> persistancesClass) {
		super(persistancesClass);
	}

	public EdiMapDao() {
		this(EdiMap.class);
	}
}