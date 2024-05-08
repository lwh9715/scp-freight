package com.scp.service.data;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatGoodstypeDao;
import com.scp.model.data.DatGoodstype;

@Component
public class GoodstypeMgrService{
	
	@Resource
	public DatGoodstypeDao datGoodstypeDao; 

	public void saveData(DatGoodstype data) {
		if(0 == data.getId()){
			datGoodstypeDao.create(data);
		}else{
			datGoodstypeDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		DatGoodstype data = datGoodstypeDao.findById(id);
		datGoodstypeDao.remove(data);
	} 
}