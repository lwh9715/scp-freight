package com.scp.service.sysmgr;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.sys.FavoritesDao;
import com.scp.model.sys.Favorites;


@Component
@Lazy(true)
public class FavoritesService {

	@Resource
	public FavoritesDao favoritesDao; 

	public void saveData(Favorites data) {
		if(0 == data.getId()){
			favoritesDao.create(data);
		}else{
			favoritesDao.modify(data);
		}
	}

	public void removeDate(long ids) {
		Favorites data = favoritesDao.findById(ids);
		if(data == null)return;
		favoritesDao.remove(data);
	} 
	/**
	 * 批量删除
	 * @param ids
	 */
	public void removeDate(String[] ids) {
		for(int i=0;i<ids.length;i++){
			long date = Long.parseLong(ids[i]);
			Favorites data =favoritesDao.findById(date);
			if(data == null)continue;
			favoritesDao.remove(data);
		}
		
	}

	
}
