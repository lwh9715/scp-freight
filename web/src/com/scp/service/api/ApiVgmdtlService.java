package com.scp.service.api;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.api.ApiVgmdtlDao;
import com.scp.model.api.ApiVgmdtl;


@Component
@Lazy(true)
public class ApiVgmdtlService {
	
	@Resource
	public ApiVgmdtlDao ApiVgmdtlDao; 
	
	public void saveData(ApiVgmdtl data) {
		if(0 == data.getId()){
			ApiVgmdtlDao.create(data);
		}else{
			ApiVgmdtlDao.modify(data);
		}
	}

	public void removeDate(long ids) {
		ApiVgmdtl data = ApiVgmdtlDao.findById(ids);
		if(data == null)return;
		ApiVgmdtlDao.remove(data);
	} 
	/**
	 * 批量删除
	 * @param ids
	 */
	public void removeDate(String[] ids) {
		for(int i=0;i<ids.length;i++){
			long date = Long.parseLong(ids[i]);
			removeDate(date);
		}
		
	}
}
