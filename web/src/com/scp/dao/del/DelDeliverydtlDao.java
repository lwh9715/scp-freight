package com.scp.dao.del;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.del.DelDeliverydtl;



/**
* 
 * @author sunny
 */
@Component
public class DelDeliverydtlDao extends BaseDaoImpl<DelDeliverydtl, Long>{
	
	protected DelDeliverydtlDao(Class<DelDeliverydtl> persistancesClass) {
		super(persistancesClass);
	}

	public DelDeliverydtlDao() {
		this(DelDeliverydtl.class);
	}
}