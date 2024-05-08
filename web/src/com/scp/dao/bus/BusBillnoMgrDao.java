package com.scp.dao.bus;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.bus.BusBillnoMgr;

@Component
@Lazy(true)
public class BusBillnoMgrDao extends BaseDaoImpl<BusBillnoMgr, Long>{
	
	protected BusBillnoMgrDao(Class<BusBillnoMgr> persistancesClass) {
		super(persistancesClass);
	}

	public BusBillnoMgrDao() {
		this(BusBillnoMgr.class);
	}
}
