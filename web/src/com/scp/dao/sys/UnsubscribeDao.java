package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.Unsubscribe;

/**
* 
 * @author Luffy
 */
@Component
public class UnsubscribeDao extends BaseDaoImpl<Unsubscribe, Long>{

	protected UnsubscribeDao(Class<Unsubscribe> persistancesClass) {
		super(persistancesClass);
	}

	public UnsubscribeDao() {
		this(Unsubscribe.class);
	}

	

}
