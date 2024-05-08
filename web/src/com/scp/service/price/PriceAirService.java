package com.scp.service.price;

import javax.annotation.Resource;

import org.operamasks.org.json.simple.JSONArray;
import org.operamasks.org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.scp.dao.price.PriceAirDao;
import com.scp.model.price.PriceAir;
import com.scp.util.AppUtils;

@Component
public class PriceAirService{
	
	@Resource
	public PriceAirDao priceAirDao;
	
	public void saveData(PriceAir data) {
		if (0 > data.getId()) {
			priceAirDao.create(data);
		} else {
			priceAirDao.modify(data);
		}
	}
	
	public void removes(String[] ids) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String id : ids) {
			String sql = "\nUPDATE price_air SET isdelete = TRUE , updater = '"+AppUtils.getUserSession().getUsercode()+"' , updatetime = NOW()  WHERE id = " + id + ";";
			stringBuffer.append(sql);
		}
		priceAirDao.executeSQL(stringBuffer.toString());
		
	}
	
	public void updateBatchEditGrid(Object modifiedData) {
		JSONArray jsonArray = (JSONArray) modifiedData;
		PriceAir ddata = new PriceAir();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			String id=String.valueOf(jsonObject.get("id"));
			String pol=String.valueOf(jsonObject.get("pol")).trim();
			String pod=String.valueOf(jsonObject.get("pod")).trim();
			String via=String.valueOf(jsonObject.get("via"));
			String line=String.valueOf(jsonObject.get("line"));
			ddata = priceAirDao.findById(Long.valueOf(id));
			ddata.setPol(pol);
			ddata.setPod(pod);
			ddata.setVia(via);
			ddata.setLine(line);
			saveData(ddata);
		}		
	}
}