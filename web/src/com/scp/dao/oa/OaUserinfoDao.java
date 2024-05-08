package com.scp.dao.oa;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.OaUserinfo;

/**
 * 
 * @author
 */
@Component
public class OaUserinfoDao extends BaseDaoImpl<OaUserinfo, Long> {

	protected OaUserinfoDao(Class<OaUserinfo> persistancesClass) {
		super(persistancesClass);
	}

	public OaUserinfoDao() {
		this(OaUserinfo.class);
	}
}
