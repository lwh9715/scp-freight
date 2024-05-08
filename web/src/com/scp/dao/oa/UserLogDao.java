package com.scp.dao.oa;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.OaUserLog;
/**
 * 
 * @author
 */
@Component
public class UserLogDao extends BaseDaoImpl<OaUserLog, Long> {
	protected UserLogDao(Class<OaUserLog> persistancesClass) {
		super(persistancesClass);
	}
	public UserLogDao() {
		this(OaUserLog.class);
	}
}
