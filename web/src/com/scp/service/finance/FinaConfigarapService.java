package com.scp.service.finance;


import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.finance.FinaConfigarapDao;
import com.scp.model.finance.FinaConfigarap;


@Component
public class FinaConfigarapService{
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;
	
	@Resource
	public FinaConfigarapDao finaConfigarapDao; 
	
	

	public void saveData(FinaConfigarap data) {
		if(0 == data.getId()){
			finaConfigarapDao.create(data);
		}else{
			finaConfigarapDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		FinaConfigarap data = finaConfigarapDao.findById(id);
		finaConfigarapDao.remove(data);
	} 
	
}
