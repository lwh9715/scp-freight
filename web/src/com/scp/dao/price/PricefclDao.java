package com.scp.dao.price;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.price.PriceFcl;

/**
 * @author bruce
 */
@Component
public class PricefclDao extends BaseDaoImpl<PriceFcl, Long> {

	protected PricefclDao(Class<PriceFcl> persistancesClass) {
		super(persistancesClass);
	}

	public PricefclDao() {
		this(PriceFcl.class);
	}

}
