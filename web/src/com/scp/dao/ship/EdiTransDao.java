package com.scp.dao.ship;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.ship.EdiTrans;




@Component
public class EdiTransDao extends BaseDaoImpl<EdiTrans, Long>{
	
	protected EdiTransDao(Class<EdiTrans> persistancesClass) {
		super(persistancesClass);
	}

	public EdiTransDao() {
		this(EdiTrans.class);
	}
}