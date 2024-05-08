package com.scp.dao.wms;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.wms.WmsTrans;


@Component
public class WmsTransDao extends BaseDaoImpl<WmsTrans, Long>{
	
	protected WmsTransDao(Class<WmsTrans> persistancesClass) {
		super(persistancesClass);
	}

	public WmsTransDao() {
		this(WmsTrans.class);
	}
}