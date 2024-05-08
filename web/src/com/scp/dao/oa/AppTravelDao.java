package com.scp.dao.oa;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.oa.AppTravel;

/**
 * 
 * @author
 */
@Component
public class AppTravelDao extends BaseDaoImpl<AppTravel, Long> {

	protected AppTravelDao(Class<AppTravel> persistancesClass) {
		super(persistancesClass);
	}

	public AppTravelDao() {
		this(AppTravel.class);
	}
}
