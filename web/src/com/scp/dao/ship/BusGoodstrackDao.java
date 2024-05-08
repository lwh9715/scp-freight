package com.scp.dao.ship;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.BusGoodstrack;



/**
* 
 * @author Neo
 */
@Component
public class BusGoodstrackDao extends BaseDaoImpl<BusGoodstrack, Long>{
	
	protected BusGoodstrackDao(Class<BusGoodstrack> persistancesClass) {
		super(persistancesClass);
	}

	public BusGoodstrackDao() {
		this(BusGoodstrack.class);
	}

	
}
