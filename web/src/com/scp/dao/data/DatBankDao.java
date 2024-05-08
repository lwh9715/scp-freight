package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatBank;



/**
* 
 * @author neo
 */
@Component
public class DatBankDao extends BaseDaoImpl<DatBank, Long>{
	
	protected DatBankDao(Class<DatBank> persistancesClass) {
		super(persistancesClass);
	}

	public DatBankDao() {
		this(DatBank.class);
	}
}