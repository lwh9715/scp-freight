package com.scp.dao.knowledge;

import org.springframework.stereotype.Component;

import com.scp.dao.BaseDaoImpl;
import com.scp.model.knowledge.SysKnowledgelib;



/**
* 
 * @author neo
 */
@Component
public class SysKnowledgelibDao extends BaseDaoImpl<SysKnowledgelib, Long>{
	
	protected SysKnowledgelibDao(Class<SysKnowledgelib> persistancesClass) {
		super(persistancesClass);
	}

	public SysKnowledgelibDao() {
		this(SysKnowledgelib.class);
	}
}