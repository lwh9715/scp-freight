package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysLoginctrl;



/**
* 
 * @author neo
 */
@Component
public class SysLoginctrlDao extends BaseDaoImpl<SysLoginctrl, Long>{
	
	protected SysLoginctrlDao(Class<SysLoginctrl> persistancesClass) {
		super(persistancesClass);
	}

	public SysLoginctrlDao() {
		this(SysLoginctrl.class);
	}
}