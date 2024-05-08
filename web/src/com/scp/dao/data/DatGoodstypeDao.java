package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatGoodstype;



/**
* 
 * @author neo
 */
@Component
public class DatGoodstypeDao extends BaseDaoImpl<DatGoodstype, Long>{
	
	protected DatGoodstypeDao(Class<DatGoodstype> persistancesClass) {
		super(persistancesClass);
	}

	public DatGoodstypeDao() {
		this(DatGoodstype.class);
	}
}