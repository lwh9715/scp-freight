package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysCorpvisit;

/**
 * 
 * @author Neo
 */
@Component
public class CorpvisitDao extends BaseDaoImpl<SysCorpvisit, Long> {

	protected CorpvisitDao(Class<SysCorpvisit> persistancesClass) {
		super(persistancesClass);
	}

	public CorpvisitDao() {
		this(SysCorpvisit.class);
	}
}
