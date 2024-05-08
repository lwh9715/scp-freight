package com.scp.service.price;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.price.PriceTruckDao;
import com.scp.model.price.PriceTruck;
import com.scp.util.AppUtils;

@Component
public class PriceTruckService{
	
	@Resource
	public PriceTruckDao priceTruckDao;
	
	public void saveData(PriceTruck data) {
		if (0 > data.getId()) {
			priceTruckDao.create(data);
		} else {
			priceTruckDao.modify(data);
		}
	}
	
	public void removes(String[] ids) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_truck SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		priceTruckDao.executeSQL(stringBuffer.toString());
		
	}
	
	public void removePriceByPriceName(String pricename) {
		String sql = "UPDATE price_truck SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"', updatetime = NOW() WHERE pricename = '"+pricename+"' AND isdelete = FALSE";
		priceTruckDao.executeSQL(sql);
	}
	
	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		PriceTruck ddata = new PriceTruck();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			String id=String.valueOf(jsonObject.get("id"));
			String pod=String.valueOf(jsonObject.get("pod")).trim();
			ddata = priceTruckDao.findById(Long.valueOf(id));
			ddata.setPod(pod);
			saveData(ddata);
		}		
	}
}