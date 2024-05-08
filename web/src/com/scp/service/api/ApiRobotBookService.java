package com.scp.service.api;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.api.ApiRobotBookDao;
import com.scp.model.api.ApiRobotBook;


@Component
@Lazy(true)
public class ApiRobotBookService {

	@Resource
	public ApiRobotBookDao apiRobotBookDao; 

	public void saveData(ApiRobotBook data) {
		if(0 == data.getId()){
			apiRobotBookDao.create(data);
		}else{
			apiRobotBookDao.modify(data);
		}
	}

	public void removeDate(long ids) {
		ApiRobotBook data = apiRobotBookDao.findById(ids);
		if(data == null)return;
		apiRobotBookDao.remove(data);
	} 
	/**
	 * 批量删除
	 * @param ids
	 */
	public void removeDate(String[] ids) {
		for(int i=0;i<ids.length;i++){
			long date = Long.parseLong(ids[i]);
			ApiRobotBook data = apiRobotBookDao.findById(date);
			if(data == null)continue;
			apiRobotBookDao.remove(data);
		}
	}
}
