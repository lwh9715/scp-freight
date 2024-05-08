package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatFiledata;



/**
* 
 * @author neo
 */
@Component
public class DatFiledataDao extends BaseDaoImpl<DatFiledata, Long>{
	
	protected DatFiledataDao(Class<DatFiledata> persistancesClass) {
		super(persistancesClass);
	}

	public DatFiledataDao() {
		this(DatFiledata.class);
	}

	
}