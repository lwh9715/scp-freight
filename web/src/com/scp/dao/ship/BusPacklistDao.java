package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusPacklist;

/**
* 
 * @author Yves
 */
@Component
public class BusPacklistDao extends BaseDaoImpl<BusPacklist, Long>{
	
	protected BusPacklistDao(Class<BusPacklist> persistancesClass) {
		super(persistancesClass);
	}

	public BusPacklistDao() {
		this(BusPacklist.class);
	}
}