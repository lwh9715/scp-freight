package com.scp.dao.oa;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.OaUserTransfer;

/**
 * 
 * @author
 */
@Component
public class OaUserTransferDao extends BaseDaoImpl<OaUserTransfer, Long> {

	protected OaUserTransferDao(Class<OaUserTransfer> persistancesClass) {
		super(persistancesClass);
	}

	public OaUserTransferDao() {
		this(OaUserTransfer.class);
	}
}
