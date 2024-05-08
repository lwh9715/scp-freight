package com.scp.service.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatLinecodeDao;
import com.scp.model.data.DatLinecode;

@Component
public class LinecodeMgrService{
	
	@Resource
	public DatLinecodeDao datLinecodeDao; 

	public void saveData(DatLinecode data) {
		if(0 == data.getId()){
			datLinecodeDao.create(data);
		}else{
			datLinecodeDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		DatLinecode data = datLinecodeDao.findById(id);
		if(data == null)return;
		datLinecodeDao.remove(data);
	} 
	
}
