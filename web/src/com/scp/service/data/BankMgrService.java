package com.scp.service.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatBankDao;
import com.scp.model.data.DatBank;

@Component
public class BankMgrService{
	
	@Resource
	public DatBankDao datBankDao; 

	public void saveData(DatBank data) {
		if(0 == data.getId()){
			datBankDao.create(data);
		}else{
			datBankDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		DatBank data = datBankDao.findById(id);
		datBankDao.remove(data);
	} 
	
}
