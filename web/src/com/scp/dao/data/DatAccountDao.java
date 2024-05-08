package com.scp.dao.data;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.data.DatAccount;



/**
* 
 * @author neo
 */
@Component
public class DatAccountDao extends BaseDaoImpl<DatAccount, Long>{
	
	protected DatAccountDao(Class<DatAccount> persistancesClass) {
		super(persistancesClass);
	}

	public DatAccountDao() {
		this(DatAccount.class);
	}
}