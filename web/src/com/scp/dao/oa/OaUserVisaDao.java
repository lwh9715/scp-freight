package com.scp.dao.oa;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.OaUserVisa;

/**
 * 
 * @author
 */
@Component
public class OaUserVisaDao extends BaseDaoImpl<OaUserVisa, Long> {

	protected OaUserVisaDao(Class<OaUserVisa> persistancesClass) {
		super(persistancesClass);
	}

	public OaUserVisaDao() {
		this(OaUserVisa.class);
	}
}
