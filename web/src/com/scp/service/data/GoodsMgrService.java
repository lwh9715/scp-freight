package com.scp.service.data;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatGoodsDao;
import com.scp.model.data.DatGoods;

@Component
public class GoodsMgrService{
	

	
	
	@Resource
	public DatGoodsDao datGoodsDao; 

	public void saveData(DatGoods data) {
		if(0 == data.getId()){
			datGoodsDao.create(data);
		}else{
			datGoodsDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		DatGoods data = datGoodsDao.findById(id);
		datGoodsDao.remove(data);
	} 
	
	
	
}