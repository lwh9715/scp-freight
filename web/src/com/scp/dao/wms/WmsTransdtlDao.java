package com.scp.dao.wms;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.wms.WmsTransdtl;


@Component
public class WmsTransdtlDao extends BaseDaoImpl<WmsTransdtl, Long>{
	
	protected WmsTransdtlDao(Class<WmsTransdtl> persistancesClass) {
		super(persistancesClass);
	}

	public WmsTransdtlDao() {
		this(WmsTransdtl.class);
	}
}