package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatWarehouseLoc;



/**
* 
 * @author neo
 */
@Component
public class DatWarehouseLocDao extends BaseDaoImpl<DatWarehouseLoc, Long>{
	
	protected DatWarehouseLocDao(Class<DatWarehouseLoc> persistancesClass) {
		super(persistancesClass);
	}

	public DatWarehouseLocDao() {
		this(DatWarehouseLoc.class);
	}
}