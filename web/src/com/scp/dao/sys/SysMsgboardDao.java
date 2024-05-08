package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysMsgboard;



/**
* 
 * @author neo
 */
@Component
public class SysMsgboardDao extends BaseDaoImpl<SysMsgboard, Long>{
	
	protected SysMsgboardDao(Class<SysMsgboard> persistancesClass) {
		super(persistancesClass);
	}

	public SysMsgboardDao() {
		this(SysMsgboard.class);
	}
}