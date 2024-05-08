package com.scp.service.knowledge;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.knowledge.SysKnowledgelibDao;
import com.scp.model.knowledge.SysKnowledgelib;

@Component
public class SysKnowledgelibService{
	
	@Resource
	public SysKnowledgelibDao sysKnowledgelibDao; 

	public void saveData(SysKnowledgelib data) {
		if(0 == data.getId()){
			sysKnowledgelibDao.create(data);
		}else{
			sysKnowledgelibDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		SysKnowledgelib data = sysKnowledgelibDao.findById(id);
		sysKnowledgelibDao.remove(data);
	} 
	
}
