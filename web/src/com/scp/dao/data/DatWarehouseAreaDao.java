package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatWarehouseArea;



/**
* 
 * @author neo
 */
@Component
public class DatWarehouseAreaDao extends BaseDaoImpl<DatWarehouseArea, Long>{
	
	protected DatWarehouseAreaDao(Class<DatWarehouseArea> persistancesClass) {
		super(persistancesClass);
	}

	public DatWarehouseAreaDao() {
		this(DatWarehouseArea.class);
	}
}