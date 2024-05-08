package com.scp.service.bus;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.bus.AirPlaneTypeDao;
import com.scp.model.bus.AirPlaneType;

@Component
public class AirPlaneTypeService {

	@Resource
	public AirPlaneTypeDao airPlaneTypeDao;
	
	public void saveData(AirPlaneType data) {
		if(0 == data.getId()){
			airPlaneTypeDao.create(data);
		}else{
			airPlaneTypeDao.modify(data);
		}
	}
	
	public void removeDate(Long id) {
		String sql="UPDATE dat_airplanetype SET isdelete=TRUE where id=" + id;
		//String sql2 = "UPDATE dat_airplanetype_link SET isdelete=TRUE where airplanetypeid = "+id;
		airPlaneTypeDao.executeSQL(sql);
		//airPlaneTypeDao.executeSQL(sql2);
	}

	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		AirPlaneType data = new AirPlaneType();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			
			data = airPlaneTypeDao.findById(Long.parseLong(String.valueOf(jsonObject.get("id"))));
			if(jsonObject.get("planetype")!=null){
				data.setPlanetype(String.valueOf(jsonObject.get("planetype")));
			}
			saveData(data);
		}
		
	}

	public void addBatchEditGrid(Object addData) {
		JSONArray jsonArray = (JSONArray) addData;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			AirPlaneType data = new AirPlaneType();
			if(jsonObject.get("planetype")!=null){
				data.setPlanetype(String.valueOf(jsonObject.get("planetype")));
			}
			saveData(data);
		}
	}

	public void removedBatchEditGrid(Object removedData) {
		// TODO Auto-generated method stub
	}

}