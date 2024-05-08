package com.scp.dao.bus;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bus.AirBookcode;

@Component
@Lazy(true)
public class AirBookcodeDao extends BaseDaoImpl<AirBookcode, Long>{
	
	protected AirBookcodeDao(Class<AirBookcode> persistancesClass) {
		super(persistancesClass);
	}

	public AirBookcodeDao() {
		this(AirBookcode.class);
	}
	
	
}