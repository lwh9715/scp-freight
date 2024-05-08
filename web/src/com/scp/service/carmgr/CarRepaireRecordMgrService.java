package com.scp.service.carmgr;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.scp.dao.car.CarRepaireRecordDao;
import com.scp.model.car.CarRepaireRecord;
import com.scp.util.AppUtils;
import com.scp.util.StrUtils;

@Component
public class CarRepaireRecordMgrService{
	

	
	
	@Resource
	public CarRepaireRecordDao carRepaireRecordDao; 

	public void saveData(CarRepaireRecord data) {
		if(0 == data.getId()){
			carRepaireRecordDao.create(data);
		}else{
			carRepaireRecordDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		
			String sql = "update car_repaire_record set isdelete=TRUE WHERE id = " + id
					+ ";";
			carRepaireRecordDao.executeSQL(sql);
		
	} 
	
	
	
	public void updateCheck(String[] ids) throws Exception {
		String updater=AppUtils.getUserSession().getUsername();
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE car_repaire_record SET ischeck = true ,updater = '"+updater+"' ,updatetime = NOW() WHERE id in ("+id+")";
		carRepaireRecordDao.executeSQL(sql);
	}


	public void updateCancelCheck(String[] ids) {
		String id = StrUtils.array2List(ids);
		String sql = "UPDATE car_repaire_record SET ischeck = false  WHERE id in ("+id+")";
		carRepaireRecordDao.executeSQL(sql);
		
	}
	
	
	
	
	
}