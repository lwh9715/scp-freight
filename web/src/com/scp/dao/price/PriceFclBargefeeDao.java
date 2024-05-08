package com.scp.dao.price;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.price.PriceFclBargefee;

/**
 * @author hunk
 */
@Component
public class PriceFclBargefeeDao extends BaseDaoImpl<PriceFclBargefee, Long> {

	protected PriceFclBargefeeDao(Class<PriceFclBargefee> persistancesClass) {
		super(persistancesClass);
	}

	public PriceFclBargefeeDao() {
		this(PriceFclBargefee.class);
	}

}
