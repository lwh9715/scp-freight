package com.scp.dao.price;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.price.PriceFclBargefeeDtl;

/**
 * @author hunk
 */
@Component
public class PriceFclBargefeeDtlDao extends BaseDaoImpl<PriceFclBargefeeDtl, Long> {

	protected PriceFclBargefeeDtlDao(Class<PriceFclBargefeeDtl> persistancesClass) {
		super(persistancesClass);
	}

	public PriceFclBargefeeDtlDao() {
		this(PriceFclBargefeeDtl.class);
	}

}
