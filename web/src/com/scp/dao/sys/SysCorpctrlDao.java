package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysCorpctrl;



/**
* 
 * @author neo
 */
@Component
public class SysCorpctrlDao extends BaseDaoImpl<SysCorpctrl, Long>{
	
	protected SysCorpctrlDao(Class<SysCorpctrl> persistancesClass) {
		super(persistancesClass);
	}

	public SysCorpctrlDao() {
		this(SysCorpctrl.class);
	}
}