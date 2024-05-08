package com.scp.service.ship;

import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.order.BusOrderDao;
import com.scp.dao.ship.BusShipAddresDao;
import com.scp.model.order.BusOrder;
import com.scp.model.ship.BusShipAddres;
import com.scp.util.AppUtils;

@Component
public class BusShipAddresMgrService{
	

	
	
	@Resource
	public BusShipAddresDao busShipAddresDao; 
	
	@Resource
	public BusOrderDao busOrderDao; 
	

	public void saveData(BusShipAddres data) {
		if(0 == data.getId()){
			busShipAddresDao.create(data);
		}else{
			busShipAddresDao.modify(data);
		}
	}

	public void removeDate(Long id) {
		BusShipAddres data = busShipAddresDao.findById(id);
		busShipAddresDao.remove(data);
	}
	
	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		BusShipAddres data = new BusShipAddres();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			data = busShipAddresDao.findById(Long.parseLong(String.valueOf(jsonObject.get("id"))));
			if(jsonObject.get("cneename")!=null){
				data.setCneename(String.valueOf(jsonObject.get("cneename")));
			}else{
				data.setCneename(null);
			}
			if(jsonObject.get("sendaddress")!=null){
				data.setSendaddress(String.valueOf(jsonObject.get("sendaddress")));
			}else{
				data.setSendaddress(null);
			}
			if(jsonObject.get("sendcontact")!=null){
				data.setSendcontact(String.valueOf(jsonObject.get("sendcontact")));
			}else{
				data.setSendcontact(null);
			}
			if(jsonObject.get("sendtel")!=null){
				data.setSendtel(String.valueOf(jsonObject.get("sendtel")));
			}else{
				data.setSendtel(null);
			}
			saveData(data);
		}
	}

	public void addBatchEditGrid(Object addData, long customsid) {
		JSONArray jsonArray = (JSONArray) addData;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			BusShipAddres data = new BusShipAddres();
			data.setLinkid(customsid);
			BusOrder busOrder = busOrderDao.findById(customsid);//'O 订单 C 清关派送'
			if(busOrder!=null){
				data.setLinktype("O");
			}else{
				data.setLinktype("C");
			}
			if(jsonObject.get("cneename")!=null){
				data.setCneename(String.valueOf(jsonObject.get("cneename")));
			}else{
				data.setCneename(null);
			}
			if(jsonObject.get("sendaddress")!=null){
				data.setSendaddress(String.valueOf(jsonObject.get("sendaddress")));
			}else{
				data.setSendaddress(null);
			}
			if(jsonObject.get("sendcontact")!=null){
				data.setSendcontact(String.valueOf(jsonObject.get("sendcontact")));
			}else{
				data.setSendcontact(null);
			}
			if(jsonObject.get("sendtel")!=null){
				data.setSendtel(String.valueOf(jsonObject.get("sendtel")));
			}else{
				data.setSendtel(null);
			}
			saveData(data);
		}
	}
	
	public void removedBatchEditGrid(Object removedData) {
		JSONArray jsonArray = (JSONArray) removedData;
		BusShipAddres data = new BusShipAddres();
		String ids = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			if(jsonObject.get("id")!=null&&!String.valueOf(jsonObject.get("id")).isEmpty()&&Pattern.matches("^\\d+$", String.valueOf(jsonObject.get("id")))){
				ids += String.valueOf(jsonObject.get("id"))+",";
			}
		}
		if(!ids.isEmpty()){
			ids = ids.substring(0, ids.length()-1);
			String sql = "DELETE FROM bus_ship_addres WHERE id IN ("+ids+");";
			busShipAddresDao.executeSQL(sql);
		}
	}
	
	public void selectAdd(String[] ids,Long customsid){
		if(ids!=null&&ids.length>0){
			for(int i=0;i<ids.length;i++){
				BusShipAddres busShipAddres = busShipAddresDao.findById(Long.parseLong(ids[i]));
				BusShipAddres busAddres = new BusShipAddres();
				busAddres.setLinkid(customsid);
				busAddres.setCneename(busShipAddres.getCneename());
				busAddres.setInputer(AppUtils.getUserSession().getUsercode());
				busAddres.setLinktype(busShipAddres.getLinktype());
				busAddres.setSendaddress(busShipAddres.getSendaddress());
				busAddres.setSendcontact(busShipAddres.getSendcontact());
				busAddres.setSendtel(busShipAddres.getSendtel());
				busShipAddresDao.create(busAddres);
			}
		}
	}
	
	public void removeSelections(String[] ids){
		if(ids!=null&&ids.length>0){
			for(int i=0;i<ids.length;i++){
				String sql = "DELETE FROM bus_ship_addres WHERE id ="+ids[i];
				busShipAddresDao.executeSQL(sql);
			}
		}
	}
}