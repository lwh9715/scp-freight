package com.scp.service.data;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.data.DatWarehouseAreaDao;
import com.scp.dao.data.DatWarehouseDao;
import com.scp.model.data.DatWarehouse;
import com.scp.model.data.DatWarehouseArea;

@Component
public class WarehouseareaMgrService{
	
	@Resource
	public DatWarehouseAreaDao warehouseAreaDao; 
	
	@Resource
	public DatWarehouseDao warehouseDao; 
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	public void saveMasterData(DatWarehouse data) {
		if(0 == data.getId()){
			warehouseDao.create(data);
			
		}else{
			warehouseDao.modify(data);
		}
	}
	
	public void saveDtlData(DatWarehouseArea data) {
		
		if(0 == data.getId()){
			
			warehouseAreaDao.create(data);
			
		}else{
			warehouseAreaDao.modify(data);
		}
	}
	
	public void removeDate(Long id) {
		String sql = "UPDATE dat_warehouse set isdelete = TRUE WHERE id = " + id +";";
		warehouseDao.executeSQL(sql);
	}
	
	
	
}