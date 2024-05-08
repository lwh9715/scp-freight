package com.scp.service.api;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.api.ApiRobotEsiDao;
import com.scp.model.api.ApiRobotEsi;


@Component
@Lazy(true)
public class ApiRobotEsiService {

	@Resource
	public ApiRobotEsiDao apiRobotEsiDao; 

	public void saveData(ApiRobotEsi data) {
		if(0 == data.getId()){
			apiRobotEsiDao.create(data);
		}else{
			apiRobotEsiDao.modify(data);
		}
	}

	public void removeDate(long ids) {
		ApiRobotEsi data = apiRobotEsiDao.findById(ids);
		if(data == null)return;
		apiRobotEsiDao.remove(data);
	} 
	/**
	 * 批量删除
	 * @param ids
	 */
	public void removeDate(String[] ids) {
		for(int i=0;i<ids.length;i++){
			long date = Long.parseLong(ids[i]);
			ApiRobotEsi data = apiRobotEsiDao.findById(date);
			if(data == null)continue;
			apiRobotEsiDao.remove(data);
		}
	}
}
