package com.scp.service.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatPackageDao;
import com.scp.model.data.DatPackage;

@Component
public class PackageMgrService{
	
	@Resource
	public DatPackageDao datPackageDao;

	public void saveData(DatPackage data) {
		if(0 == data.getId()){
			datPackageDao.create(data);
		}else{
			datPackageDao.modify(data);
			
		}
	}

	public void removeDate(Long id) {
		DatPackage data = datPackageDao.findById(id);
		datPackageDao.remove(data);
	} 
	
}
