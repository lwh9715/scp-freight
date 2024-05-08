package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatPackage;



/**
* 
 * @author neo
 */
@Component
public class DatPackageDao extends BaseDaoImpl<DatPackage , Long>{
	
	protected DatPackageDao(Class<DatPackage> persistancesClass) {
		super(persistancesClass);
	}

	public DatPackageDao() {
		this(DatPackage.class);
	}
}