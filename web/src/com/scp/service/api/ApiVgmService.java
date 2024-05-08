/**
 * 
 */
package com.scp.service.api;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.api.ApiVgmDao;
import com.scp.model.api.ApiVgm;

/**
 * @author Administrator
 *
 */
@Component
@Lazy(true)
public class ApiVgmService {
	
	@Resource
	public ApiVgmDao ApiVgmDao; 
	
	public void saveData(ApiVgm data) {
		if(0 == data.getId()){
			ApiVgmDao.create(data);
		}else{
			ApiVgmDao.modify(data);
		}
	}

	public void removeDate(long ids) {
		ApiVgm data = ApiVgmDao.findById(ids);
		if(data == null)return;
		ApiVgmDao.remove(data);
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
