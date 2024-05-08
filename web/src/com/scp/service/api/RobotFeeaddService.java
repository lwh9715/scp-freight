package com.scp.service.api;

import javax.annotation.Resource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.scp.dao.api.RobotFeeaddDao;
import com.scp.model.api.ApiRobotFeeadd;

@Component
@Lazy(true)
public class RobotFeeaddService {
	
	@Resource
	public RobotFeeaddDao robotFeeaddDao; 

	public void saveData(ApiRobotFeeadd data) {
		if(0 == data.getId()){
			robotFeeaddDao.create(data);
		}else{
			robotFeeaddDao.modify(data);
		}
	}

	public void removeDate(long ids) {
		ApiRobotFeeadd data = robotFeeaddDao.findById(ids);
		if(data == null)return;
		robotFeeaddDao.remove(data);
	} 
	/**
	 * 批量删除
	 * @param ids
	 */
	public void removeDate(String[] ids) {
		for(int i=0;i<ids.length;i++){
			long date = Long.parseLong(ids[i]);
			ApiRobotFeeadd data = robotFeeaddDao.findById(date);
			if(data == null)continue;
			robotFeeaddDao.remove(data);
		}
	}
}
