package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysMessage;



/**
* 
 * @author neo
 */
@Component
public class SysMessageDao extends BaseDaoImpl<SysMessage, Long>{
	
	protected SysMessageDao(Class<SysMessage> persistancesClass) {
		super(persistancesClass);
	}

	public SysMessageDao() {
		this(SysMessage.class);
	}
}