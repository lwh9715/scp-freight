package com.scp.service.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatProductDao;
import com.scp.model.data.DatProduct;

@Component
public class ProductMgrService {
	
	@Resource
	public DatProductDao datProductDao;

	public void saveData(DatProduct data) {
		if(0 == data.getId()){
			datProductDao.create(data);
		}else{
			datProductDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		DatProduct data = datProductDao.findById(id);
		datProductDao.remove(data);
	} 
}