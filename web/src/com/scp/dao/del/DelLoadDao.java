package com.scp.dao.del;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.del.DelLoad;



/**
* 
 * @author sunny
 */
@Component
public class DelLoadDao extends BaseDaoImpl<DelLoad, Long>{
	
	protected DelLoadDao(Class<DelLoad> persistancesClass) {
		super(persistancesClass);
	}

	public DelLoadDao() {
		this(DelLoad.class);
	}
}