package com.scp.dao.del;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.del.DelLoadtl;



/**
* 
 * @author sunny
 */
@Component
public class DelLoadtlDao extends BaseDaoImpl<DelLoadtl, Long>{
	
	protected DelLoadtlDao(Class<DelLoadtl> persistancesClass) {
		super(persistancesClass);
	}

	public DelLoadtlDao() {
		this(DelLoadtl.class);
	}
}