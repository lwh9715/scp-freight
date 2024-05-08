package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatGoods;



/**
* 
 * @author neo
 */
@Component
public class DatGoodsDao extends BaseDaoImpl<DatGoods, Long>{
	
	protected DatGoodsDao(Class<DatGoods> persistancesClass) {
		super(persistancesClass);
	}

	public DatGoodsDao() {
		this(DatGoods.class);
	}
}