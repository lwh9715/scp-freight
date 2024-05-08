package com.scp.dao.order;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.order.BusOrderdtl;

/**
 * @author bruce
 */
@Component
public class BusOrderdtlDao extends BaseDaoImpl<BusOrderdtl, Long> {

	protected BusOrderdtlDao(Class<BusOrderdtl> persistancesClass) {
		super(persistancesClass);
	}

	public BusOrderdtlDao() {
		this(BusOrderdtl.class);
	}
}
