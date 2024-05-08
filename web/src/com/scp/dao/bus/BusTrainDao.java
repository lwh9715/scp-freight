package com.scp.dao.bus;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bus.BusTrain;

/**
 * @author neo
 */
@Component
@Lazy(true)
public class BusTrainDao extends BaseDaoImpl<BusTrain, Long>{
	
	protected BusTrainDao(Class<BusTrain> persistancesClass) {
		super(persistancesClass);
	}

	public BusTrainDao() {
		this(BusTrain.class);
	}
	
	
}