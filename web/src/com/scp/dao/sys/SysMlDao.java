package com.scp.dao.sys;

import java.util.List;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.sys.SysMl;



/**
* 
 * @author neo
 */
@Component
public class SysMlDao extends BaseDaoImpl<SysMl, Long>{
	
	protected SysMlDao(Class<SysMl> persistancesClass) {
		super(persistancesClass);
	}

	public SysMlDao() {
		this(SysMl.class);
	}
	
	
	public void insertDate(String ch) {
		String sql = "INSERT INTO sys_ml(id,ch)VALUES (getid(), '"+ch+"');";
		this.executeSQL(sql);
	}
	
	public List<SysMl> findbych(String ch) {
		return findAllByClauseWhere("ch = '"+ch+"'");
	}
	
}


