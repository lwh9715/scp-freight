package com.scp.service.data;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.DaoIbatisTemplate;
import com.scp.dao.data.DatFeeitemDao;
import com.scp.model.data.DatFeeitem;

@Component
public class FeeItemMgrService{
	
	@Resource
	public DatFeeitemDao datFeeitemDao; 
	
	@Resource
	public DaoIbatisTemplate daoIbatisTemplate;

	public void saveData(DatFeeitem data) {
		if(0 == data.getId()){
			datFeeitemDao.create(data);
		}else{
			datFeeitemDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		DatFeeitem data = datFeeitemDao.findById(id);
		datFeeitemDao.remove(data);
	}

	public void updateExtBatch(String[] ids) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String id : ids) {
			stringBuilder.append("\nUPDATE dat_feeitem set isext = true where id = "+id+";");
		}
		this.datFeeitemDao.executeSQL(stringBuilder.toString());
	} 
}