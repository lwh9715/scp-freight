package com.scp.service.api;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.api.ApiRobotBookPreDao;
import com.scp.model.api.ApiRobotBookPre;


@Component
@Lazy(true)
public class ApiRobotBookPreService {

	@Resource
	public ApiRobotBookPreDao apiRobotBookPreDao; 

	public void saveData(ApiRobotBookPre data) {
		if(0 == data.getId()){
			apiRobotBookPreDao.create(data);
		}else{
			apiRobotBookPreDao.modify(data);
		}
	}

	public void removeDate(long ids) {
		ApiRobotBookPre data = apiRobotBookPreDao.findById(ids);
		if(data == null)return;
		apiRobotBookPreDao.remove(data);
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
