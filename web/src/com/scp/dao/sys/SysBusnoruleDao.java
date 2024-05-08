package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysBusnorule;



/**
* 
 * @author neo
 */
@Component
public class SysBusnoruleDao extends BaseDaoImpl<SysBusnorule, Long>{
	
	protected SysBusnoruleDao(Class<SysBusnorule> persistancesClass) {
		super(persistancesClass);
	}

	public SysBusnoruleDao() {
		this(SysBusnorule.class);
	}

	public void removeDate(Long id) {
		String sql = "UPDATE sys_busnorule SET isdelete = TRUE WHERE id = " + id + ";";
		this.executeSQL(sql);
	}
}