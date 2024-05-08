package com.scp.service.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatAccountDao;
import com.scp.dao.data.DatBankDao;
import com.scp.model.data.DatAccount;

@Component
public class AccountMgrService{
	
	@Resource
	public DatAccountDao datAccountDao; 
	
	@Resource
	public DatBankDao bankDao; 

	public void saveData(DatAccount data) {
		if(0 == data.getId()){
			datAccountDao.create(data);
		}else{
			datAccountDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		DatAccount data = datAccountDao.findById(id);
		datAccountDao.remove(data);
	} 
	
}
