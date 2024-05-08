package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.CsUser;



/**
* 
 * @author neo
 */
@Component
public class CsUserDao extends BaseDaoImpl<CsUser, Long>{
	
	protected CsUserDao(Class<CsUser> persistancesClass) {
		super(persistancesClass);
	}

	public CsUserDao() {
		this(CsUser.class);
	}
}