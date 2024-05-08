/**
 * 
 */
package com.scp.service.ship;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.ship.VoyageDao;
import com.scp.model.ship.Voyage;

/**
 * @author Administrator
 *
 */
@Component
@Lazy(true)
public class VoyageService {
	
	@Resource
	public VoyageDao voyageDao;
	
	public void saveData(Voyage data) {
		if(0 == data.getId()){
			voyageDao.create(data);
		}else{
			voyageDao.modify(data);
		}
	}

	public void removeDate(long ids) {
		Voyage data = voyageDao.findById(ids);
		if(data == null)return;
		voyageDao.remove(data);
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
