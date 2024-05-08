package com.scp.service.data;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.data.DatLineDao;
import com.scp.model.data.DatLine;

@Component
public class LineMgrService{
	
	@Resource
	public DatLineDao datLineDao; 

	public void saveData(DatLine data) {
		if(0 == data.getId()){
			datLineDao.create(data);
		}else{
			datLineDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		DatLine data = datLineDao.findById(id);
		if(data == null)return;
		datLineDao.remove(data);
	} 
	
	public void removeDateIsdelete(Long id) {
		DatLine data = datLineDao.findById(id);
		if(data == null)return;
		data.setIsdelete(true);
		datLineDao.modify(data);
	} 
}
