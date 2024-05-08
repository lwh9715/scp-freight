package com.scp.dao.salesmgr;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.salesmgr.BlackList;



@Component
public class BlackListDao extends BaseDaoImpl<BlackList, Long>{
	
	protected BlackListDao(Class<BlackList> persistancesClass) {
		super(persistancesClass);
	}

	public BlackListDao() {
		this(BlackList.class);
	}
}
