package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusCustomsLink;



/**
* 
 * @author neo
 */
@Component
public class BusCustomsLinkDao extends BaseDaoImpl<BusCustomsLink, Long>{
	
	protected BusCustomsLinkDao(Class<BusCustomsLink> persistancesClass) {
		super(persistancesClass);
	}

	public BusCustomsLinkDao() {
		this(BusCustomsLink.class);
	}
}