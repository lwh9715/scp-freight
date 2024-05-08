package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysCorpmemory;



/**
* 
 * @author neo
 */
@Component
public class SysCorpmemoryDao extends BaseDaoImpl<SysCorpmemory, Long>{
	
	protected SysCorpmemoryDao(Class<SysCorpmemory> persistancesClass) {
		super(persistancesClass);
	}

	public SysCorpmemoryDao() {
		this(SysCorpmemory.class);
	}
}