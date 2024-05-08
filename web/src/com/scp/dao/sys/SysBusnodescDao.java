package com.scp.dao.sys;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysBusnodesc;



/**
* 
 * @author neo
 */
@Component
public class SysBusnodescDao extends BaseDaoImpl<SysBusnodesc, Long>{
	
	protected SysBusnodescDao(Class<SysBusnodesc> persistancesClass) {
		super(persistancesClass);
	}

	public SysBusnodescDao() {
		this(SysBusnodesc.class);
	}

	public void removeDate(Long id) {
		String sql = "UPDATE sys_busnodesc SET isdelete = TRUE WHERE id = " + id + ";";
		this.executeSQL(sql);
	}
}