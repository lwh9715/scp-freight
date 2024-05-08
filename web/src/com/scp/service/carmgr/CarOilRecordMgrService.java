package com.scp.service.carmgr;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.car.CarOliRecordDao;
import com.scp.model.car.CarOliRecord;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class CarOilRecordMgrService{
	

	
	
	@Resource
	public CarOliRecordDao carOliRecordDao; 

	public void saveData(CarOliRecord data) {
		if(0 == data.getId()){
			carOliRecordDao.create(data);
		}else{
			carOliRecordDao.modify(data);
		}
	}

	public void updateCheck(String[] ids) throws Exception {
		String updater=AppUtils.getUserSession().getUsername();
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE car_oli_record SET ischeck = true ,updater = '"+updater+"' ,updatetime = NOW() WHERE id in ("+id+")";
		carOliRecordDao.executeSQL(sql);
	}


	public void updateCancelCheck(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE car_oli_record SET ischeck = false  WHERE id in ("+id+")";
		carOliRecordDao.executeSQL(sql);
		
	}
	
	public void removeDate(Long id) {
		
		String sql = "update car_oli_record set isdelete=TRUE WHERE id = " + id
				+ ";";
		carOliRecordDao.executeSQL(sql);
	
} 
	
}