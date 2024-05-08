/**
 * 
 */
package com.scp.service.data;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.model.data.Ediesi;

/**
 * @author Administrator
 *
 */
@Component
@Lazy(true)
public class EdiesiService {
	
	@Resource
	public com.scp.dao.data.EdiesiDao ediesiDao;
	
	public void saveData(Ediesi data) {
		if(0 == data.getId()){
			ediesiDao.create(data);
		}else{
			ediesiDao.modify(data);
		}
	}

	public void removeDate(long ids) {
		Ediesi data = ediesiDao.findById(ids);
		if(data == null)return;
		ediesiDao.remove(data);
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
