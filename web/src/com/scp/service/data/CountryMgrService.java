package com.scp.service.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatCountryDao;
import com.scp.model.data.DatCountry;

@Component
public class CountryMgrService{
	
	@Resource
	public DatCountryDao datCountryDao; 

	public void saveData(DatCountry data) {
		if(0 == data.getId()){
			datCountryDao.create(data);
		}else{
			datCountryDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		DatCountry data = datCountryDao.findById(id);
		datCountryDao.remove(data);
	} 
	
}
