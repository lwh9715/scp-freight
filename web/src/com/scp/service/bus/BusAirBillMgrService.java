package com.scp.service.bus;

import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.bus.BusAirBillDao;
import com.scp.model.bus.BusAirBill;
import com.scp.model.ship.BusPacklist;

@Component
public class BusAirBillMgrService {

	@Resource
	public BusAirBillDao busAirBillDao;
	
	public void saveData(BusAirBill data) {
		if(0 == data.getId()){
			busAirBillDao.create(data);
		}else{
			busAirBillDao.modify(data);
		}
	}
	
	public void removeDate(Long id) {
		String sql="UPDATE bus_air_bill SET isdelete=TRUE where id=" + id;
		busAirBillDao.executeSQL(sql);
	}
	
	
	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		BusAirBill data = new BusAirBill();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			data = busAirBillDao.findById(Long.parseLong(String.valueOf(jsonObject.get("id"))));
			if(jsonObject.get("hawbno")!=null){
				data.setHawbno(String.valueOf(jsonObject.get("hawbno")));
			}else{
				data.setHawbno(null);
			}
			if(jsonObject.get("piece")!=null&&!jsonObject.get("piece").toString().equals("")){
				data.setPiece(String.valueOf(jsonObject.get("piece")));
			}else{
				data.setPiece(null);
			}
			if(jsonObject.get("ppccpaytype")!=null&&!jsonObject.get("ppccpaytype").toString().equals("")){
				data.setPpccpaytype(String.valueOf(jsonObject.get("ppccpaytype")));
			}else{
				data.setPpccpaytype(null);
			}
			saveData(data);
		}
	}
	
	
	
	public void addBatchEditGrid(Object addData, long airid, long jobid) {
		JSONArray jsonArray = (JSONArray) addData;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			BusAirBill data = new BusAirBill();
			data.setBusairid(airid);
			data.setJobid(jobid);
			
			if(jsonObject.get("hawbno")!=null){
				data.setHawbno(String.valueOf(jsonObject.get("hawbno")));
			}else{
				data.setHawbno(null);
			}
			if(jsonObject.get("piece")!=null&&!jsonObject.get("piece").toString().equals("")){
				data.setPiece(String.valueOf(jsonObject.get("piece")));
			}else{
				data.setPiece(null);
			}
			if(jsonObject.get("ppccpaytype")!=null&&!jsonObject.get("ppccpaytype").toString().equals("")){
				data.setPpccpaytype(String.valueOf(jsonObject.get("ppccpaytype")));
			}else{
				data.setPpccpaytype(null);
			}
			saveData(data);
		}
	}
	
	
	public void removedBatchEditGrid(Object removedData) {
		JSONArray jsonArray = (JSONArray) removedData;
		BusPacklist data = new BusPacklist();
		String ids = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			if(jsonObject.get("id")!=null&&!String.valueOf(jsonObject.get("id")).isEmpty()&&Pattern.matches("^\\d+$", String.valueOf(jsonObject.get("id")))){
				ids += String.valueOf(jsonObject.get("id"))+",";
			}
		}
		if(!ids.isEmpty()){
			ids = ids.substring(0, ids.length()-1);
			String sql = "DELETE FROM bus_air_bill WHERE id IN ("+ids+");";
			busAirBillDao.executeSQL(sql);
		}
	}
	
	

}
