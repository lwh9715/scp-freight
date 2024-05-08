package com.scp.dao.oa;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.OaUserProtocol;

/**
 * 
 * @author
 */
@Component
public class OaUserProtocolDao extends BaseDaoImpl<OaUserProtocol, Long> {

	protected OaUserProtocolDao(Class<OaUserProtocol> persistancesClass) {
		super(persistancesClass);
	}

	public OaUserProtocolDao() {
		this(OaUserProtocol.class);
	}
}
