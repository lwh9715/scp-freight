package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatFeeitem;



/**
* 
 * @author neo
 */
@Component
public class DatFeeitemDao extends BaseDaoImpl<DatFeeitem, Long>{
	
	protected DatFeeitemDao(Class<DatFeeitem> persistancesClass) {
		super(persistancesClass);
	}

	public DatFeeitemDao() {
		this(DatFeeitem.class);
	}
}