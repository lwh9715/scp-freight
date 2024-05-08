package com.scp.dao.finance;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.finance.FinaBillCntdtl;



/**
* 
 * @author neo
 */
@Component
public class FinaBillCntdtlDao extends BaseDaoImpl<FinaBillCntdtl, Long>{
	
	protected FinaBillCntdtlDao(Class<FinaBillCntdtl> persistancesClass) {
		super(persistancesClass);
	}

	public FinaBillCntdtlDao() {
		this(FinaBillCntdtl.class);
	}
}