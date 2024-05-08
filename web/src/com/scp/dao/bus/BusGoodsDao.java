package com.scp.dao.bus;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bus.BusGoods;



/**
* 
 * @author neo
 */
@Component
public class BusGoodsDao extends BaseDaoImpl<BusGoods, Long>{
	
	protected BusGoodsDao(Class<BusGoods> persistancesClass) {
		super(persistancesClass);
	}

	public BusGoodsDao() {
		this(BusGoods.class);
	}
}