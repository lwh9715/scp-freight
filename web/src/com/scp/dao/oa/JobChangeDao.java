package com.scp.dao.oa;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.OaJobChange;

/**
 * 
 * @author
 */
@Component
public class JobChangeDao extends BaseDaoImpl<OaJobChange, Long> {

	protected JobChangeDao(Class<OaJobChange> persistancesClass) {
		super(persistancesClass);
	}

	public JobChangeDao() {
		this(OaJobChange.class);
	}
}
