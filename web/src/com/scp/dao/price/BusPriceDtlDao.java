package com.scp.dao.price;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.price.BusPriceDtl;

@Component
public class BusPriceDtlDao extends BaseDaoImpl<BusPriceDtl, Long>{

	protected BusPriceDtlDao(Class<BusPriceDtl> persistancesClass) {
		super(persistancesClass);
	}
	
	public BusPriceDtlDao(){
		this(BusPriceDtl.class);
	}
}