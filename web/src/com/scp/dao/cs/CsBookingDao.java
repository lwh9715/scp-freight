package com.scp.dao.cs;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.view.module.cs.CsBooking;

/**
 * @author neo
 */
@Component
@Lazy(true)
public class CsBookingDao extends BaseDaoImpl<CsBooking, Long>{
	
	protected CsBookingDao(Class<CsBooking> persistancesClass) {
		super(persistancesClass);
	}

	public CsBookingDao() {
		this(CsBooking.class);
	}
	
	
}