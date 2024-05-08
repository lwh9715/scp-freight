package com.scp.dao.car;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.car.CarOliRecord;



/**
* 
 * @author sunny
 */
@Component
public class CarOliRecordDao extends BaseDaoImpl<CarOliRecord, Long>{
	
	protected CarOliRecordDao(Class<CarOliRecord> persistancesClass) {
		super(persistancesClass);
	}

	public CarOliRecordDao() {
		this(CarOliRecord.class);
	}
}