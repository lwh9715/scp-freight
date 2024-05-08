package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatWarehouse;



/**
* 
 * @author neo
 */
@Component
public class DatWarehouseDao extends BaseDaoImpl<DatWarehouse, Long>{
	
	protected DatWarehouseDao(Class<DatWarehouse> persistancesClass) {
		super(persistancesClass);
	}

	public DatWarehouseDao() {
		this(DatWarehouse.class);
	}
}