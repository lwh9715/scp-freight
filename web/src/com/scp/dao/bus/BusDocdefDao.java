package com.scp.dao.bus;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bus.BusDocdef;

/**
 * @author neo
 */
@Component
@Lazy(true)
public class BusDocdefDao extends BaseDaoImpl<BusDocdef, Long>{
	
	protected BusDocdefDao(Class<BusDocdef> persistancesClass) {
		super(persistancesClass);
	}

	public BusDocdefDao() {
		this(BusDocdef.class);
	}
}