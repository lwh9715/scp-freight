package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.PriceFclAsk;

/**
 * 
 * @author Neo
 */

@Component
public class PriceFclASKDao extends BaseDaoImpl<PriceFclAsk, Long> {
	
	protected PriceFclASKDao(Class<PriceFclAsk> persistancesClass) {
		super(persistancesClass);
	}

	public PriceFclASKDao() {
		this(PriceFclAsk.class);
	}
}
