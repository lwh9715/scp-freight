package com.scp.dao.oa;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.OaUserContract;

/**
 * 
 * @author
 */
@Component
public class OaUserContractDao extends BaseDaoImpl<OaUserContract, Long> {

	protected OaUserContractDao(Class<OaUserContract> persistancesClass) {
		super(persistancesClass);
	}

	public OaUserContractDao() {
		this(OaUserContract.class);
	}
}
