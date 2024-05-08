package com.scp.service.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatCurrencyDao;
import com.scp.model.data.DatCurrency;

@Component
public class CurrencyMgrService{
	
	@Resource
	public DatCurrencyDao datCurrencyDao; 

	public void saveData(DatCurrency data) {
		if(0 == data.getId()){
			datCurrencyDao.create(data);
		}else{
			datCurrencyDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		DatCurrency data = datCurrencyDao.findById(id);
		datCurrencyDao.remove(data);
	} 
	
}
