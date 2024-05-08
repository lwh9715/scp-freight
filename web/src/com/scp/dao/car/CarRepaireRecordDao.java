package com.scp.dao.car;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.car.CarRepaireRecord;



/**
* 
 * @author sunny
 */
@Component
public class CarRepaireRecordDao extends BaseDaoImpl<CarRepaireRecord, Long>{
	
	protected CarRepaireRecordDao(Class<CarRepaireRecord> persistancesClass) {
		super(persistancesClass);
	}

	public CarRepaireRecordDao() {
		this(CarRepaireRecord.class);
	}
}