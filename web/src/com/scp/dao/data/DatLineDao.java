package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatLine;



/**
* 
 * @author neo
 */
@Component
public class DatLineDao extends BaseDaoImpl<DatLine, Long>{
	
	protected DatLineDao(Class<DatLine> persistancesClass) {
		super(persistancesClass);
	}

	public DatLineDao() {
		this(DatLine.class);
	}
}