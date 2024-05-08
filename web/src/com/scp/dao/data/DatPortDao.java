package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatPort;



/**
* 
 * @author neo
 */
@Component
public class DatPortDao extends BaseDaoImpl<DatPort , Long>{
	
	protected DatPortDao(Class<DatPort> persistancesClass) {
		super(persistancesClass);
	}

	public DatPortDao() {
		this(DatPort.class);
	}
}