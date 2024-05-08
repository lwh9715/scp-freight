package com.scp.dao.oa;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.OaFee;

/**
 * 
 * @author
 */
@Component
public class OaFeeDao extends BaseDaoImpl<OaFee, Long> {

	protected OaFeeDao(Class<OaFee> persistancesClass) {
		super(persistancesClass);
	}

	public OaFeeDao() {
		this(OaFee.class);
	}
}
