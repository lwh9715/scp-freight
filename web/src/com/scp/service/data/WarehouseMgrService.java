package com.scp.service.data;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatWarehouseDao;
import com.scp.model.data.DatWarehouse;

@Component
public class WarehouseMgrService{
	
	@Resource
	public DatWarehouseDao datWarehouseDao; 

	public void saveData( DatWarehouse data) {
		if(0 == data.getId()){
			datWarehouseDao.create(data);
		}else{
			datWarehouseDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		 DatWarehouse data = datWarehouseDao.findById(id);
		datWarehouseDao.remove(data);
	} 
}