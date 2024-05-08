package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysCorporation;



/**
* 
 * @author neo
 */
@Component
public class SysCorporationDao extends BaseDaoImpl<SysCorporation, Long>{
	
	protected SysCorporationDao(Class<SysCorporation> persistancesClass) {
		super(persistancesClass);
	}

	public SysCorporationDao() {
		this(SysCorporation.class);
	}
}