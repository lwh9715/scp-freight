package com.scp.service.finance;


import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.finance.FinaAgenBilldtlDao;
import com.scp.model.finance.FinaAgenBilldtl;


@Component
public class AgenBilldtlMgrService{
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	@Resource
	public FinaAgenBilldtlDao finaAgenBilldtlDao; 
	
	
	

	public void saveData(FinaAgenBilldtl data) {
		if(0 == data.getId()){
			finaAgenBilldtlDao.create(data);
		}else{
			finaAgenBilldtlDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		FinaAgenBilldtl data = finaAgenBilldtlDao.findById(id);
		finaAgenBilldtlDao.remove(data);
	} 
	
}
